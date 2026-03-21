package tomeko.screenshotmessageenhancer.mixin;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(Main.class)
public class MainMixin {
    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void Main(CallbackInfo ci) {
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac")) return;

        System.setProperty("java.awt.headless", "false");
    }
}
