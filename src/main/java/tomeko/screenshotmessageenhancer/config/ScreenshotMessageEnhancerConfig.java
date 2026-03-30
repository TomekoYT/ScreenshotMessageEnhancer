package tomeko.screenshotmessageenhancer.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import tomeko.screenshotmessageenhancer.utils.Constants;

public class ScreenshotMessageEnhancerConfig {
    public static final ConfigClassHandler<ScreenshotMessageEnhancerConfig> CONFIG = ConfigClassHandler.createBuilder(ScreenshotMessageEnhancerConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve(Constants.MOD_ID + ".json"))
                    .build())
            .build();

    @SerialEntry
    public static boolean modifyScreenshotMessageEnabled = true;
    @SerialEntry
    public static boolean modifyScreenshotMessageAddName = false;
    @SerialEntry
    public static boolean modifyScreenshotMessageAddCopy = true;
    @SerialEntry
    public static boolean modifyScreenshotMessageAddOpen = true;
    @SerialEntry
    public static boolean modifyScreenshotMessageAddOpenFolder = true;
    @SerialEntry
    public static boolean modifyScreenshotMessageAddDelete = true;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Component.literal(Constants.MOD_NAME))

                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Screenshot Message Enhancer Config"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Enabled"))
                                .binding(defaults.modifyScreenshotMessageEnabled, () -> config.modifyScreenshotMessageEnabled, newVal -> config.modifyScreenshotMessageEnabled = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Add Screenshot Name"))
                                .binding(defaults.modifyScreenshotMessageAddName, () -> config.modifyScreenshotMessageAddName, newVal -> config.modifyScreenshotMessageAddName = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Add [COPY] Button"))
                                .binding(defaults.modifyScreenshotMessageAddCopy, () -> config.modifyScreenshotMessageAddCopy, newVal -> config.modifyScreenshotMessageAddCopy = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Add [OPEN] Button"))
                                .binding(defaults.modifyScreenshotMessageAddOpen, () -> config.modifyScreenshotMessageAddOpen, newVal -> config.modifyScreenshotMessageAddOpen = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Add [OPEN FOLDER] Button"))
                                .binding(defaults.modifyScreenshotMessageAddOpenFolder, () -> config.modifyScreenshotMessageAddOpenFolder, newVal -> config.modifyScreenshotMessageAddOpenFolder = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Add [DELETE] Button"))
                                .binding(defaults.modifyScreenshotMessageAddDelete, () -> config.modifyScreenshotMessageAddDelete, newVal -> config.modifyScreenshotMessageAddDelete = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())

        )).generateScreen(parent);
    }

    public static void register() {
        ScreenshotMessageEnhancerConfig.CONFIG.load();
    }
}
