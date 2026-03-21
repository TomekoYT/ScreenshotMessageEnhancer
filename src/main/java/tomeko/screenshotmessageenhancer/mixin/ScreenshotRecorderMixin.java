package tomeko.screenshotmessageenhancer.mixin;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig;
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager;
import tomeko.screenshotmessageenhancer.utils.Constants;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {
    @Inject(at = @At("HEAD"), method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/client/gl/Framebuffer;ILjava/util/function/Consumer;)V", cancellable = true)
    private static void saveScreenshot(File gameDirectory, String fileName, Framebuffer framebuffer, int downscaleFactor, Consumer<Text> messageReceiver, CallbackInfo ci) {
        ScreenshotRecorder.takeScreenshot(framebuffer, (nativeImage) -> {
            File screenshotsFolder = new File(gameDirectory, "screenshots");
            File screenshotFile;
            if (fileName == null) {
                screenshotFile = getScreenshotFilename(screenshotsFolder);
            } else {
                screenshotFile = new File(screenshotsFolder, fileName);
            }

            try {
                nativeImage.writeTo(screenshotFile);
                ScreenshotManager.screenshotImages.add(nativeImage);
                ScreenshotManager.screenshotFiles.add(screenshotFile);
                MutableText message = Text.literal("Saved screenshot");

                if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddName) {
                    message.append(Text.literal(" as "));
                    message.append(Text.literal(screenshotFile.getName()).formatted(Formatting.UNDERLINE));
                }

                if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddCopy) {
                    message.append(" ");
                    message.append(Text.literal("[COPY]").formatted(Formatting.BOLD, Formatting.BLUE).styled(style -> style
                            .withClickEvent(new ClickEvent.RunCommand(Constants.SCREENSHOT_COPY_COMMAND + " " + (ScreenshotManager.screenshotImages.toArray().length - 1)))
                            .withHoverEvent(new HoverEvent.ShowText(Text.literal("Copy the screenshot")))));
                }

                if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddOpen) {
                    message.append(" ");
                    message.append(Text.literal("[OPEN]").formatted(Formatting.BOLD, Formatting.GREEN).styled(style -> style
                            .withClickEvent(new ClickEvent.OpenFile(screenshotFile.getAbsolutePath()))
                            .withHoverEvent(new HoverEvent.ShowText(Text.literal("Open " + screenshotFile.getName())))));
                }

                if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddOpenFolder) {
                    message.append(" ");
                    message.append(Text.literal("[OPEN FOLDER]").formatted(Formatting.BOLD, Formatting.GOLD).styled(style -> style
                            .withClickEvent(new ClickEvent.OpenFile(screenshotsFolder.getAbsolutePath()))
                            .withHoverEvent(new HoverEvent.ShowText(Text.literal(screenshotsFolder.getPath())))));
                }

                if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddDelete) {
                    message.append(" ");
                    message.append(Text.literal("[DELETE]").formatted(Formatting.BOLD, Formatting.RED).styled(style -> style
                            .withClickEvent(new ClickEvent.RunCommand(Constants.SCREENSHOT_DELETE_COMMAND + " " + (ScreenshotManager.screenshotFiles.toArray().length - 1)))
                            .withHoverEvent(new HoverEvent.ShowText(Text.literal("Delete the screenshot")))));
                }

                messageReceiver.accept(message);
            } catch (Exception ignored) {
            } finally {
                ci.cancel();
            }
        });
        ci.cancel();
    }

    private static File getScreenshotFilename(File directory) {
        String time = Util.getFormattedCurrentTime();
        int i = 1;

        while (true) {
            String fileName;
            if (i == 1) {
                fileName = time + ".png";
            } else {
                fileName = time + "_" + i + ".png";
            }

            File file = new File(directory, fileName);
            if (!file.exists()) {
                return file;
            }
            i++;
        }
    }
}
