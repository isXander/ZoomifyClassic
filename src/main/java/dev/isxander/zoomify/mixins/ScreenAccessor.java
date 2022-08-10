package dev.isxander.zoomify.mixins;

import com.mojang.minecraft.gui.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor
    void setButtons(List<?> buttons);
}
