package tomeko.screenshotmessageenhancer.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
//? if >= 26.1 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
//?} else {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
*///?}
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager
import tomeko.screenshotmessageenhancer.utils.Constants

object SMEScreenshotCopyCommand {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal(Constants.SCREENSHOT_COPY_COMMAND)
                    .then(
                        argument("pos", IntegerArgumentType.integer())
                            .executes { context ->
                                val pos = IntegerArgumentType.getInteger(context, "pos")
                                ScreenshotManager.copyScreenshot(pos, true)
                                1
                            }
                    )
            )
        }
    }
}