package com.tieniilfilo.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ChipColor {
    GREEN, LAVENDER, PINK, CORAL, HONEY, GRAY
}

fun ChipColor.toColor(): Color = when (this) {
    ChipColor.GREEN -> Color(0xFFC2E0C6)
    ChipColor.LAVENDER -> Color(0xFFC4C4F7)
    ChipColor.PINK -> Color(0xFFF4C2C2)
    ChipColor.CORAL -> Color(0xFFE8A090)
    ChipColor.HONEY -> Color(0xFFD4A853)
    ChipColor.GRAY -> Color(0xFFD5CFC5)
}

fun ChipColor.toOnColor(): Color = when (this) {
    ChipColor.GREEN -> Color(0xFF2C5C24)
    ChipColor.LAVENDER -> Color(0xFF3B3060)
    ChipColor.PINK -> Color(0xFF6E3030)
    ChipColor.CORAL -> Color(0xFF5C2828)
    ChipColor.HONEY -> Color(0xFF5C4020)
    ChipColor.GRAY -> Color(0xFF5C5040)
}

@Composable
fun StatusChip(
    label: String,
    chipColor: ChipColor,
    modifier: Modifier = Modifier,
    dotColor: Color? = null,
    isActive: Boolean = false,
) {
    val bg = chipColor.toColor()
    val content = chipColor.toOnColor()

    Surface(
        modifier = modifier.then(
            if (isActive) Modifier.border(1.5.dp, content.copy(alpha = 0.3f), MaterialTheme.shapes.small)
            else Modifier
        ),
        shape = MaterialTheme.shapes.small,
        color = bg,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor ?: content)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = content,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun ColorSwatch(
    colorHex: Long,
    modifier: Modifier = Modifier,
    size: Int = 24,
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color(colorHex.toInt())),
    )
}
