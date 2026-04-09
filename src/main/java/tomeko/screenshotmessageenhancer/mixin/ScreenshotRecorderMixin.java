package tomeko.screenshotmessageenhancer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig;
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager;
import tomeko.screenshotmessageenhancer.utils.Constants;
import com.mojang.blaze3d.pipeline.RenderTarget;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
//? if >= 1.21.11 {
/*import net.minecraft.util.Util;
 *///?} else {
import net.minecraft.Util;
//?}
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

import static tomeko.screenshotmessageenhancer.utils.ScreenshotMessageEnhancerUtils.threadExecutor;

@Mixin(Screenshot.class)
public class ScreenshotRecorderMixin {
    @Inject(at = @At("HEAD"), method = "grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;ILjava/util/function/Consumer;)V", cancellable = true)
    private static void saveScreenshot(File gameDirectory, String fileName, RenderTarget framebuffer, int downscaleFactor, Consumer<Component> messageReceiver, CallbackInfo ci) {
        if (!ScreenshotMessageEnhancerConfig.modifyScreenshotMessageEnabled) return;

        Screenshot.takeScreenshot(framebuffer, (nativeImage) -> {
            File screenshotsFolder = new File(gameDirectory, "screenshots");
            File screenshotFile;
            if (fileName == null) {
                screenshotFile = getScreenshotFilename(screenshotsFolder);
            } else {
                screenshotFile = new File(screenshotsFolder, fileName);
            }

            AtomicBoolean shouldReturn = new AtomicBoolean(false);
            threadExecutor.submit(() -> {
                try {
                    nativeImage.writeToFile(screenshotFile);
                } catch (Exception ignored) {
                } finally {
                    ci.cancel();
                    shouldReturn.set(true);
                }
            });

            if (shouldReturn.get()) return;

            ScreenshotManager.screenshotImages.add(nativeImage);
            ScreenshotManager.screenshotFiles.add(screenshotFile);
            MutableComponent message = Component.literal("Saved screenshot");

            if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddName) {
                message.append(Component.literal(" as "));
                message.append(Component.literal(screenshotFile.getName()).withStyle(ChatFormatting.UNDERLINE));
            }

            if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddCopy) {
                message.append(" ");
                message.append(Component.literal("[COPY]").withStyle(ChatFormatting.BOLD, ChatFormatting.BLUE).withStyle(style -> style
                        .withClickEvent(new ClickEvent.RunCommand(Constants.SCREENSHOT_COPY_COMMAND + " " + (ScreenshotManager.screenshotImages.toArray().length - 1)))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Copy the screenshot")))));
            }

            if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddOpen) {
                message.append(" ");
                message.append(Component.literal("[OPEN]").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN).withStyle(style -> style
                        .withClickEvent(new ClickEvent.OpenFile(screenshotFile.getAbsolutePath()))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Open " + screenshotFile.getName())))));
            }

            if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddOpenFolder) {
                message.append(" ");
                message.append(Component.literal("[OPEN FOLDER]").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD).withStyle(style -> style
                        .withClickEvent(new ClickEvent.OpenFile(screenshotsFolder.getAbsolutePath()))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal(screenshotsFolder.getPath())))));
            }

            if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddDelete) {
                message.append(" ");
                message.append(Component.literal("[DELETE]").withStyle(ChatFormatting.BOLD, ChatFormatting.RED).withStyle(style -> style
                        .withClickEvent(new ClickEvent.RunCommand(Constants.SCREENSHOT_DELETE_COMMAND + " " + (ScreenshotManager.screenshotFiles.toArray().length - 1)))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Delete the screenshot")))));
            }

            messageReceiver.accept(message);
        });
        ci.cancel();
    }

    private static File getScreenshotFilename(File directory) {
        String time = Util.getFilenameFormattedDateTime();
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
