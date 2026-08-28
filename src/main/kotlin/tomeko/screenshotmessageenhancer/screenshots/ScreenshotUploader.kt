package tomeko.screenshotmessageenhancer.screenshots

import tomeko.screenshotmessageenhancer.utils.Constants
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.concurrent.CompletableFuture

object ScreenshotUploader {
    private val httpClient = HttpClient.newHttpClient()

    fun upload(file: File): CompletableFuture<String> {
        val boundary = "---MinecraftScreenshot${UUID.randomUUID()}"
        val body = createBody(file, boundary);

        val request = HttpRequest.newBuilder()
            .uri(URI.create(Constants.SCREENSHOT_UPLOAD_URL))
            .header("Content-Type", "multipart/form-data; boundary=$boundary")
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build()

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply { response ->

                if(response.statusCode() !in 200..299){
                    throw RuntimeException("Failed to upload screenshot")
                }

                val url = response.body().trim()

                url

            }
    }

    private fun createBody(file: File, boundary: String): ByteArray {
        val output = ByteArrayOutputStream()

        fun write(text: String){
            output.write(text.toByteArray(StandardCharsets.UTF_8))
        }

        write("--$boundary\r\n")
        write(
            "Content-Disposition: form-data; name=\"reqtype\"\r\n\r\n"
        )
        write("fileupload\r\n")

        write("--$boundary\r\n")
        write(
            "Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"${file.name}\"\r\n"
        )
        write("Content-Type: image/png\r\n\r\n")

        file.inputStream().use { input ->
            input.copyTo(output)
        }

        write("\r\n")

        write("--$boundary--\r\n")

        return output.toByteArray()
    }

}