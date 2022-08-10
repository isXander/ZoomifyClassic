package dev.isxander.zoomify.utils

import dev.isxander.settxi.impl.SettingDisplayName
import kotlin.math.*

enum class TransitionType(override val displayName: String) : Transition, SettingDisplayName {
    INSTANT("Instant") {
        override fun apply(t: Double) =
            t
    },
    LINEAR("Linear") {
        override fun apply(t: Double) =
            t
    },
    EASE_IN_SINE("Ease In Sine") {
        override fun apply(t: Double): Double =
            1 - cos((t * PI) / 2)

        override fun inverse(x: Double) =
            acos(-(x - 1)) * 2 / PI
    },
    EASE_OUT_SINE("Ease Out Sine") {
        override fun apply(t: Double): Double =
            sin((t * PI) / 2)

        override fun inverse(x: Double) =
            asin(x) * 2 / PI
    },
    EASE_IN_OUT_SINE("Ease In/Out Sine") {
        override fun apply(t: Double): Double =
            -(cos(PI * t) - 1) / 2
    },
    EASE_IN_QUAD("Ease In Quad") {
        override fun apply(t: Double): Double =
            t * t

        override fun inverse(x: Double) =
            sqrt(x)
    },
    EASE_OUT_QUAD("Ease Out Quad") {
        override fun apply(t: Double): Double =
            1 - (1 - t) * (1 - t)

        override fun inverse(x: Double) =
            -(sqrt(-(x - 1)) - 1)

    },
    EASE_IN_OUT_QUAD("Ease In/Out Quad") {
        override fun apply(t: Double): Double =
            if (t < 0.5)
                2 * t * t
            else
                1 - (-2 * t + 2).pow(2) / 2
    },
    EASE_IN_CUBIC("Ease In Cubic") {
        override fun apply(t: Double): Double =
            t.pow(3)

        override fun inverse(x: Double) =
            x.pow(1 / 3.0)
    },
    EASE_OUT_CUBIC("Ease Out Cubic") {
        override fun apply(t: Double): Double =
            1 - (1 - t).pow(3)

        override fun inverse(x: Double) =
            -((-x + 1).pow(1.0 / 3.0)) + 1
    },
    EASE_IN_OUT_CUBIC("Ease In/Out Cubic") {
        override fun apply(t: Double): Double =
            if (t < 0.5)
                4 * t * t * t
            else
                1 - (-2 * t + 2).pow(3) / 2
    },
    EASE_IN_EXP("Ease In Exp") {
        private val c_log2_1023 = log(1023.0, base=2.0)

        override fun apply(t: Double): Double =
            when (t) {
                0.0 -> 0.0
                1.0 -> 1.0
                else -> 2.0.pow(10.0 * t - c_log2_1023) - 1/1023
            }

        override fun inverse(x: Double) =
            when (x) {
                0.0 -> 0.0
                1.0 -> 1.0
                else -> ln(1023 * x + 1) / (10 * ln(2.0))
            }
    },
    EASE_OUT_EXP("Ease Out Exp") {
        private val c_log2_1023 = log(1023.0, base=2.0)
        private val c_10_ln2 = 10.0 * ln(2.0)
        private val c_ln_1203 = ln(1023.0)

        override fun apply(t: Double): Double =
            when (t) {
                0.0 -> 0.0
                1.0 -> 1.0
                else -> 1.0 - 2.0.pow(10.0 - c_log2_1023 - 10.0 * t) + 1/1023
            }

        override fun inverse(x: Double) =
            when (x) {
                0.0 -> 0.0
                1.0 -> 1.0
                else -> -((ln(-((1023 * x - 1024) / 1023)) - c_10_ln2 + c_ln_1203) / c_10_ln2)
            }
    },
    EASE_IN_OUT_EXP("Ease In/Out Exp") {
        private val c_log2_1023 = log(1023.0, base=2.0)

        override fun apply(t: Double): Double =
            when {
                t == 0.0 -> 0.0
                t == 1.0 -> 1.0
                t < 0.5 -> 2.0.pow(20.0 * t - c_log2_1023) - 1/1023
                else -> 1.0 - 2.0.pow(10.0 - c_log2_1023 - 10.0 * t) + 1/1023
            }
    };

    fun opposite(): TransitionType = when (this) {
        INSTANT -> INSTANT
        LINEAR -> LINEAR
        EASE_IN_SINE -> EASE_OUT_SINE
        EASE_OUT_SINE -> EASE_IN_SINE
        EASE_IN_OUT_SINE -> EASE_IN_OUT_SINE
        EASE_IN_QUAD -> EASE_OUT_QUAD
        EASE_OUT_QUAD -> EASE_IN_QUAD
        EASE_IN_OUT_QUAD -> EASE_IN_OUT_QUAD
        EASE_IN_CUBIC -> EASE_OUT_CUBIC
        EASE_OUT_CUBIC -> EASE_IN_CUBIC
        EASE_IN_OUT_CUBIC -> EASE_IN_OUT_CUBIC
        EASE_IN_EXP -> EASE_OUT_EXP
        EASE_OUT_EXP -> EASE_IN_EXP
        EASE_IN_OUT_EXP -> EASE_IN_OUT_EXP
    }
}

fun interface Transition {
    fun apply(t: Double): Double

    fun inverse(x: Double): Double {
        throw UnsupportedOperationException()
    }

    fun hasInverse() = try {
        inverse(0.0)
        true
    } catch (_: UnsupportedOperationException) {
        false
    }
}
