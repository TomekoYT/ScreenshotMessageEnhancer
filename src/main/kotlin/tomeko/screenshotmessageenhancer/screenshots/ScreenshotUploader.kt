package tomeko.screenshotmessageenhancer.screenshots

import tomeko.screenshotmessageenhancer.utils.Constants
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.concurrent.CompletableFuture

object ScreenshotUploader {
    fun upload(file: File): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            handleUpload(file)
        }
    }

    private fun handleUpload(file: File): String {
        val boundary = "---MinecraftScreenshot${UUID.randomUUID()}"
        val body = createBody(file, boundary)

        val url = URI.create(Constants.SCREENSHOT_UPLOAD_URL).toURL()
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.doInput = true

            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            connection.setRequestProperty("Content-Length", body.size.toString())

            connection.outputStream.use { output ->
                output.write(body)
                output.flush()
            }

            val statusCode = connection.responseCode
            if (statusCode !in 200..299)
                throw RuntimeException("Failed to upload screenshot (HTTP $statusCode)")

            return connection.inputStream
                .bufferedReader(StandardCharsets.UTF_8)
                .use { it.readText() }
                .trim()

        } finally {
            connection.disconnect()
        }
    }

    private fun createBody(file: File, boundary: String): ByteArray {
        val output = ByteArrayOutputStream()

        fun write(text: String) {
            output.write(text.toByteArray(StandardCharsets.UTF_8))
        }

        write("--$boundary\r\n")
        write("Content-Disposition: form-data; name=\"reqtype\"\r\n\r\n")
        write("fileupload\r\n")

        write("--$boundary\r\n")
        write("Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"${file.name}\"\r\n")
        write("Content-Type: image/png\r\n\r\n")

        file.inputStream().use { input ->
            input.copyTo(output)
        }

        write("\r\n")
        write("--$boundary--\r\n")

        return output.toByteArray()
    }
}