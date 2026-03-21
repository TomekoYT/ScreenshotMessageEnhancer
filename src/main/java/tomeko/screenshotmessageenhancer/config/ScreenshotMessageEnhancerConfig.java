package tomeko.screenshotmessageenhancer.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import tomeko.screenshotmessageenhancer.utils.Constants;

public class ScreenshotMessageEnhancerConfig {
    public static final ConfigClassHandler<ScreenshotMessageEnhancerConfig> CONFIG = ConfigClassHandler.createBuilder(ScreenshotMessageEnhancerConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve(Constants.MOD_ID + ".json"))
                    .build())
            .build();

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal(Constants.MOD_NAME))
        )).generateScreen(parent);
    }

    public static void register() {
        ScreenshotMessageEnhancerConfig.CONFIG.load();
    }
}
