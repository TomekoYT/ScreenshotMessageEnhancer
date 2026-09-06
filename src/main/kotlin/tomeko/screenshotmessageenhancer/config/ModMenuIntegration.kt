package tomeko.screenshotmessageenhancer.config

//? if >= 1.21.1 {
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.minecraft.client.gui.screens.Screen
import org.polyfrost.oneconfig.utils.v1.dsl.createScreen

class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return { _: Screen -> ScreenshotMessageEnhancerConfig.createScreen() }
    }
}
//?}