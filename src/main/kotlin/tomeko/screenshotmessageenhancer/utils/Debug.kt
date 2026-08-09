package tomeko.screenshotmessageenhancer.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig

object Debug {
    private val LOGGER: Logger = LoggerFactory.getLogger(Constants.MOD_ID)

    @JvmStatic
    fun log(message: String) {
        if (!ScreenshotMessageEnhancerConfig.debugModeEnabled) return

        forceLog(message)
    }

    fun forceLog(message: String) {
        LOGGER.info("[${Constants.MOD_NAME}] $message")
    }

    fun forceError(message: String) {
        error("[${Constants.MOD_NAME}] $message")
    }
}