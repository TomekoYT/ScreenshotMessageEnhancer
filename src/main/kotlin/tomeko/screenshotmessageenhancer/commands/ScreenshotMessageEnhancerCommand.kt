package tomeko.screenshotmessageenhancer.commands

//? if forge {
/*import cc.polyfrost.oneconfig.utils.commands.CommandManager
import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
*///?} else {
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
//? if >= 26.1 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
//?} else {
//import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
//?}
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import net.minecraft.commands.CommandBuildContext
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
//?}
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig
import tomeko.screenshotmessageenhancer.utils.Constants

//? if forge {
//@Command(value = Constants.MOD_ID)
//?}
object ScreenshotMessageEnhancerCommand {
    //? if !forge {
    private var shouldOpenConfig: Boolean = false
    //?}

    fun register() {
        //? if forge {
        //CommandManager.INSTANCE.registerCommand(this)
         //?} else {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher: CommandDispatcher<FabricClientCommandSource>, _: CommandBuildContext ->
            dispatcher.register(
                literal(Constants.MOD_ID)
                    .executes { _: CommandContext<FabricClientCommandSource> ->
                        shouldOpenConfig = true
                        return@executes 1
                    }
            )
        }

        ClientTickEvents.END_CLIENT_TICK.register { _: Minecraft ->
            if (!shouldOpenConfig) return@register

            ScreenshotMessageEnhancerConfig.openUI()

            shouldOpenConfig = false
        }
        //?}
    }

    //? if forge {
    /*@Main
    fun handle() {
        ScreenshotMessageEnhancerConfig.openGui()
    }
    *///?}
}