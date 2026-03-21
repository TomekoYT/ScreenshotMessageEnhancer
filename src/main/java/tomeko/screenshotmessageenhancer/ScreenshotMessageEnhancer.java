package tomeko.screenshotmessageenhancer;

import net.fabricmc.api.ClientModInitializer;
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig;

public class ScreenshotMessageEnhancer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScreenshotMessageEnhancerConfig.register();
	}
}