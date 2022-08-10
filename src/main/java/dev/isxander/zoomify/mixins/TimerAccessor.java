package dev.isxander.zoomify.mixins;

import com.mojang.minecraft.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Timer.class)
public interface TimerAccessor {
    @Accessor("field_513")
    double getTickDelta();

}
