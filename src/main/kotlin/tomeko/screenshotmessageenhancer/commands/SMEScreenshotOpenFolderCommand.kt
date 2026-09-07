package tomeko.screenshotmessageenhancer.commands

//? if 1.8.9 {
/*import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraftforge.client.ClientCommandHandler
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager
import tomeko.screenshotmessageenhancer.utils.Constants

object SMEScreenshotOpenFolderCommand : CommandBase() {
    fun register() {
        ClientCommandHandler.instance.registerCommand(this)
    }

    const val COMMAND_USAGE = "/${Constants.SCREENSHOT_OPEN_FOLDER_COMMAND}"

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
}
*///?}