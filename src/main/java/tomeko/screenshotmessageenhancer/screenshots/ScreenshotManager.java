package tomeko.screenshotmessageenhancer.screenshots;

import ca.weblite.objc.Client;
import ca.weblite.objc.Proxy;
import com.mojang.blaze3d.platform.NativeImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ScreenshotManager {
    public static ArrayList<NativeImage> screenshotImages = new ArrayList<>();
    public static ArrayList<File> screenshotFiles = new ArrayList<>();
    private static final Minecraft client = Minecraft.getInstance();

    public static void copyScreenshot(int pos) {
        if (pos >= screenshotImages.toArray().length) return;

        Component message = Component.literal("Screenshot copied to clipboard!").withStyle(style -> style.withColor(ChatFormatting.GREEN));

        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac")) {
            Client macClient = Client.getInstance();
            Proxy url = macClient.sendProxy("NSURL", "fileURLWithPath:", screenshotFiles.get(pos).getPath());

            Proxy image = macClient.sendProxy("NSImage", "alloc");
            image.send("initWithContentsOfURL:", url);

            Proxy array = macClient.sendProxy("NSArray", "array");
            array = array.sendProxy("arrayByAddingObject:", image);

            Proxy pasteboard = macClient.sendProxy("NSPasteboard", "generalPasteboard");
            pasteboard.send("clearContents");
            pasteboard.sendBoolean("writeObjects:", array);

            if (client.player != null) {
                //? if >= 26.1 {
                /*client.gui.getChat().addClientSystemMessage(message);
                *///?} else {
                client.player.displayClientMessage(message, false);
                //?}
            }
            return;
        }

        BufferedImage image = convert(screenshotImages.get(pos));
        ImageContent content = new ImageContent(image);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(content, null);


        if (client.player != null) {
            //? if >= 26.1 {
            /*client.gui.getChat().addClientSystemMessage(message);
            *///?} else {
            client.player.displayClientMessage(message, false);
            //?}
        }
    }

    public static void deleteScreenshot(int pos) {
        if (pos >= screenshotFiles.toArray().length || !screenshotFiles.get(pos).exists()) return;

        screenshotFiles.get(pos).delete();

        Component message = Component.literal("Screenshot deleted!").withStyle(style -> style.withColor(ChatFormatting.RED));
        if (client.player != null) {
            //? if >= 26.1 {
            /*client.gui.getChat().addClientSystemMessage(message);
            *///?} else {
            client.player.displayClientMessage(message, false);
            //?}
        }
    }

    private static BufferedImage convert(NativeImage image) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                out.setRGB(x, y, image.getPixel(x, y));
            }
        }
        return out;
    }
}
