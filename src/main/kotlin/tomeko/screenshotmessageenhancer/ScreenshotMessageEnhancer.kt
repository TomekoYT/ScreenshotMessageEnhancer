package tomeko.screenshotmessageenhancer

import net.fabricmc.api.ClientModInitializer
import tomeko.screenshotmessageenhancer.commands.*
import tomeko.screenshotmessageenhancer.config.*
import tomeko.screenshotmessageenhancer.utils.*

class ScreenshotMessageEnhancer : ClientModInitializer {
    override fun onInitializeClient() {
        if (!System.getProperty("os.name").lowercase().contains("mac"))
            System.setProperty("java.awt.headless", "false")

        ScreenshotMessageEnhancerCommand.register()
        SMEScreenshotCopyCommand.register()
        SMEScreenshotDeleteCommand.register()
        SMEScreenshotUploadCommand.register()

        ScreenshotMessageEnhancerConfig.register()

        Debug.forceLog("Initialized!")
    }
}