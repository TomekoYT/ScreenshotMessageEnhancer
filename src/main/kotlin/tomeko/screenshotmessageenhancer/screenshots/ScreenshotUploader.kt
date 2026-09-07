package tomeko.screenshotmessageenhancer.screenshots

import tomeko.screenshotmessageenhancer.utils.Constants
import java.io.BufferedOutputStream
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
        val boundary = "----MinecraftScreenshot${UUID.randomUUID()}"

        val url = URI.create(Constants.SCREENSHOT_UPLOAD_URL).toURL()
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.doInput = true
            connection.connectTimeout = 10_000
            connection.readTimeout = 30_000

            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            connection.setFixedLengthStreamingMode(calculateMultipartSize(file, boundary))

            BufferedOutputStream(connection.outputStream).use { output ->
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
                    input.copyTo(output, bufferSize = 65536)
                }

                write("\r\n")
                write("--$boundary--\r\n")
                output.flush()
            }

            val statusCode = connection.responseCode
            if (statusCode !in 200..299) {
                throw RuntimeException("Failed to upload screenshot (HTTP $statusCode)")
            }

            return connection.inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }.trim()
        } finally {
            connection.disconnect()
        }
    }

    private fun calculateMultipartSize(file: File, boundary: String): Long {
        fun size(text: String): Long = text.toByteArray(StandardCharsets.UTF_8).size.toLong()

        return (size("--$boundary\r\n")
                + size("Content-Disposition: form-data; name=\"reqtype\"\r\n\r\n")
                + size("fileupload\r\n")
                + size("--$boundary\r\n")
                + size("Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"${file.name}\"\r\n")
                + size("Content-Type: image/png\r\n\r\n")
                + file.length()
                + size("\r\n")
                + size("--$boundary--\r\n"))
    }
}