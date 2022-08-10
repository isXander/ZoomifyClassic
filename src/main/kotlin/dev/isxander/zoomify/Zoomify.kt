package dev.isxander.zoomify

import com.mojang.minecraft.Minecraft
import com.mojang.minecraft.input.Keybind
import dev.isxander.zoomify.api.KeybindingAPI
import dev.isxander.zoomify.config.ZoomKeyBehaviour
import dev.isxander.zoomify.config.ZoomifySettings
import net.fabricmc.api.ClientModInitializer
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.util.logging.LogManager

object Zoomify : ClientModInitializer {
    val LOGGER = LogManager.getLogManager().getLogger("Zoomify")

    val zoomKeybind = KeybindingAPI.register("Zoomify", 0x2E)

    var zooming = false
    private val zoomHelper = ZoomHelper()

    var previousZoomDivisor = 1.0
        private set

    const val maxScrollTiers = 30
    private var scrollSteps = 0

    override fun onInitializeClient() {
        ZoomifySettings
    }

    fun getFovDivisor(tickDelta: Float): Double {
        if (!zooming) {
            scrollSteps = 0
            zoomHelper.reset()
        }

        return zoomHelper.getZoomDivisor(tickDelta).also { previousZoomDivisor = it }
    }

    fun onTick(client: Minecraft) {
        zoomHelper.tick(zooming, scrollSteps, 0.05)
    }

    fun onKeyEvent(client: Minecraft) {
        when (ZoomifySettings.zoomKeyBehaviour) {
            ZoomKeyBehaviour.HOLD -> {
                if (Keyboard.getEventKey() == zoomKeybind.keybind.key) {
                    zooming = Keyboard.getEventKeyState()
                }
            }
            ZoomKeyBehaviour.TOGGLE -> {
                if (Keyboard.getEventKey() == zoomKeybind.keybind.key && Keyboard.getEventKeyState()) {
                    zooming = !zooming
                }
            }
        }
    }

    fun onScrollSteps(client: Minecraft) {
        val mouseDelta = Mouse.getEventDWheel()
        scrollSteps += mouseDelta.coerceIn(-1..1)
        scrollSteps = scrollSteps.coerceIn(0..maxScrollTiers)
    }
}