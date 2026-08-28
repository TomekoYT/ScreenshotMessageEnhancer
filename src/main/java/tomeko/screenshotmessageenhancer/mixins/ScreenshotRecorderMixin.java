package tomeko.screenshotmessageenhancer.mixins;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
//? if >= 1.21.11 {
import net.minecraft.util.Util;
//?} else {
/*import net.minecraft.Util;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig;
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager;
import tomeko.screenshotmessageenhancer.utils.Buttons;
import tomeko.screenshotmessageenhancer.utils.Constants;

import java.io.File;
import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotRecorderMixin {
    @Inject(
            method = "grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void saveScreenshot(
            File workDir,
            String forceName,
            RenderTarget target,
            Consumer<Component> callback,
            CallbackInfo ci
    ) {
        ci.cancel();

        //? if >= 1.21.11 {
        Screenshot.takeScreenshot(target, (nativeImage) -> {
        //?} else {
        /*NativeImage nativeImage = Screenshot.takeScreenshot(target);
        *///?}

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

                ScreenshotManager.INSTANCE.getScreenshotFiles().add(finalFile);
                int currentIdx = ScreenshotManager.INSTANCE.getScreenshotFiles().size() - 1;

                if (ScreenshotMessageEnhancerConfig.INSTANCE.getAutoCopyScreenshot()) {
                    ScreenshotManager.INSTANCE.copyScreenshot(currentIdx, false);
                }

                MutableComponent message = Component.literal("Saved screenshot");

                if (ScreenshotMessageEnhancerConfig.INSTANCE.getShowName()) {
                    message.append(Component.literal(" as "));
                    message.append(Component.literal(finalFile.getName()).withStyle(ChatFormatting.UNDERLINE));
                }

                if (ScreenshotMessageEnhancerConfig.INSTANCE.getButtons()[Buttons.COPY.ordinal()]) {
                    String command =
                            //? if = 1.21.1 {
                            /*"/" +
                                    *///?}
                                        Constants.SCREENSHOT_COPY_COMMAND + " " + currentIdx;
                    Component text = Component.literal("Copy the screenshot");

                    message.append(" ");
                    message.append(
                            Component.literal("[COPY]")
                                    .withStyle(ChatFormatting.BOLD, ChatFormatting.BLUE)
                                    .withStyle(style -> style
                                            .withClickEvent(
                                                    //? if >= 1.21.11 {
                                                    new ClickEvent.RunCommand(command)
                                                    //?} else {
                                                    /*new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                                                    *///?}
                                            )
                                            .withHoverEvent(
                                                    //? if >= 1.21.11 {
                                                    new HoverEvent.ShowText(text)
                                                    //?} else {
                                                    /*new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                    *///?}
                                            )
                                    )
                    );
                }

                if (ScreenshotMessageEnhancerConfig.INSTANCE.getButtons()[Buttons.OPEN.ordinal()]) {
                    String path = finalFile.getAbsolutePath();
                    Component text = Component.literal("Open " + finalFile.getName());

                    message.append(" ");
                    message.append(
                            Component.literal("[OPEN]")
                                    .withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN)
                                    .withStyle(style -> style
                                            .withClickEvent(
                                                    //? if >= 1.21.11 {
                                                    new ClickEvent.OpenFile(path)
                                                    //?} else {
                                                    /*new ClickEvent(ClickEvent.Action.OPEN_FILE, path)
                                                    *///?}
                                            )
                                            .withHoverEvent(
                                                    //? if >= 1.21.11 {
                                                    new HoverEvent.ShowText(text)
                                                    //?} else {
                                                    /*new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                    *///?}
                                            )
                                    )
                    );
                }

                if (ScreenshotMessageEnhancerConfig.INSTANCE.getButtons()[Buttons.OPEN_FOLDER.ordinal()]) {
                    String path = finalFolder.getAbsolutePath();
                    Component text = Component.literal(finalFolder.getPath());

                    message.append(" ");
                    message.append(
                            Component.literal("[OPEN FOLDER]")
                                    .withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD)
                                    .withStyle(style -> style
                                            .withClickEvent(
                                                    //? if >= 1.21.11 {
                                                    new ClickEvent.OpenFile(path)
                                                    //?} else {
                                                    /*new ClickEvent(ClickEvent.Action.OPEN_FILE, path)
                                                    *///?}
                                            )
                                            .withHoverEvent(
                                                    //? if >= 1.21.11 {
                                                    new HoverEvent.ShowText(text)
                                                    //?} else {
                                                    /*new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                    *///?}
                                            )
                                    )
                    );
                }

                if (ScreenshotMessageEnhancerConfig.INSTANCE.getButtons()[Buttons.DELETE.ordinal()]) {
                    String command =
                            //? if = 1.21.1 {
                            /*"/" +
                                    *///?}
                                    Constants.SCREENSHOT_DELETE_COMMAND + " " + currentIdx;
                    Component text = Component.literal("Delete the screenshot");

                    message.append(" ");
                    message.append(
                            Component.literal("[DELETE]")
                                    .withStyle(ChatFormatting.BOLD, ChatFormatting.RED)
                                    .withStyle(style -> style
                                            .withClickEvent(
                                                    //? if >= 1.21.11 {
                                                    new ClickEvent.RunCommand(command)
                                                    //?} else {
                                                    /*new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                                                    *///?}
                                            )
                                            .withHoverEvent(
                                                    //? if >= 1.21.11 {
                                                    new HoverEvent.ShowText(text)
                                                    //?} else {
                                                    /*new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                    *///?}
                                            )
                                    )
                    );
                }

                callback.accept(message);

            } catch (Exception e) {
                callback.accept(Component.literal("Failed to save screenshot: " + e.getMessage()).withStyle(ChatFormatting.RED));
            } finally {
                nativeImage.close();
            }
        });
        //? if >= 1.21.11 {
        });
        //?}
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