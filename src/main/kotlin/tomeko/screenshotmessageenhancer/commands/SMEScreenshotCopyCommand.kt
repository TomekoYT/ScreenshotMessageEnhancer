package tomeko.screenshotmessageenhancer.commands

//? if forge {
/*import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraftforge.client.ClientCommandHandler
*///?} elif ornithe {
/*import com.mojang.brigadier.arguments.IntegerArgumentType
import org.polyfrost.oneconfig.api.commands.v1.CommandManager.argument
import org.polyfrost.oneconfig.api.commands.v1.CommandManager.literal
import org.polyfrost.oneconfig.internal.legacy.command.ClientCommandRegistrationCallback
*///?} else {
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
//? if >= 26.1 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
//?} else {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
*///?}
//?}
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager
import tomeko.screenshotmessageenhancer.utils.Constants

object SMEScreenshotCopyCommand
//? if forge {
//: CommandBase()
//?}
{
    fun register() {
        //? if forge {
        //ClientCommandHandler.instance.registerCommand(this)
        //?} else {
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
        //?}
    }

    //? if forge {
    /*const val COMMAND_USAGE = "/${Constants.SCREENSHOT_COPY_COMMAND} <pos>"

    override fun getCommandName(): String = Constants.SCREENSHOT_COPY_COMMAND

    override fun getCommandUsage(sender: ICommandSender): String = COMMAND_USAGE

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.size != 1 || args[0].toIntOrNull() == null) throw WrongUsageException(COMMAND_USAGE)

        ScreenshotManager.copyScreenshot(args[0].toInt(), true)
    }

    override fun getRequiredPermissionLevel(): Int = 0
    *///?}
}