package tomeko.screenshotmessageenhancer.screenshots;

import ca.weblite.objc.Client;
import ca.weblite.objc.Proxy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import javax.imageio.ImageIO; // Standard Java Image Loader

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

public class ScreenshotManager {
    public static final ArrayList<File> screenshotFiles = new ArrayList<>();
    private static final Minecraft client = Minecraft.getInstance();

    public static void copyScreenshot(int pos) {
        if (pos >= screenshotFiles.size()) return;

        File file = screenshotFiles.get(pos);
        if (!file.exists()) return;

        Component message = Component.literal("Screenshot copied to clipboard!").withStyle(style -> style.withColor(ChatFormatting.GREEN));

        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac")) {
            Util.ioPool().execute(() -> {
                try {
                    Client macClient = Client.getInstance();
                    Proxy url = macClient.sendProxy("NSURL", "fileURLWithPath:", file.getPath());

                    Proxy image = macClient.sendProxy("NSImage", "alloc");
                    image.send("initWithContentsOfURL:", url);

                    Proxy array = macClient.sendProxy("NSArray", "array");
                    array = array.sendProxy("arrayByAddingObject:", image);

                    Proxy pasteboard = macClient.sendProxy("NSPasteboard", "generalPasteboard");
                    pasteboard.send("clearContents");
                    pasteboard.sendBoolean("writeObjects:", array);

                    client.execute(() -> sendChatMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        // Windows / Linux
        Util.ioPool().execute(() -> {
            try {
                // Safely read the image directly from the disk file
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    ImageContent content = new ImageContent(image);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(content, null);
                    client.execute(() -> sendChatMessage(message));
                }
            } catch (Exception e) {
                Component errorMessage = Component.literal("Failed to read screenshot file for clipboard.").withStyle(style -> style.withColor(ChatFormatting.RED));
                client.execute(() -> sendChatMessage(errorMessage));
                e.printStackTrace();
            }
        });
    }

    public static void deleteScreenshot(int pos) {
        if (pos >= screenshotFiles.size()) return;

        Util.ioPool().execute(() -> {
            File file = screenshotFiles.get(pos);
            if (file.exists() && file.delete()) {
                Component message = Component.literal("Screenshot deleted!").withStyle(style -> style.withColor(ChatFormatting.RED));
                client.execute(() -> sendChatMessage(message));
            } else {
                Component errorMessage = Component.literal("Could not delete screenshot (File not found)").withStyle(style -> style.withColor(ChatFormatting.GOLD));
                client.execute(() -> sendChatMessage(errorMessage));
            }
        });
    }

    private static void sendChatMessage(Component message) {
        if (client.player != null) {
            //? if >= 26.2 {
            client.gui.hud.getChat().addClientSystemMessage(message);
            //?} else {
            /*client.gui.getChat().addClientSystemMessage(message);
             *///?}
        }
    }
}