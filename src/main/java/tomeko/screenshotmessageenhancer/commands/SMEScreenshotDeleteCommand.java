package tomeko.screenshotmessageenhancer.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import tomeko.screenshotmessageenhancer.screenshots.ScreenshotManager;
import tomeko.screenshotmessageenhancer.utils.Constants;

//? if >= 26.1 {

/*import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
 
*///?} else {
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//?}

public class SMEScreenshotDeleteCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(literal(Constants.SCREENSHOT_DELETE_COMMAND)
                        .then(argument("pos", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    int pos = IntegerArgumentType.getInteger(ctx, "pos");
                                    ScreenshotManager.deleteScreenshot(pos);
                                    return 1;
                                })
                        )
                )
        );
    }
}
