package tomeko.screenshotmessageenhancer.config

import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.Info
import org.polyfrost.oneconfig.api.config.v1.annotations.MultiSelectDropdown
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import tomeko.screenshotmessageenhancer.utils.Buttons
import tomeko.screenshotmessageenhancer.utils.Constants
import tomeko.screenshotmessageenhancer.utils.Debug

object ScreenshotMessageEnhancerConfig : Config(
    "${Constants.MOD_ID}.json",
    "/assets/${Constants.MOD_ID}/icon.png",
    Constants.MOD_NAME,
    Category.QOL
) {
    fun register() {
        if (!this::class.java.getDeclaredField("buttons")
                .getAnnotation(MultiSelectDropdown::class.java).options
                .contentEquals(Buttons.entries.map { it.buttonName }.toTypedArray())
        ) Debug.forceError("buttons missing options")

        preload()

        if (buttons.size != Buttons.entries.size)
            buttons = BooleanArray(Buttons.entries.size) { true }
    }


    private const val CATEGORY_GENERAL = "General"

    @Switch(
        title = "Show Name",
        description = "Show screenshot name in the message",
        category = CATEGORY_GENERAL
    )
    var showName = false

    @MultiSelectDropdown(
        title = "Buttons",
        description = "Buttons for Screenshot Message Enhancer",
        checkable = true,
        options = [
            "Copy",
            "Open",
            "Open Folder",
            "Delete",
            "Upload"
        ],
        category = CATEGORY_GENERAL
    )
    var buttons: BooleanArray = BooleanArray(Buttons.entries.size) { true }

    @Switch(
        title = "Automatically Copy Screenshot",
        category = CATEGORY_GENERAL
    )
    var autoCopyScreenshot = false


    private const val CATEGORY_DEBUG = "Debug"

    @Info(
        title = "Probably should stay disabled",
        category = CATEGORY_DEBUG
    )
    var debugModeInfo: Nothing? = null

    @Switch(
        title = "Debug Mode",
        category = CATEGORY_DEBUG
    )
    var debugModeEnabled: Boolean = false
}