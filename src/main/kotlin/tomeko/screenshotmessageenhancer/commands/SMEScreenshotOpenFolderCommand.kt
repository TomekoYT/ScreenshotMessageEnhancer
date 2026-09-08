package tomeko.screenshotmessageenhancer.commands

//? if forge {
/*import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraftforge.client.ClientCommandHandler
*///?} elif ornithe {
/*import org.polyfrost.oneconfig.api.commands.v1.CommandManager.literal
import org.polyfrost.oneconfig.internal.legacy.command.ClientCommandRegistrationCallback
*///?}
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager
import tomeko.screenshotmessageenhancer.utils.Constants

object SMEScreenshotOpenFolderCommand
//? if forge {
//: CommandBase()
//?}
{
    fun register() {
        //? if forge {
        //ClientCommandHandler.instance.registerCommand(this)
        //?} elif ornithe {
        /*ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal(Constants.SCREENSHOT_OPEN_FOLDER_COMMAND)
                    .executes {
                        ScreenshotManager.openScreenshotFolder()
                        1
                    }
            )
        }
        *///?}
    }

    //? if forge {
    /*const val COMMAND_USAGE = "/${Constants.SCREENSHOT_OPEN_FOLDER_COMMAND}"

    override fun getCommandName(): String =
        Constants.SCREENSHOT_OPEN_FOLDER_COMMAND

    override fun getCommandUsage(sender: ICommandSender): String =
        COMMAND_USAGE

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isNotEmpty()) {
            throw WrongUsageException(COMMAND_USAGE)
        }

        ScreenshotManager.openScreenshotFolder()
    }

    override fun getRequiredPermissionLevel(): Int = 0
    *///?}
}