package tomeko.screenshotmessageenhancer.commands

//? if forge {
/*import cc.polyfrost.oneconfig.utils.commands.CommandManager
import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
*///?} elif ornithe {
/*import com.mojang.brigadier.arguments.IntegerArgumentType
import org.polyfrost.oneconfig.api.commands.v1.CommandManager.argument
import org.polyfrost.oneconfig.api.commands.v1.CommandManager.literal
import org.polyfrost.oneconfig.api.event.v1.EventManager
import org.polyfrost.oneconfig.api.event.v1.events.TickEvent
import org.polyfrost.oneconfig.internal.legacy.command.ClientCommandRegistrationCallback
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
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
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal(Constants.MOD_ID)
                    .executes {
                        shouldOpenConfig = true
                        1
                    }
            )
        }
        //?}


        //? if !forge {
        //? if ornithe {
        //EventManager.register(TickEvent.Start::class) {
            //?} elif fabric {
        ClientTickEvents.END_CLIENT_TICK.register { _: Minecraft ->
            //?}
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