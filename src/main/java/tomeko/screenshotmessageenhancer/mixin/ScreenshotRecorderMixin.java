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
import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

@Mixin(Screenshot.class)
public class ScreenshotRecorderMixin {
    @Inject(at = @At("HEAD"), method = "grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;ILjava/util/function/Consumer;)V", cancellable = true)
    private static void saveScreenshot(File workDir, String forceName, RenderTarget target, int downscaleFactor, Consumer<Component> callback, CallbackInfo ci) {
        if (!ScreenshotMessageEnhancerConfig.modifyScreenshotMessageEnabled) return;

        ci.cancel();

        Screenshot.takeScreenshot(target, (nativeImage) -> {
            File screenshotsFolder = new File(workDir, "screenshots");

            if (!screenshotsFolder.exists()) {
                screenshotsFolder.mkdirs();
            }

            File screenshotFile;
            if (forceName == null) {
                screenshotFile = getScreenshotFilename(screenshotsFolder);
            } else {
                screenshotFile = new File(screenshotsFolder, forceName);
            }

            File accessibleScreenshotFile;
            File accessibleScreenshotsFolder;
            try {
                accessibleScreenshotFile = screenshotFile.getCanonicalFile();
                accessibleScreenshotsFolder = screenshotsFolder.getCanonicalFile();
            } catch (Exception e) {
                accessibleScreenshotFile = screenshotFile.getAbsoluteFile();
                accessibleScreenshotsFolder = screenshotsFolder.getAbsoluteFile();
            }

            File finalFile = accessibleScreenshotFile;
            File finalFolder = accessibleScreenshotsFolder;

            Util.ioPool().execute(() -> {
                try {
                    nativeImage.writeToFile(finalFile);

                    ScreenshotManager.screenshotFiles.add(finalFile);
                    int currentIdx = ScreenshotManager.screenshotFiles.size() - 1;

                    MutableComponent message = Component.literal("Saved screenshot");

                    if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddName) {
                        message.append(Component.literal(" as "));
                        message.append(Component.literal(finalFile.getName()).withStyle(ChatFormatting.UNDERLINE));
                    }

                    if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddCopy) {
                        message.append(" ");
                        message.append(Component.literal("[COPY]").withStyle(ChatFormatting.BOLD, ChatFormatting.BLUE).withStyle(style -> style
                                .withClickEvent(new ClickEvent.RunCommand(Constants.SCREENSHOT_COPY_COMMAND + " " + currentIdx))
                                .withHoverEvent(new HoverEvent.ShowText(Component.literal("Copy the screenshot")))));
                    }

                    if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddOpen) {
                        message.append(" ");
                        message.append(Component.literal("[OPEN]").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN).withStyle(style -> style
                                .withClickEvent(new ClickEvent.OpenFile(finalFile.getAbsolutePath()))
                                .withHoverEvent(new HoverEvent.ShowText(Component.literal("Open " + finalFile.getName())))));
                    }

                    if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddOpenFolder) {
                        message.append(" ");
                        message.append(Component.literal("[OPEN FOLDER]").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD).withStyle(style -> style
                                .withClickEvent(new ClickEvent.OpenFile(finalFolder.getAbsolutePath()))
                                .withHoverEvent(new HoverEvent.ShowText(Component.literal(finalFolder.getPath())))));
                    }

                    if (ScreenshotMessageEnhancerConfig.modifyScreenshotMessageAddDelete) {
                        message.append(" ");
                        message.append(Component.literal("[DELETE]").withStyle(ChatFormatting.BOLD, ChatFormatting.RED).withStyle(style -> style
                                .withClickEvent(new ClickEvent.RunCommand(Constants.SCREENSHOT_DELETE_COMMAND + " " + currentIdx))
                                .withHoverEvent(new HoverEvent.ShowText(Component.literal("Delete the screenshot")))));
                    }

                    callback.accept(message);
                } catch (Exception e) {
                    callback.accept(Component.literal("Failed to save screenshot: " + e.getMessage()).withStyle(ChatFormatting.RED));
                } finally {
                    nativeImage.close();
                }
            });
        });
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