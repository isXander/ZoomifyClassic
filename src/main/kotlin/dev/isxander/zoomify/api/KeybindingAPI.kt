package dev.isxander.zoomify.api

import com.mojang.minecraft.input.Keybind

object KeybindingAPI {
    private val customKeybinds = mutableListOf<KeybindInfo>()
    private val startIndex = 9

    fun register(name: String, key: Int): KeybindInfo {
        val keybind = KeybindInfo(Keybind(name, key), startIndex + customKeybinds.size + 1)
        customKeybinds += keybind
        return keybind
    }

    fun addCustomKeybinds(keys: Array<Keybind>): Array<Keybind> {
        val vanillaKeys = keys.toMutableList()
        vanillaKeys.addAll(customKeybinds.map { it.keybind })
        return vanillaKeys.toTypedArray()
    }

    data class KeybindInfo(val keybind: Keybind, val idx: Int)
}