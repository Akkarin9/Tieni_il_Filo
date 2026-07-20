package com.tieniilfilo.app.ui.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

object AnimSpec {
    val press = spring<Float>(dampingRatio = 0.5f, stiffness = 500f)
    val counter = spring<Float>(dampingRatio = 0.6f, stiffness = 200f)
    val pageIndicator = spring<Float>(dampingRatio = 0.7f, stiffness = 300f)
}

@Composable
fun Modifier.pressAnimation(isPressed: Boolean): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = AnimSpec.press,
        label = "pressScale",
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = AnimSpec.press,
        label = "pressAlpha",
    )
    return graphicsLayer {
        scaleX = scale
        scaleY = scale
        this.alpha = alpha
    }
}

@Composable
fun animatedCounterText(target: Int, animationSpec: AnimationSpec<Int> = spring(dampingRatio = 0.6f, stiffness = 200f)): String {
    val count by animateIntAsState(
        targetValue = target,
        animationSpec = animationSpec,
        label = "counter",
    )
    return count.toString()
}

fun staggerEnter(index: Int, delayPerItem: Int = 40) = fadeIn(
    tween(300, delayMillis = (index % 8) * delayPerItem),
) + slideInVertically(
    tween(300, delayMillis = (index % 8) * delayPerItem),
) { it / 4 }
