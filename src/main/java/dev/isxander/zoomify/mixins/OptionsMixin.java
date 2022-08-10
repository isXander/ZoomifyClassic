package dev.isxander.zoomify.mixins;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.Options;
import com.mojang.minecraft.input.Keybind;
import dev.isxander.zoomify.api.KeybindingAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Options.class)
public class OptionsMixin {
    @Shadow public Keybind[] keybinds;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void modifyKeybinds(Minecraft client, File directory, CallbackInfo ci) {
        keybinds = KeybindingAPI.INSTANCE.addCustomKeybinds(keybinds);
    }


}
