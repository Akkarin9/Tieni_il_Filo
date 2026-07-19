package com.tieniilfilo.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val size: Float,
    val rotation: Float,
)

@Composable
fun ConfettiOverlay(
    active: Boolean,
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {},
) {
    if (!active) return

    val colors = listOf(
        Color(0xFFF4C2C2),
        Color(0xFFC4C4F7),
        Color(0xFFC2E0C6),
        Color(0xFFFFFDD0),
        Color(0xFFE8A090),
        Color(0xFFD4A853),
    )

    var particles by remember {
        mutableStateOf(
            List(48) {
                Particle(
                    x = Random.nextFloat(),
                    y = Random.nextFloat() * -0.3f,
                    vx = (Random.nextFloat() - 0.5f) * 0.01f,
                    vy = Random.nextFloat() * 0.012f + 0.006f,
                    color = colors.random(),
                    size = Random.nextFloat() * 10f + 6f,
                    rotation = Random.nextFloat() * 360f,
                )
            }
        )
    }
    var tick by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(active) {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < 2200) {
            tick += 1f
            particles = particles.map { p ->
                p.copy(
                    x = p.x + p.vx,
                    y = p.y + p.vy,
                    rotation = p.rotation + 8f,
                )
            }
            delay(16)
        }
        onFinished()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        particles.forEach { p ->
            rotate(p.rotation, pivot = Offset(p.x * w, p.y * h)) {
                drawRect(
                    color = p.color,
                    topLeft = Offset(p.x * w, p.y * h),
                    size = androidx.compose.ui.geometry.Size(p.size, p.size * 0.6f),
                )
            }
        }
        // keep tick referenced so recomposition runs
        if (tick < 0) drawCircle(Color.Transparent)
    }
}
