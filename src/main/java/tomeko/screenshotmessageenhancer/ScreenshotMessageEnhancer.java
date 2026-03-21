package tomeko.screenshotmessageenhancer;

import net.fabricmc.api.ClientModInitializer;
import tomeko.screenshotmessageenhancer.commands.*;
import tomeko.screenshotmessageenhancer.config.*;

public class ScreenshotMessageEnhancer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SMEScreenshotCopyCommand.register();
		SMEScreenshotDeleteCommand.register();

		ScreenshotMessageEnhancerConfig.register();
	}
}