package tomeko.screenshotmessageenhancer.mixins;

//? if >1.8.9 {
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

// Main's static init sets java.awt.headless=true and AWT caches it on first use,
// so it has to be undone before any other mod touches AWT (client entrypoints run too late)
@Mixin(Main.class)
public class MainMixin {
    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void screenshotmessageenhancer$allowAwtClipboard(String[] args, CallbackInfo ci) {
        if (!System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac"))
            System.setProperty("java.awt.headless", "false");
    }
}
//?}
