package tomeko.screenshotmessageenhancer.mixins;

//? if 1.8.9 {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
*///?} else {
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Util;
//?}
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//? if 1.8.9 {
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?} else {
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig;
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotCompressor;
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager;
import tomeko.screenshotmessageenhancer.utils.Constants;
//? if 1.8.9 {
/*import tomeko.screenshotmessageenhancer.utils.ThreadExecutorKt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
*///?}
import java.io.File;
//? if 1.8.9 {
/*import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
*///?}
//? if fabric {
import java.util.function.Consumer;
//?}

@Mixin(
        //? if 1.8.9 {
        //ScreenShotHelper.class
        //?} else {
        Screenshot.class
        //?}
)
public abstract class ScreenshotRecorderMixin {
    //? if 1.8.9 {
    /*private static IntBuffer screenshotmessageenhancer$pixelBuffer;
    private static int[] screenshotmessageenhancer$pixelValues;
    *///?}

    @Inject(
            method =
                    //? if 1.8.9 {
                    //"saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",
                    //?} elif >= 1.21.11 {
                    "grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;ILjava/util/function/Consumer;)V",
            //?} else {
            //"grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;Ljava/util/function/Consumer;)V",
            //?}
            at = @At("HEAD"),
            cancellable = true
    )
    private static void screenshotmessageenhancer$saveScreenshot(
            File workDir,
            String forceName,
            //? if 1.8.9 {
            /*int width,
            int height,
            Framebuffer buffer,
            CallbackInfoReturnable<IChatComponent> cir
            *///?} else {
            RenderTarget target,
            int downscaleFactor,
            Consumer<Component> callback,
            CallbackInfo ci
            //?}
    ) {
        //? if 1.8.9 {
        //cir.setReturnValue(new ChatComponentText("Saving screenshot...").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
        //?} else {
        //? if >= 26.2
        Minecraft.getInstance().gui.hud.getChat().addClientSystemMessage(Component.literal("Saving screenshot...").withStyle((style -> style.withColor(ChatFormatting.GRAY))));
        //? else
        //Minecraft.getInstance().gui.getChat().addClientSystemMessage(Component.literal("Saving screenshot...").withStyle((style -> style.withColor(ChatFormatting.GRAY))));
        ci.cancel();
        //?}

        //? if 1.8.9 {
        /*int screenshotWidth = width;
        int screenshotHeight = height;

        if (OpenGlHelper.isFramebufferEnabled()) {
            screenshotWidth = buffer.framebufferTextureWidth;
            screenshotHeight = buffer.framebufferTextureHeight;
        }

        int pixelCount = screenshotWidth * screenshotHeight;

        if (screenshotmessageenhancer$pixelBuffer == null || screenshotmessageenhancer$pixelBuffer.capacity() < pixelCount) {
            screenshotmessageenhancer$pixelBuffer = BufferUtils.createIntBuffer(pixelCount);
            screenshotmessageenhancer$pixelValues = new int[pixelCount];
        }

        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);

        screenshotmessageenhancer$pixelBuffer.clear();

        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(buffer.framebufferTexture);
            GL11.glGetTexImage(
                    3553,
                    0,
                    32993,
                    33639,
                    screenshotmessageenhancer$pixelBuffer
            );
        } else {
            GL11.glReadPixels(
                    0,
                    0,
                    screenshotWidth,
                    screenshotHeight,
                    32993,
                    33639,
                    screenshotmessageenhancer$pixelBuffer
            );
        }

        screenshotmessageenhancer$pixelBuffer.get(screenshotmessageenhancer$pixelValues);
        TextureUtil.processPixelValues(screenshotmessageenhancer$pixelValues, screenshotWidth, screenshotHeight);

        for (int i = 0; i < pixelCount; i++) {
            screenshotmessageenhancer$pixelValues[i] |= 0xFF000000;
        }

        int[] pixelValuesAsync = new int[pixelCount];

        System.arraycopy(
                screenshotmessageenhancer$pixelValues,
                0,
                pixelValuesAsync,
                0,
                pixelCount
        );

        final int finalWidth = width;
        final int finalHeight = height;
        *///?}

        File screenshotsFolder = new File(workDir, "screenshots");

        if (!screenshotsFolder.exists()) {
            screenshotsFolder.mkdirs();
        }

        File screenshotFile;

        if (forceName == null) {
            screenshotFile = screenshotmessageenhancer$getScreenshotFilename(screenshotsFolder);
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

        //? if 1.8.9 {
        /*ThreadExecutorKt.getThreadExecutor().execute(() -> {
            BufferedImage nativeImage;

            if (OpenGlHelper.isFramebufferEnabled()) {
                nativeImage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, BufferedImage.TYPE_INT_ARGB);
                int heightOffset = buffer.framebufferTextureHeight - buffer.framebufferHeight;

                for (int y = heightOffset; y < buffer.framebufferTextureHeight; ++y) {
                    for (int x = 0; x < buffer.framebufferWidth; ++x) {
                        nativeImage.setRGB(x, y - heightOffset, pixelValuesAsync[y * buffer.framebufferTextureWidth + x]);
                    }
                }
            } else {
                nativeImage = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_ARGB);
                nativeImage.setRGB(0, 0, finalWidth, finalHeight, pixelValuesAsync, 0, finalWidth);
            }
            *///?} else{
        Screenshot.takeScreenshot(target, (nativeImage) -> {
            //?}

            //? if fabric {
            Util.ioPool().execute(() -> {
                //?}
                try {
                    //? if 1.8.9 {
                    //ImageIO.write(nativeImage, "png", finalFile);
                    //?} else {
                    nativeImage.writeToFile(finalFile);
                    //?}

                    ScreenshotManager.INSTANCE.getScreenshotFiles().add(finalFile);
                    int currentIdx = ScreenshotManager.INSTANCE.getScreenshotFiles().size() - 1;

                    if (ScreenshotMessageEnhancerConfig.INSTANCE.getAutoCopyScreenshot()) {
                        ScreenshotManager.INSTANCE.copyScreenshot(currentIdx, false);
                    }

                    //? if 1.8.9 {
                    //ChatComponentText message = new ChatComponentText("Saved screenshot");
                    //?} else {
                    MutableComponent message = Component.literal("Saved screenshot");
                    //?}

                    if (ScreenshotMessageEnhancerConfig.INSTANCE.getShowName()) {
                        //? if 1.8.9 {
                    /*message.appendText(" as ");
                    message.appendSibling(new ChatComponentText(finalFile.getName()).setChatStyle(new ChatStyle().setUnderlined(true)));
                    *///?} else {
                        message.append(Component.literal(" as "));
                        message.append(Component.literal(finalFile.getName()).withStyle(ChatFormatting.UNDERLINE));
                        //?}
                    }

                    if (ScreenshotMessageEnhancerConfig.INSTANCE.getShowCopyButton()) {
                        String command =
                                //? if <= 1.21.1 {
                                //"/" +
                                        //?}
                                Constants.SCREENSHOT_COPY_COMMAND + " " + currentIdx;

                        //? if 1.8.9 {
                        //ChatComponentText text = new ChatComponentText("Copy the screenshot");
                        //?} else {
                        Component text = Component.literal("Copy the screenshot");
                        //?}

                        //? if 1.8.9 {
                        //message.appendText(" ");
                        //?} else {
                        message.append(" ");
                        //?}
                        //? if 1.8.9 {
                    /*message.appendSibling(
                            new ChatComponentText("[COPY]").setChatStyle(
                                    new ChatStyle()
                                            .setColor(EnumChatFormatting.BLUE)
                                            .setBold(true)
                                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                                            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text))
                            ));
                    *///?} else {
                        message.append(
                                Component.literal("[COPY]")
                                        .withStyle(ChatFormatting.BOLD, ChatFormatting.BLUE)
                                        .withStyle(style -> style
                                                .withClickEvent(
                                                        //? if >= 1.21.11 {
                                                        new ClickEvent.RunCommand(command)
                                                        //?} else {
                                                        //new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                                                        //?}
                                                )
                                                .withHoverEvent(
                                                        //? if >= 1.21.11 {
                                                        new HoverEvent.ShowText(text)
                                                        //?} else {
                                                        //new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                        //?}
                                                )
                                        )
                        );
                        //?}
                    }

                    if (ScreenshotMessageEnhancerConfig.INSTANCE.getShowOpenButton()) {
                        //? if 1.8.9 {
                        //String command = "/" + Constants.SCREENSHOT_OPEN_COMMAND + " " + currentIdx;
                        //?} else {
                        String path = finalFile.getAbsolutePath();
                        //?}

                        //? if 1.8.9 {
                        //ChatComponentText text = new ChatComponentText("Open " + finalFile.getName());
                        //?} else {
                        Component text = Component.literal("Open " + finalFile.getName());
                        //?}

                        //? if 1.8.9 {
                        //message.appendText(" ");
                        //?} else {
                        message.append(" ");
                        //?}

                        //? if 1.8.9 {
                    /*message.appendSibling(
                            new ChatComponentText("[OPEN]").setChatStyle(
                                    new ChatStyle()
                                            .setColor(EnumChatFormatting.GREEN)
                                            .setBold(true)
                                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                                            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text))
                            )
                    );
                    *///?} else {
                        message.append(
                                Component.literal("[OPEN]")
                                        .withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN)
                                        .withStyle(style -> style
                                                .withClickEvent(
                                                        //? if >= 1.21.11 {
                                                        new ClickEvent.OpenFile(path)
                                                        //?} else {
                                                        //new ClickEvent(ClickEvent.Action.OPEN_FILE, path)
                                                        //?}
                                                )
                                                .withHoverEvent(
                                                        //? if >= 1.21.11 {
                                                        new HoverEvent.ShowText(text)
                                                        //?} else {
                                                        //new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                        //?}
                                                )
                                        )
                        );
                        //?}
                    }

                    if (ScreenshotMessageEnhancerConfig.INSTANCE.getShowOpenFolderButton()) {
                        //? if 1.8.9 {
                        //String command = "/" + Constants.SCREENSHOT_OPEN_FOLDER_COMMAND;
                        //?} else {
                        String path = finalFolder.getAbsolutePath();
                        //?}

                        //? if 1.8.9 {
                        //ChatComponentText text = new ChatComponentText(finalFolder.getPath());
                        //?} else {
                        Component text = Component.literal(finalFolder.getPath());
                        //?}

                        //? if 1.8.9 {
                        //message.appendText(" ");
                        //?} else {
                        message.append(" ");
                        //?}

                        //? if 1.8.9 {
                    /*message.appendSibling(
                            new ChatComponentText("[OPEN FOLDER]").setChatStyle(
                                    new ChatStyle()
                                            .setColor(EnumChatFormatting.GOLD)
                                            .setBold(true)
                                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                                            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text))
                            )
                    );
                    *///?} else {
                        message.append(
                                Component.literal("[OPEN FOLDER]")
                                        .withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD)
                                        .withStyle(style -> style
                                                .withClickEvent(
                                                        //? if >= 1.21.11 {
                                                        new ClickEvent.OpenFile(path)
                                                        //?} else {
                                                        //new ClickEvent(ClickEvent.Action.OPEN_FILE, path)
                                                        //?}
                                                )
                                                .withHoverEvent(
                                                        //? if >= 1.21.11 {
                                                        new HoverEvent.ShowText(text)
                                                        //?} else {
                                                        //new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                        //?}
                                                )
                                        )
                        );
                        //?}
                    }

                    if (ScreenshotMessageEnhancerConfig.INSTANCE.getShowDeleteButton()) {
                        String command =
                                //? if <= 1.21.1 {
                                //"/" +
                                        //?}
                                Constants.SCREENSHOT_DELETE_COMMAND + " " + currentIdx;

                        //? if 1.8.9 {
                        //ChatComponentText text = new ChatComponentText("Delete the screenshot");
                        //?} else {
                        Component text = Component.literal("Delete the screenshot");
                        //?}

                        //? if 1.8.9 {
                        //message.appendText(" ");
                        //?} else {
                        message.append(" ");
                        //?}
                        //? if 1.8.9 {
                    /*message.appendSibling(
                            new ChatComponentText("[DELETE]").setChatStyle(
                                    new ChatStyle()
                                            .setColor(EnumChatFormatting.RED)
                                            .setBold(true)
                                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                                            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text))
                            ));
                    *///?} else {
                        message.append(
                                Component.literal("[DELETE]")
                                        .withStyle(ChatFormatting.BOLD, ChatFormatting.RED)
                                        .withStyle(style -> style
                                                .withClickEvent(
                                                        //? if >= 1.21.11 {
                                                        new ClickEvent.RunCommand(command)
                                                        //?} else {
                                                        //new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                                                        //?}
                                                )
                                                .withHoverEvent(
                                                        //? if >= 1.21.11 {
                                                        new HoverEvent.ShowText(text)
                                                        //?} else {
                                                        //new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                        //?}
                                                )
                                        )
                        );
                        //?}
                    }

                    if (ScreenshotMessageEnhancerConfig.INSTANCE.getShowUploadButton()) {
                        String command =
                                //? if <= 1.21.1 {
                                //"/" +
                                        //?}
                                Constants.SCREENSHOT_UPLOAD_COMMAND + " " + currentIdx;

                        //? if 1.8.9 {
                        //ChatComponentText text = new ChatComponentText("Upload the screenshot");
                        //?} else {
                        Component text = Component.literal("Upload the screenshot");
                        //?}

                        //? if 1.8.9 {
                        //message.appendText(" ");
                        //?} else {
                        message.append(" ");
                        //?}
                        //? if 1.8.9 {
                    /*message.appendSibling(
                            new ChatComponentText("[UPLOAD]").setChatStyle(
                                    new ChatStyle()
                                            .setColor(EnumChatFormatting.YELLOW)
                                            .setBold(true)
                                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                                            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text))
                            ));
                    *///?} else {
                        message.append(
                                Component.literal("[UPLOAD]")
                                        .withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)
                                        .withStyle(style -> style
                                                .withClickEvent(
                                                        //? if >= 1.21.11 {
                                                        new ClickEvent.RunCommand(command)
                                                        //?} else {
                                                        //new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                                                        //?}
                                                )
                                                .withHoverEvent(
                                                        //? if >= 1.21.11 {
                                                        new HoverEvent.ShowText(text)
                                                        //?} else {
                                                        //new HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
                                                        //?}
                                                )
                                        )
                        );
                        //?}
                    }

                    //? if 1.8.9 {
                /*Minecraft.getMinecraft().addScheduledTask(() -> {
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
                });
                *///?} else {
                    callback.accept(message);
                    //?}

                    if (ScreenshotMessageEnhancerConfig.INSTANCE.getCompressScreenshots()) {
                        ScreenshotCompressor.INSTANCE.compress(finalFile, currentIdx);
                    }

                } catch (Exception e) {
                    //? if 1.8.9 {
                /*final String errorMsg = e.getMessage();
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
                            new ChatComponentText("Failed to save screenshot: " + errorMsg)
                                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))
                    );
                });
                *///?} else {
                    callback.accept(Component.literal("Failed to save screenshot: " + e.getMessage()).withStyle(ChatFormatting.RED));
                    //?}
                }
                //? if fabric {
                finally {
                    nativeImage.close();
                }
                //?}
            });
            //? if fabric {
        });
        //?}
    }

    private static File screenshotmessageenhancer$getScreenshotFilename(File directory) {
        String time =
                //? if 1.8.9 {
                //new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
                //?} else {
                Util.getFilenameFormattedDateTime();
        //?}
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