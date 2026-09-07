package tomeko.screenshotmessageenhancer.config

//? if forge {
/*import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
*///?} else {
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.Info
import org.polyfrost.oneconfig.api.config.v1.annotations.MultiSelectDropdown
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
//?}
import tomeko.screenshotmessageenhancer.utils.Constants

object ScreenshotMessageEnhancerConfig : Config(
    //? if forge {
    /*Mod(
        Constants.MOD_NAME,
        ModType.UTIL_QOL,
        Constants.MOD_ICON
    ),
    "${Constants.MOD_ID}.json"
    *///?} else {
    "${Constants.MOD_ID}.json",
    Constants.MOD_ICON,
    Constants.MOD_NAME,
    Category.QOL
    //?}
) {
    fun register() {
        //? if forge {
        //initialize()
        //?} else {
        preload()
        //?}
    }

    //? if forge {
    //@Exclude
    //?}
    private const val CATEGORY_GENERAL = "General"

    //? if forge {
    //@Exclude
    //?}
    private const val SUBCATEGORY_NAME = "Name"

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Show Name",
        description = "Show screenshot name in the screenshot message",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_NAME
    )
    var showName = false

    //? if forge {
    //@Exclude
    //?}
    private const val SUBCATEGORY_BUTTONS = "Buttons"

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Show Copy Button",
        description = "Show copy button in the screenshot message",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_BUTTONS
    )
    var showCopyButton = true

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Show Open Button",
        description = "Show open button in the screenshot message",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_BUTTONS
    )
    var showOpenButton = true

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Show Open Folder Button",
        description = "Show open folder button in the screenshot message",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_BUTTONS
    )
    var showOpenFolderButton = true

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Show Delete Button",
        description = "Show delete button in the screenshot message",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_BUTTONS
    )
    var showDeleteButton = true

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Show Upload Button",
        description = "Show upload button in the screenshot message",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_BUTTONS
    )
    var showUploadButton = true

    //? if forge {
    //@Exclude
    //?}
    private const val SUBCATEGORY_MISC = "Misc"

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Automatically Copy Screenshot",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_MISC
    )
    var autoCopyScreenshot = false

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Compress Screenshots",
        description = "Compress Screenshots without any quality loss",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_MISC
    )
    var compressScreenshots = true


    //? if forge {
    //@Exclude
    //?}
    private const val CATEGORY_DEBUG = "Debug"

    @Info(
        //? if forge {
        //text =
            //?} else {
            title =
            //?}
            "Probably should stay disabled",
        //? if forge {
        //type = InfoType.INFO,
        //?}
        category = CATEGORY_DEBUG
    )
    var debugModeInfo: Nothing? = null

    @Switch(
        //? if forge {
        //name =
            //?} else {
            title =
            //?}
            "Debug Mode",
        category = CATEGORY_DEBUG
    )
    var debugModeEnabled: Boolean = false
}