package com.tieniilfilo.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(
    title: String,
    tintColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .drawBehind { drawCircle(tintColor) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    val lineY = size.height - 2.dp.toPx()
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(tintColor, tintColor.copy(alpha = 0f)),
                            startX = 0f,
                            endX = size.width * 0.7f,
                        ),
                        start = Offset(0f, lineY),
                        end = Offset(size.width * 0.7f, lineY),
                        strokeWidth = 2.dp.toPx(),
                    )
                }
        )
    }
}
