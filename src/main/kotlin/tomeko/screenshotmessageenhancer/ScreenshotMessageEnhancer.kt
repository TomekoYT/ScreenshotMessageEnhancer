package tomeko.screenshotmessageenhancer

//? if forge {
/*import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.ShutdownEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
*///?} elif ornithe {
//import net.ornithemc.osl.entrypoints.api.ModInitializer
//?} else {
import net.fabricmc.api.ClientModInitializer
//?}
import tomeko.screenshotmessageenhancer.commands.*
import tomeko.screenshotmessageenhancer.config.*
import tomeko.screenshotmessageenhancer.utils.*

//? if forge {
/*@Mod(
    name = Constants.MOD_NAME,
    modid = Constants.MOD_ID,
    version = Constants.MOD_VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
*///?}
class ScreenshotMessageEnhancer
//? if ornithe {
//: ModInitializer
//?} elif fabric {
    : ClientModInitializer
//?}
{
    //? if forge {
    //@Mod.EventHandler
    //?} else {
    override
    //?}
    fun
    //? if fabric {
            onInitializeClient(
        //?} else {
        //init(
        //?}
        //? if forge {
        //event: FMLInitializationEvent
        //?}
    ) {
        //? if forge {
        //EventManager.INSTANCE.register(this)
        //?}

        if (!System.getProperty("os.name").lowercase().contains("mac"))
            System.setProperty("java.awt.headless", "false")

        ScreenshotMessageEnhancerCommand.register()
        SMEScreenshotCopyCommand.register()
        SMEScreenshotDeleteCommand.register()
        //? if 1.8.9 {
        /*SMEScreenshotOpenCommand.register()
        SMEScreenshotOpenFolderCommand.register()
        *///?}
        SMEScreenshotUploadCommand.register()

        ScreenshotMessageEnhancerConfig.register()

        Debug.forceLog("${Constants.MOD_VERSION} Initialized!")
    }
}