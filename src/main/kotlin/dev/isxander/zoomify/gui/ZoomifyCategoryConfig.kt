package dev.isxander.zoomify.gui

import com.mojang.minecraft.gui.Button
import com.mojang.minecraft.gui.NarrowButton
import com.mojang.minecraft.gui.Screen
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import dev.isxander.zoomify.config.ZoomifySettings
import dev.isxander.zoomify.mixins.ButtonAccessor
import org.lwjgl.input.Keyboard
import java.text.DecimalFormat

class ZoomifyCategoryConfig(val category: String, val parent: Screen? = null) : Screen() {
    private val categories = listOf(ZoomifySettings.BEHAVIOUR, ZoomifySettings.CONTROLS, ZoomifySettings.SCROLLING)
    private val settings = ZoomifySettings.settings.filter { it.category == category }

    override fun init() {
        var startCategoryX = 0
        for ((i, category) in categories.withIndex()) {
            val width = font.width(category) + 10
            val button = Button(i, this.width / 16 + startCategoryX, this.height / 16, category)
            (button as ButtonAccessor).width = width
            if (category == this.category)
                button.enabled = false
            startCategoryX += width + 4

            this.buttons.add(button)
        }

        for ((i, setting) in settings.withIndex()) {
            val x = this.width / 2 - 205 + i % 2 * 210
            val y = height / 6 + 24 * (i shr 1)
            val name = getNameForSetting(setting)

            this.buttons.add(Button(100 + i, x, y, name))
        }

        this.buttons.add(NarrowButton(200, this.width / 2 - 155, this.height / 6 + 168, "Back..."))
        this.buttons.add(NarrowButton(201, this.width / 2 + 5, this.height / 6 + 168, "Done"))
    }

    override fun buttonClicked(button: Button) {
        when {
            button.id < categories.size ->
                this.minecraft.openScreen(ZoomifyCategoryConfig(button.msg))
            button.id in 100 until 200 ->
                cycleSetting(button, settings[button.id - 100])
            button.id == 200 ->
                this.minecraft.openScreen(parent)
            button.id == 201 ->
                this.minecraft.openScreen(null)
        }
    }

    override fun render(mouseX: Int, mouseY: Int) {
        fillGradient(0, 0, width, height, 1610941696, -1607454624)
        super.render(mouseX, mouseY)
    }

    fun cycleSetting(button: Button, setting: Setting<*>) {
        when (setting) {
            is DoubleSetting -> {
                var inc = 0.5
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                    inc = -inc

                var target = setting.get() + inc
                val start = setting.range?.start ?: Double.MIN_VALUE
                val end = setting.range?.endInclusive ?: Double.MAX_VALUE
                if (target > end) {
                    target = start + (target - end)
                } else if (target < start) {
                    target = end - (start - target)
                }

                setting.set(target)
            }
            is FloatSetting -> {
                var inc = 0.5f
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                    inc = -inc

                var target = setting.get() + inc
                val start = setting.range?.start ?: Float.MIN_VALUE
                val end = setting.range?.endInclusive ?: Float.MAX_VALUE
                if (target > end) {
                    target = start
                } else if (target < start) {
                    target = end
                }

                setting.set(target)
            }
            is IntSetting -> {
                var inc = 1
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                    inc = -inc

                var target = setting.get() + inc
                val start = setting.range?.start ?: Int.MIN_VALUE
                val end = setting.range?.endInclusive ?: Int.MAX_VALUE
                if (target > end) {
                    target = start + (target - end)
                } else if (target < start) {
                    target = end - (start - target)
                }

                setting.set(target)
            }
            is LongSetting -> {
                var inc = 1
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                    inc = -inc

                var target = setting.get() + inc
                val start = setting.range?.start ?: Long.MIN_VALUE
                val end = setting.range?.endInclusive ?: Long.MAX_VALUE
                if (target > end) {
                    target = start + (target - end)
                } else if (target < start) {
                    target = end - (start - target)
                }

                setting.set(target)
            }
            is BooleanSetting ->
                setting.set(!setting.get())
            is EnumSetting<*> ->
                setting.cycleEnumValue(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        }

        button.msg = getNameForSetting(setting)
    }

    private fun <T : Enum<T>> EnumSetting<T>.cycleEnumValue(backward: Boolean) {
        println("cycling")
        val enumConstants = enumClass.enumConstants
        println(enumConstants.map { it.name })
        val currentOrdinal = get(false).ordinal
        var target = currentOrdinal + if (backward) -1 else 1
        if (target < 0) target = enumConstants.size - 1
        else if (target >= enumConstants.size) target = 0
        set(enumConstants[target])
    }

    fun getNameForSetting(setting: Setting<*>): String {
        var name = "${setting.name}: "

        when (setting) {
            is DoubleSetting -> name += DecimalFormat("0.00").format(setting.get(false))
            is FloatSetting -> name += DecimalFormat("0.00").format(setting.get(false))
            is IntSetting -> name += setting.get(false)
            is LongSetting -> name += setting.get(false)
            is EnumSetting<*> -> name += setting.getEnumDisplayString()
            is BooleanSetting -> name += if (setting.get(false)) "Yes" else "No"
        }

        return name
    }

    private fun <T : Enum<T>> EnumSetting<T>.getEnumDisplayString(): String =
        nameProvider(get(false))
}