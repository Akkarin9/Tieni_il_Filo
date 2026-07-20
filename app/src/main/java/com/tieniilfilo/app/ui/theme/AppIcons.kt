package com.tieniilfilo.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

object AppIcons {
    val CrochetHook: ImageVector by lazy {
        ImageVector.Builder(
            name = "CrochetHook",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            addPath(
                pathData = parsePath("M10 2 h4 v2 h-4 z"),
                fill = SolidColor(Color.Black),
            )
            addPath(
                pathData = parsePath("M11 4 h2 v14 h-2 z"),
                fill = SolidColor(Color.Black),
            )
            addPath(
                pathData = parsePath("M11 16 c0 3.5 -5 4.5 -7 1.5 c-2 -3 1 -5 4 -3 l0 1 c-2 -1 -4 0 -2.5 1.5 c1.5 1.5 5.5 0.5 5.5 -1.5"),
                fill = SolidColor(Color.Black),
            )
        }.build()
    }

    private fun parsePath(pathData: String) = PathParser().parsePathString(pathData).toNodes()
}
