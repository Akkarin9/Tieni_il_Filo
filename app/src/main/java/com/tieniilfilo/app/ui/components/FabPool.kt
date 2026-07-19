package com.tieniilfilo.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class FabState(
    val icon: ImageVector,
    val label: String,
    val color: Long,
)

@Composable
fun AnimatedFab(
    state: FabState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation by animateFloatAsState(
        targetValue = if (state.label == "Progetti") 180f else 0f,
        animationSpec = spring(),
        label = "fabRotation",
    )

    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Crossfade(
            targetState = state.icon,
            animationSpec = tween(300),
            label = "fabIcon",
        ) { icon ->
            Icon(
                imageVector = icon,
                contentDescription = state.label,
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}

val DefaultFabState = FabState(
    icon = Icons.Default.Add,
    label = "",
    color = 0xFF000000,
)
