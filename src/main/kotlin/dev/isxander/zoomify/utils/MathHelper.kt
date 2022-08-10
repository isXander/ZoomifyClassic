@file:JvmName("MathHelper")
package dev.isxander.zoomify.utils

fun lerp(delta: Double, start: Double, end: Double): Double {
    return start + delta * (end - start)
}