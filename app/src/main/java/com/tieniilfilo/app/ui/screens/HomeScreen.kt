package com.tieniilfilo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    onYarnClick: () -> Unit,
    onProjectClick: () -> Unit,
    onPatternClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val yarnCount by viewModel.yarnCount.collectAsState()
    val activeProjects by viewModel.activeProjectCount.collectAsState()
    val completedProjects by viewModel.completedProjectCount.collectAsState()
    val pausedProject by viewModel.pausedProject.collectAsState()
    val yarnColors by viewModel.distinctYarnColors.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    GreetingHeader()
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Impostazioni")
                }
            }
        }

        item {
            StatCardsRow(
                yarnCount = yarnCount,
                activeProjects = activeProjects,
                completedProjects = completedProjects,
                onYarnClick = onYarnClick,
                onProjectClick = onProjectClick,
            )
        }

        if (pausedProject != null) {
            item {
                ResumeProjectCard(
                    projectName = pausedProject!!.name,
                    projectNotes = pausedProject!!.notes.ifBlank { "In pausa — riprendi da dove hai lasciato" },
                    onProjectClick = onProjectClick,
                )
            }
        }

        if (yarnColors.isNotEmpty()) {
            item {
                ColorPaletteWidget(
                    colors = yarnColors,
                    onYarnClick = onYarnClick,
                )
            }
        }

        item {
            QuickActionsRow(
                onPatternsClick = onPatternClick,
                onGalleryClick = onGalleryClick,
                onStatsClick = onStatsClick,
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun GreetingHeader() {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Ciao creativa!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Pronta per il prossimo progetto?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun StatCardsRow(
    yarnCount: Int,
    activeProjects: Int,
    completedProjects: Int,
    onYarnClick: () -> Unit,
    onProjectClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.ShoppingCart,
            label = "Filati",
            value = "$yarnCount",
            startColor = Color(0xFFE89F6C),
            endColor = Color(0xFFE8835A),
            onClick = onYarnClick,
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.AutoAwesome,
            label = "In corso",
            value = "$activeProjects",
            startColor = Color(0xFFB084CC),
            endColor = Color(0xFF9B6DB5),
            onClick = onProjectClick,
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Celebration,
            label = "Completati",
            value = "$completedProjects",
            startColor = Color(0xFFE8B86C),
            endColor = Color(0xFFE09F4A),
            onClick = onProjectClick,
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    startColor: Color,
    endColor: Color,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(startColor.copy(alpha = 0.32f), endColor.copy(alpha = 0.14f))
                    ),
                    shape = RoundedCornerShape(20.dp),
                ),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(startColor.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = startColor,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = startColor,
                )
            }
        }
    }
}

@Composable
private fun ResumeProjectCard(
    projectName: String,
    projectNotes: String,
    onProjectClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProjectClick),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8A87C).copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8A87C).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color(0xFFE8A87C),
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = projectName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = projectNotes,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE8A87C),
                )
            }
        }
    }
}

@Composable
private fun ColorPaletteWidget(
    colors: List<Int>,
    onYarnClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(16.dp),
        ) {
            Text(
                text = "I tuoi colori",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 10.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(color))
                            .clickable(onClick = onYarnClick),
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsRow(
    onPatternsClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onStatsClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        QuickActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Star,
            label = "Schemi",
            color = Color(0xFF7BA37B),
            onClick = onPatternsClick,
        )
        QuickActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Check,
            label = "Galleria",
            color = Color(0xFFD4876A),
            onClick = onGalleryClick,
        )
        QuickActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.AutoAwesome,
            label = "Statistiche",
            color = Color(0xFFB084CC),
            onClick = onStatsClick,
        )
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = color,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = color,
            )
        }
    }
}
