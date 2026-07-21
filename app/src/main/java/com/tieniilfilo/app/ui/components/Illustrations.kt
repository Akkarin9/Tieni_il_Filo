package com.tieniilfilo.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.cos
import kotlin.math.sin

private val IllColor = Color(0xFFB59B88).copy(alpha = 0.5f)
private val IllAccent = Color(0xFFD4876A).copy(alpha = 0.4f)

@Composable
fun SkeinIllustration(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "skeinSpin")
    val rotation by transition.animateFloat(0f, 360f, infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Restart), label = "skeinRot")
    Canvas(modifier = modifier.graphicsLayer(rotationZ = rotation)) {
        val cx = size.width / 2
        val cy = size.height / 2
        val r = size.minDimension * 0.35f
        val rx = r * 1.2f
        val ry = r * 1.4f
        drawOval(IllColor, Offset(cx - rx, cy - ry), Size(rx * 2, ry * 2))
        for (i in 0..8) {
            val a = i * 40f
            drawLine(IllAccent, Offset(cx + rx * cos(Math.toRadians(a.toDouble())).toFloat(), cy + ry * sin(Math.toRadians(a.toDouble())).toFloat()), Offset(cx + rx * cos(Math.toRadians((a + 140f).toDouble())).toFloat(), cy + ry * sin(Math.toRadians((a + 140f).toDouble())).toFloat()), strokeWidth = 2f)
        }
        val tailPath = Path().apply {
            moveTo(cx - r * 0.5f, cy - r * 0.8f)
            cubicTo(cx - r * 1.1f, cy - r * 1.5f, cx + r * 0.5f, cy - r * 1.3f, cx + r * 0.8f, cy - r * 0.6f)
        }
        drawPath(tailPath, IllAccent, style = Stroke(width = 3f))
    }
}

@Composable
fun BasketEmptyIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2
        val cy = size.height * 0.55f
        val w = size.width * 0.55f
        val h = size.height * 0.25f
        drawOval(IllColor, Offset(cx - w, cy - h), Size(w * 2, h * 2))
        drawOval(IllColor.copy(alpha = 0.3f), Offset(cx - w * 1.1f, cy + h * 0.5f - h * 0.6f), Size(w * 2.2f, h * 1.2f))
        drawOval(Color.Transparent, Offset(cx - w * 1.1f, cy - h), Size(w * 2.2f, h * 0.6f), style = Stroke(width = 4f))
        val handlePath = Path().apply {
            moveTo(cx - w * 0.7f, cy - h * 0.1f)
            cubicTo(cx - w * 0.5f, cy - h * 1.5f, cx + w * 0.5f, cy - h * 1.5f, cx + w * 0.7f, cy - h * 0.1f)
        }
        drawPath(handlePath, IllColor, style = Stroke(width = 4f))
        for (i in -3..3) {
            val lx = cx + i * w * 0.18f
            if (lx > cx - w || lx < cx + w) {
                drawLine(IllAccent.copy(alpha = 0.3f), Offset(lx, cy - h * 0.6f), Offset(lx, cy + h * 0.3f))
            }
        }
    }
}

@Composable
fun OpenBookIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2
        val cy = size.height * 0.5f
        val w = size.width * 0.38f
        val h = size.height * 0.4f
        val pageColor = IllColor.copy(alpha = 0.8f)
        drawRoundRect(pageColor, Offset(cx - w * 2, cy - h), Size(w * 2, h * 2), CornerRadius(4f, 4f))
        drawRoundRect(pageColor, Offset(cx, cy - h), Size(w * 2, h * 2), CornerRadius(4f, 4f))
        drawLine(IllAccent, Offset(cx, cy - h), Offset(cx, cy + h), strokeWidth = 4f)
        for (i in 0..4) {
            val lineY = cy - h * 0.6f + i * h * 0.25f
            drawLine(Color.White.copy(alpha = 0.5f), Offset(cx - w * 1.6f, lineY), Offset(cx - w * 0.2f, lineY))
            drawLine(Color.White.copy(alpha = 0.5f), Offset(cx + w * 0.2f, lineY), Offset(cx + w * 1.6f, lineY))
        }
    }
}

@Composable
fun HookIllustration(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "hookSway")
    val sway by transition.animateFloat(-3f, 3f, infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse), label = "sway")
    Canvas(modifier = modifier.graphicsLayer(rotationZ = sway)) {
        val cx = size.width / 2
        val topY = size.height * 0.1f
        val bottomY = size.height * 0.9f
        val lw = 4f
        drawLine(IllColor.copy(alpha = 0.8f), Offset(cx, topY), Offset(cx, bottomY * 0.75f), strokeWidth = lw)
        drawCircle(IllColor.copy(alpha = 0.8f), radius = 12f, center = Offset(cx, topY + 12f), style = Stroke(width = lw))
        val hookPath = Path().apply {
            moveTo(cx, bottomY * 0.75f)
            cubicTo(cx + 30f, bottomY * 0.85f, cx + 25f, bottomY, cx - 8f, bottomY * 0.85f)
        }
        drawPath(hookPath, IllColor.copy(alpha = 0.8f), style = Stroke(width = lw))
        drawCircle(IllColor.copy(alpha = 0.8f), radius = 6f, center = Offset(cx - 8f, bottomY * 0.85f))
    }
}

@Composable
fun CrochetTileIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val s = size.minDimension * 0.4f
        val cx = size.width / 2
        val cy = size.height / 2
        drawRoundRect(Color(0xFFE8D5C8).copy(alpha = 0.4f), Offset(cx - s, cy - s), Size(s * 2, s * 2))
        val positions = listOf(Offset(cx - s * 0.3f, cy - s * 0.3f), Offset(cx + s * 0.3f, cy - s * 0.3f), Offset(cx, cy + s * 0.3f))
        val ss = 6f
        for (p in positions) {
            drawLine(IllColor, Offset(p.x - ss, p.y - ss), Offset(p.x + ss, p.y + ss))
            drawLine(IllColor, Offset(p.x + ss, p.y - ss), Offset(p.x - ss, p.y + ss))
        }
    }
}
