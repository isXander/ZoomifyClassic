package dev.isxander.zoomify.config

import dev.isxander.settxi.impl.SettingDisplayName

enum class ZoomKeyBehaviour(override val displayName: String) : SettingDisplayName {
    HOLD("Hold"),
    TOGGLE("Toggle")
}
