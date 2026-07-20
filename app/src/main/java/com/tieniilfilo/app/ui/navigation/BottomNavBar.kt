package com.tieniilfilo.app.ui.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun TieniIlFiloBottomNav(
    currentRoute: String?,
    onItemClick: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.screen.route

            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.screen) },
                icon = {
                    Box(contentAlignment = Alignment.Center) {
                        val pillWidth by animateDpAsState(
                            targetValue = if (selected) 48.dp else 0.dp,
                            animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
                            label = "navPill",
                        )
                        if (selected) {
                            Box(
                                modifier = Modifier
                                    .size(pillWidth, 32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
                            )
                        }
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                        )
                    }
                },
                label = { Text(text = item.label) },
            )
        }
    }
}
