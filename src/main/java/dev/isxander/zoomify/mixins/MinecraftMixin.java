package dev.isxander.zoomify.mixins;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.Timer;
import com.mojang.minecraft.gui.InGameHud;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.player.Inventory;
import dev.isxander.zoomify.Zoomify;
import dev.isxander.zoomify.config.ZoomifySettings;
import dev.isxander.zoomify.utils.MathHelper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow private Timer timer;

    @Shadow public int width;

    @Shadow public int height;

    @Shadow public InGameHud hud;

    @Shadow public Screen screen;

    @ModifyConstant(method = "run", constant = @Constant(floatValue = 70f, ordinal = 2))
    private float getFov(float prevFov) {
        return (float) (prevFov / Zoomify.INSTANCE.getFovDivisor(timer.a));
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/player/Player;method_164(IZ)V"))
    private void onKey(CallbackInfo ci) {
        Zoomify.INSTANCE.onKeyEvent((Minecraft) (Object) this);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        Zoomify.INSTANCE.onTick((Minecraft) (Object) this);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/minecraft/player/Inventory;swapPaint(I)V"
        )
    )
    private void onMouse(Inventory inventory, int i) {
        if (ZoomifySettings.INSTANCE.getScrollZoom() && Zoomify.INSTANCE.getZooming()) {
            Zoomify.INSTANCE.onScrollSteps((Minecraft) (Object) this);
        } else {
            inventory.swapPaint(i);
        }
    }

    @ModifyArgs(method = "run", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/player/Player;turn(FF)V"))
    private void modifySensitivity(Args args) {
        for (int param = 0; param < 2; param++) {
            args.set(param, (float)(((float)args.get(param)) / MathHelper.lerp(ZoomifySettings.INSTANCE.getRelativeSensitivity() / 100.0, 1.0, Zoomify.INSTANCE.getPreviousZoomDivisor())));
        }
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/Minecraft;checkGlError(Ljava/lang/String;)V", ordinal = 4))
    private void onPreRender(CallbackInfo ci) {
        if (Display.wasResized()) {
            this.width = Display.getWidth();
            this.height = Display.getHeight();
            GL11.glViewport(0, 0, width, height);

            this.hud = new InGameHud((Minecraft) (Object) this, width, height);

            ((ScreenAccessor) this.screen).setButtons(new ArrayList<>());
            this.screen.init((Minecraft) (Object) this, this.width * 240 / this.height, this.height * 240 / this.height);
        }
    }
}
