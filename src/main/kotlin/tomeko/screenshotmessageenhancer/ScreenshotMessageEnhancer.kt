package tomeko.screenshotmessageenhancer

//? if forge {
/*import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.ShutdownEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
*///?} else {
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
//? if fabric || ornithe {
: ClientModInitializer
//?}
{
    //? if forge {
    //@Mod.EventHandler
    //?} else {
    override
    //?}
    fun onInitializeClient(
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
        SMEScreenshotUploadCommand.register()

        ScreenshotMessageEnhancerConfig.register()

        Debug.forceLog("Initialized!")
    }
}