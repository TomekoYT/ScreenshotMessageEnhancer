package tomeko.screenshotmessageenhancer.utils

//? if fabric {
import org.slf4j.Logger
import org.slf4j.LoggerFactory
//?}
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig

object Debug {
    //? if fabric {
    private val LOGGER: Logger = LoggerFactory.getLogger(Constants.MOD_ID)
    //?}

    fun log(message: String) {
        if (!ScreenshotMessageEnhancerConfig.debugModeEnabled) return

        forceLog(message)
    }

    fun forceLog(message: String) {
        //? if 1.8.9 {
        //println("[${Constants.MOD_NAME}] $message")
        //?} else {
        LOGGER.info("[${Constants.MOD_NAME}] $message")
        //?}
    }
}