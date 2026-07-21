package com.tieniilfilo.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun StitchDivider(
    modifier: Modifier = Modifier,
    tint: Color = Color(0xFFB59B88).copy(alpha = 0.4f),
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp),
    ) {
        val y = size.height / 2
        val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 5f), 0f)
        drawLine(
            color = tint,
            start = Offset(16f, y),
            end = Offset(size.width - 16f, y),
            strokeWidth = 1.5f,
            pathEffect = dash,
        )
        // Thread-end nodes
        drawCircle(tint, radius = 2.5f, center = Offset(16f, y))
        drawCircle(tint, radius = 2.5f, center = Offset(size.width - 16f, y))
    }
}
