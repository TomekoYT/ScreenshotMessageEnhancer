package tomeko.screenshotmessageenhancer.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.CommandBuildContext
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
//? if = 1.21.11 {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
//?}
import net.minecraft.client.Minecraft
import tomeko.screenshotmessageenhancer.config.ScreenshotMessageEnhancerConfig
import tomeko.screenshotmessageenhancer.utils.Constants

object ScreenshotMessageEnhancerCommand {
    private var shouldOpenConfig: Boolean = false

    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher: CommandDispatcher<FabricClientCommandSource>, _: CommandBuildContext ->
            dispatcher.register(
                literal(Constants.MOD_ID)
                    .executes { _: CommandContext<FabricClientCommandSource> ->
                        shouldOpenConfig = true
                        1
                    }
            )
        }

        ClientTickEvents.END_CLIENT_TICK.register { _: Minecraft ->
            if (!shouldOpenConfig) return@register

            ScreenshotMessageEnhancerConfig.openUI()

            shouldOpenConfig = false
        }
    }
}