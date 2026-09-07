package tomeko.screenshotmessageenhancer.screenshots

import tomeko.screenshotmessageenhancer.utils.Debug
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.stream.FileImageOutputStream

object ScreenshotCompressor {
    fun compress(file: File, pos: Int) {
        if (!file.exists()) return

        val originalSize = file.length()

        try {
            val image = ImageIO.read(file)
            if (image == null) {
                Debug.log("Compression skipped, couldn't decode ${file.name}")
                return
            }

            val writers = ImageIO.getImageWritersByFormatName("png")
            if (!writers.hasNext()) {
                Debug.log("Compression skipped, no PNG writer available")
                return
            }

            val writer = writers.next()
            val tempFile = File.createTempFile("sme_compress_", ".png", file.parentFile)

            try {
                val writeParam = writer.defaultWriteParam

                if (writeParam.canWriteCompressed()) {
                    writeParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
                    writeParam.compressionQuality = 0.4f
                }

                FileImageOutputStream(tempFile).use { output ->
                    writer.output = output
                    writer.write(null, IIOImage(image, null, null), writeParam)
                }

                val compressedSize = tempFile.length()
                if (compressedSize in 1 until originalSize) {
                    try {
                        Files.move(
                            tempFile.toPath(),
                            file.toPath(),
                            StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.ATOMIC_MOVE
                        )
                        tempFile.copyTo(ScreenshotManager.screenshotFiles[pos])
                        val savedPercent = ((originalSize - compressedSize) * 100 / originalSize)
                        Debug.log("Compressed ${file.name}: $originalSize -> $compressedSize bytes (-$savedPercent%)")
                    } catch (e: IOException) {
                        Debug.log("Compression skipped, couldn't replace ${file.name}: ${e.message}")
                        tempFile.delete()
                    }
                } else {
                    Debug.log("Compression skipped, no size reduction for ${file.name}")
                    tempFile.delete()
                }
            } finally {
                writer.dispose()
            }
        } catch (e: IOException) {
            Debug.log("Failed to compress screenshot ${file.name}: ${e.message}")
        } catch (e: Exception) {
            Debug.log("Failed to compress screenshot ${file.name}: ${e.message}")
        }
    }
}
