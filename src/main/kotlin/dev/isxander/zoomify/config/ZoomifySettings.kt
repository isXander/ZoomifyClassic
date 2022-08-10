package dev.isxander.zoomify.config

import dev.isxander.settxi.impl.*
import dev.isxander.settxi.serialization.SettxiConfigKotlinx
import dev.isxander.zoomify.utils.TransitionType
import net.fabricmc.loader.api.FabricLoader

object ZoomifySettings : SettxiConfigKotlinx(FabricLoader.getInstance().configDir.resolve("zoomify.json")) {
    const val BEHAVIOUR = "Behaviour"
    const val SCROLLING = "Scrolling"
    const val CONTROLS = "Controls"

    var initialZoom by int(4) {
        name = "Initial Zoom"
        description = "The zoom level when you first press the keybind."
        category = BEHAVIOUR
        range = 1..10
    }

    var zoomInTime by double(2.0) {
        name = "Zoom In Time"
        description = "How many seconds to complete zooming in."
        category = BEHAVIOUR
        range = 0.1..20.0
    }

    var zoomOutTime by double(0.5) {
        name = "Zoom Out Time"
        description = "How many seconds to complete zooming out."
        category = BEHAVIOUR
        range = 0.1..20.0
    }

    var zoomInTransition by enum(TransitionType.EASE_OUT_EXP) {
        name = "Zoom In Transition"
        description = "The transition type used when zooming in."
        category = BEHAVIOUR
    }

    var zoomOutTransition by enum(TransitionType.EASE_OUT_EXP) {
        name = "Zoom Out Transition"
        description = "The transition type used when zooming out."
        category = BEHAVIOUR
        modifyGet { it.opposite() }
    }

    var scrollZoom by boolean(true) {
        name = "Enable Scroll Zoom"
        description = "Allow you to zoom in and out more with scroll wheel."
        category = SCROLLING
    }

    var scrollZoomAmount by int(3) {
        name = "Scroll Zoom Amount"
        description = "The additional divisor of the FOV per scroll step."
        category = SCROLLING
        range = 1..10
    }

    var scrollZoomSmoothness by int(70) {
        name = "Scroll Zoom Smoothness"
        description = "How smooth/how long it takes to scroll-zoom."
        category = SCROLLING
        range = 0..100
    }

    var zoomKeyBehaviour by enum(ZoomKeyBehaviour.HOLD) {
        name = "Zoom Key Behaviour"
        description = "The behaviour of the zoom key."
        category = CONTROLS
    }

    var relativeSensitivity by int(100) {
        name = "Relative Sensitivity"
        description = "Controls the sensitivity based on the zoom level."
        category = CONTROLS
        range = 0..150
    }

    var relativeViewBobbing by boolean(true) {
        name = "Relative View Bobbing"
        description = "Make view-bobbing less jarring when zoomed in."
        category = CONTROLS
    }

    init {
        import()
    }
}
