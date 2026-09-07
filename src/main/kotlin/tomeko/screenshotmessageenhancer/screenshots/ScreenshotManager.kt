package tomeko.screenshotmessageenhancer.screenshots

//? if 1.8.9 {
/*import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.EnumChatFormatting
*///?} else {
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
//? if >= 1.21.11 {
import net.minecraft.util.Util
//?} else {
//import net.minecraft.Util
//?}
//?}
import tomeko.screenshotmessageenhancer.compat.getStyledChatMessage
import tomeko.screenshotmessageenhancer.compat.sendChatMessage
//? if 1.8.9 {
/*import tomeko.screenshotmessageenhancer.utils.threadExecutor
import java.awt.Desktop
*///?}

import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import java.util.ArrayList
import javax.imageio.ImageIO

object ScreenshotManager {
    val screenshotFiles: ArrayList<File> = ArrayList()

    private val mc: Minecraft =
    //? if = 1.8.9 {
    //Minecraft.getMinecraft()

        //?} else {
        Minecraft.getInstance()

    //?}
    fun copyScreenshot(pos: Int, showMessage: Boolean) {
        if (pos >= screenshotFiles.size) return

        val file = screenshotFiles[pos]
        if (!file.exists()) return

        //? if 1.8.9 {
        //threadExecutor.execute {
        //?} else {
        Util.ioPool().execute {
            //?}
            try {
                val image: BufferedImage? = ImageIO.read(file)

                if (image != null) {
                    val content = ImageContent(image)

                    Toolkit.getDefaultToolkit().systemClipboard.setContents(content, null)

                    //? if 1.8.9 {
                    //mc.addScheduledTask {
                    //?} else {
                    mc.execute {
                        //?}
                        sendChatMessage(
                            getStyledChatMessage(
                                "Screenshot copied to clipboard!",
                                //? if 1.8.9 {
                                //EnumChatFormatting.GREEN
                                //?} else {
                                ChatFormatting.GREEN
                                //?}
                            ),
                            showMessage
                        )
                    }
                }
            } catch (e: Exception) {
                //? if 1.8.9 {
                //mc.addScheduledTask {
                //?} else {
                mc.execute {
                    //?}
                    sendChatMessage(
                        getStyledChatMessage(
                            "Failed to read screenshot file for clipboard.",
                            //? if 1.8.9 {
                            //EnumChatFormatting.RED
                            //?} else {
                            ChatFormatting.RED
                            //?}
                        ),
                        showMessage
                    )
                }

                e.printStackTrace()
            }
        }
    }

    fun deleteScreenshot(pos: Int) {
        if (pos >= screenshotFiles.size) return

        //? if 1.8.9 {
        //threadExecutor.execute {
        //?} else {
        Util.ioPool().execute {
            //?}
            val file = screenshotFiles[pos]

            if (file.exists() && file.delete()) {
                //? if 1.8.9 {
                //mc.addScheduledTask {
                //?} else {
                mc.execute {
                    //?}
                    sendChatMessage(
                        getStyledChatMessage(
                            "Screenshot deleted!",
                            //? if 1.8.9 {
                            //EnumChatFormatting.RED
                            //?} else {
                            ChatFormatting.RED
                            //?}
                        ),
                        true
                    )
                }
            } else {
                //? if 1.8.9 {
                //mc.addScheduledTask {
                //?} else {
                mc.execute {
                    //?}
                    sendChatMessage(
                        getStyledChatMessage(
                            "Couldn't delete screenshot (File not found)",
                            //? if 1.8.9 {
                            //EnumChatFormatting.GOLD
                            //?} else {
                            ChatFormatting.GOLD
                            //?}
                        ),
                        true
                    )
                }
            }
        }
    }

    //? if 1.8.9 {
    /*fun openScreenshot(pos: Int) {
        if (pos >= screenshotFiles.size) return

        val file = screenshotFiles[pos]
        if (!file.exists()) return

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openScreenshotFolder() {
        val folder = File(Minecraft.getMinecraft().mcDataDir, "screenshots")

        if (!folder.exists()) {
            folder.mkdirs()
        }

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(folder)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    *///?}

    fun uploadScreenshot(pos: Int) {
        if (pos >= screenshotFiles.size) return

        val file = screenshotFiles[pos]
        if (!file.exists()) return

        sendChatMessage(
            getStyledChatMessage(
                "Uploading screenshot...",
                //? if 1.8.9 {
                //EnumChatFormatting.YELLOW
                //?} else {
                ChatFormatting.YELLOW
                //?}
            ),
            true
        )

        //? if 1.8.9 {
        //threadExecutor.execute {
        //?} else {
        Util.ioPool().execute {
            //?}
            try {
                ScreenshotUploader.upload(file).thenAccept { url ->
                    //? if 1.8.9 {
                    //GuiScreen.setClipboardString(url)
                    //?} else {
                    mc.keyboardHandler.clipboard = url
                    //?}

                    //? if 1.8.9 {
                    //mc.addScheduledTask {
                    //?} else {
                    mc.execute {
                        //?}
                        sendChatMessage(
                            getStyledChatMessage(
                                "Screenshot uploaded + link copied to clipboard!",
                                //? if 1.8.9 {
                                //EnumChatFormatting.YELLOW
                                //?} else {
                                ChatFormatting.YELLOW
                                //?}
                            ),
                            true
                        )
                    }
                }.exceptionally { error ->
                    error.printStackTrace()
                    null
                }
            } catch (_: Exception) {
                //? if 1.8.9 {
                //mc.addScheduledTask {
                //?} else {
                mc.execute {
                    //?}
                    sendChatMessage(
                        getStyledChatMessage(
                            "Failed to upload screenshot!",
                            //? if 1.8.9 {
                            //EnumChatFormatting.RED
                            //?} else {
                            ChatFormatting.RED
                            //?}
                        ),
                        true
                    )
                }
            }
        }
    }
}