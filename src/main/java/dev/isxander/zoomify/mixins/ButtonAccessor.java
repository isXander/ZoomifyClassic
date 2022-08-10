package dev.isxander.zoomify.mixins;

import com.mojang.minecraft.gui.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Button.class)
public interface ButtonAccessor {
    @Accessor("w")
    int getWidth();

    @Accessor("w")
    void setWidth(int width);

    @Accessor("h")
    int getHeight();

    @Accessor("h")
    void setHeight(int height);
}
