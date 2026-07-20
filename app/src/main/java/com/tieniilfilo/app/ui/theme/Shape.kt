package com.tieniilfilo.app.ui.theme

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.unit.dp

val TieniIlFiloShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

val squircleShape = GenericShape { size, _ ->
    val radius = size.minDimension * 0.35f
    addRoundRect(
        RoundRect(
            rect = androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.height),
            topLeft = CornerRadius(radius),
            topRight = CornerRadius(radius),
            bottomRight = CornerRadius(radius),
            bottomLeft = CornerRadius(radius),
        )
    )
}

val pillShape = GenericShape { size, _ ->
    val radius = size.minDimension / 2f
    addRoundRect(
        RoundRect(
            rect = androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.height),
            topLeft = CornerRadius(radius),
            topRight = CornerRadius(radius),
            bottomRight = CornerRadius(radius),
            bottomLeft = CornerRadius(radius),
        )
    )
}
