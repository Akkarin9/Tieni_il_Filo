package com.tieniilfilo.app.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Modifier.warmGradientMesh(): Modifier {
    val transition = rememberInfiniteTransition(label = "mesh")
    val phase1 by transition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "meshPhase1",
    )
    val phase2 by transition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Restart),
        label = "meshPhase2",
    )
    val phase3 by transition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Restart),
        label = "meshPhase3",
    )
    return drawBehind {
        drawRect(color = Color(0xFFFFF8F0)) // solid base to ensure full opacity
        drawWarmGradientCircle(size.width, size.height, HeroCoral.copy(alpha = 1f), phase1, 0.4f, 0.3f, 0.3f)
        drawWarmGradientCircle(size.width, size.height, Color(0xFFFFD9CC).copy(alpha = 0.9f), phase2, 0.35f, 0.5f, 0.5f)
        drawWarmGradientCircle(size.width, size.height, HeroAmber.copy(alpha = 0.85f), phase3, 0.3f, 0.7f, 0.6f)
    }
}

private fun DrawScope.drawWarmGradientCircle(
    w: Float, h: Float,
    color: Color,
    phaseDeg: Float,
    radiusFraction: Float,
    centerXFrac: Float,
    centerYFrac: Float,
) {
    val cx = w * centerXFrac + (cos(Math.toRadians(phaseDeg.toDouble())).toFloat() * w * 0.15f)
    val cy = h * centerYFrac + (sin(Math.toRadians(phaseDeg.toDouble())).toFloat() * h * 0.1f)
    val radius = w.coerceAtLeast(h) * radiusFraction
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(color, color.copy(alpha = 0.5f), color.copy(alpha = 0.2f)),
            center = Offset(cx, cy),
            radius = radius,
        ),
        radius = radius,
        center = Offset(cx, cy),
    )
}

fun Brush.Companion.heroGradient(startColor: Color, endColor: Color): Brush = linearGradient(
    colors = listOf(startColor, endColor),
)
