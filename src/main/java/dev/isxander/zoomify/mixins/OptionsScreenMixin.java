package dev.isxander.zoomify.mixins;

import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.OptionsScreen;
import com.mojang.minecraft.gui.Screen;
import dev.isxander.zoomify.config.ZoomifySettings;
import dev.isxander.zoomify.gui.ZoomifyCategoryConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Inject(method = "init", at = @At("RETURN"))
    private void onScreenInit(CallbackInfo ci) {
        this.buttons.add(new Button(1000, this.width / 2 - 100, this.height / 6 + 120 + 12 - 22, "Zoomify..."));
    }

    @Inject(method = "buttonClicked", at = @At("RETURN"))
    private void onButtonClicked(Button button, CallbackInfo ci) {
        if (button.enabled && button.id == 1000) {
            this.minecraft.openScreen(new ZoomifyCategoryConfig(ZoomifySettings.BEHAVIOUR, minecraft.screen));
        }
    }
}
