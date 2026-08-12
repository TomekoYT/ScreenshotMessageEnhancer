package tomeko.screenshotmessageenhancer.screenshots

import ca.weblite.objc.Client
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.util.Util
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import java.util.ArrayList
import java.util.Locale
import javax.imageio.ImageIO

object ScreenshotManager {

    @JvmField
    val screenshotFiles: ArrayList<File> = ArrayList()

    private val client: Minecraft = Minecraft.getInstance()

    @JvmStatic
    fun copyScreenshot(pos: Int, showMessage: Boolean) {
        if (pos >= screenshotFiles.size) return

        val file = screenshotFiles[pos]
        if (!file.exists()) return

        val message = Component.literal("Screenshot copied to clipboard!")
            .withStyle { style -> style.withColor(ChatFormatting.GREEN) }

        if (System.getProperty("os.name").lowercase(Locale.ROOT).contains("mac")) {
            Util.ioPool().execute {
                try {
                    val macClient = Client.getInstance()
                    val url = macClient.sendProxy(
                        "NSURL",
                        "fileURLWithPath:",
                        file.path
                    )

                    val image = macClient.sendProxy("NSImage", "alloc")
                    image.send("initWithContentsOfURL:", url)

                    var array = macClient.sendProxy("NSArray", "array")
                    array = array.sendProxy("arrayByAddingObject:", image)

                    val pasteboard = macClient.sendProxy(
                        "NSPasteboard",
                        "generalPasteboard"
                    )

                    pasteboard.send("clearContents")
                    pasteboard.sendBoolean("writeObjects:", array)

                    client.execute {
                        sendChatMessage(message, showMessage)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return
        }

        Util.ioPool().execute {
            try {
                val image: BufferedImage? = ImageIO.read(file)

                if (image != null) {
                    val content = ImageContent(image)

                    Toolkit.getDefaultToolkit()
                        .systemClipboard
                        .setContents(content, null)

                    client.execute {
                        sendChatMessage(message, showMessage)
                    }
                }
            } catch (e: Exception) {
                val errorMessage = Component.literal(
                    "Failed to read screenshot file for clipboard."
                ).withStyle { style ->
                    style.withColor(ChatFormatting.RED)
                }

                client.execute {
                    sendChatMessage(errorMessage, showMessage)
                }

                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun deleteScreenshot(pos: Int) {
        if (pos >= screenshotFiles.size) return

        Util.ioPool().execute {
            val file = screenshotFiles[pos]

            if (file.exists() && file.delete()) {
                val message = Component.literal("Screenshot deleted!")
                    .withStyle { style ->
                        style.withColor(ChatFormatting.RED)
                    }

                client.execute {
                    sendChatMessage(message, true)
                }
            } else {
                val errorMessage = Component.literal(
                    "Could not delete screenshot (File not found)"
                ).withStyle { style ->
                    style.withColor(ChatFormatting.GOLD)
                }

                client.execute {
                    sendChatMessage(errorMessage, true)
                }
            }
        }
    }

    private fun sendChatMessage(message: Component, showMessage: Boolean) {
        if (!showMessage || client.player == null) return

        //? if >= 26.2 {
        /*client.gui.hud.chat.addClientSystemMessage(message)
        *///?} else if >= 26.1 {
        client.gui.chat.addClientSystemMessage(message)
        //?} else {
        /*client.gui.chat.addMessage(message)
        *///?}
    }
}