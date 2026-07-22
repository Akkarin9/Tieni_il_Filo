package com.tieniilfilo.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.R
import com.tieniilfilo.app.ui.components.SectionHeader
import com.tieniilfilo.app.ui.screens.project.formatDate
import com.tieniilfilo.app.ui.theme.HeroAmber
import com.tieniilfilo.app.ui.theme.HeroCoral
import com.tieniilfilo.app.ui.theme.HeroRose
import com.tieniilfilo.app.ui.theme.HeroSage
import com.tieniilfilo.app.ui.theme.NunitoFontFamily
import com.tieniilfilo.app.ui.theme.pressAnimation
import com.tieniilfilo.app.ui.theme.squircleShape
import com.tieniilfilo.app.ui.theme.warmGradientMesh

private val AmbraColor = Color(0xFFFFB74D)
private val LavenderColor = Color(0xFFB084CC)
private val PeachColor = Color(0xFFE89F6C)
private val SageColor = Color(0xFF7BA37B)
private val CoralColor = Color(0xFFD4876A)

@Composable
fun HomeScreen(
    onYarnClick: () -> Unit,
    onProjectClick: () -> Unit,
    onPatternClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onStatsClick: () -> Unit,
    onActiveProjectsClick: () -> Unit = {},
    onCompletedProjectsClick: () -> Unit = {},
    onProjectDetailClick: (Long) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val yarnCount by viewModel.yarnCount.collectAsState()
    val activeProjects by viewModel.activeProjectCount.collectAsState()
    val completedProjects by viewModel.completedProjectCount.collectAsState()
    val activeProjectList by viewModel.activeProjects.collectAsState()
    val yarnColors by viewModel.distinctYarnColors.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .warmGradientMesh(),
    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            GreetingBanner(onSettingsClick = onSettingsClick)
        }

        item {
            BentoStatCards(
                yarnCount = yarnCount,
                activeProjects = activeProjects,
                completedProjects = completedProjects,
                onYarnClick = onYarnClick,
                onActiveProjectsClick = onActiveProjectsClick,
                onCompletedProjectsClick = onCompletedProjectsClick,
            )
        }

        if (yarnColors.isNotEmpty()) {
            item {
                ScrollableColorPalette(colors = yarnColors, onYarnClick = onYarnClick)
            }
        }

        item {
            QuickActionGrid(
                onPatternsClick = onPatternClick,
                onGalleryClick = onGalleryClick,
                onStatsClick = onStatsClick,
            )
        }

        if (activeProjectList.isNotEmpty()) {
            item {
                FeaturedActiveProjects(
                    projects = activeProjectList.take(3),
                    onProjectDetailClick = onProjectDetailClick,
                    onSeeAllClick = onProjectClick,
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
    }
}

@Composable
private fun GreetingBanner(onSettingsClick: () -> Unit) {
    val sparkleTransition = rememberInfiniteTransition(label = "sparkle")
    val sparkleAlpha by sparkleTransition.animateFloat(
        initialValue = 0f, targetValue = 0.6f,
        animationSpec = infiniteRepeatable(tween(3000), androidx.compose.animation.core.RepeatMode.Reverse),
        label = "sparkle",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        IconButton(onClick = onSettingsClick) {
            Icon(Icons.Rounded.Settings, contentDescription = stringResource(R.string.settings), tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun BentoStatCards(
    yarnCount: Int,
    activeProjects: Int,
    completedProjects: Int,
    onYarnClick: () -> Unit,
    onActiveProjectsClick: () -> Unit,
    onCompletedProjectsClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BentoStatCard(
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
            icon = Icons.Rounded.ShoppingCart,
            label = stringResource(R.string.yarns),
            value = yarnCount,
            startColor = PeachColor,
            endColor = Color(0xFFE8835A),
            onClick = onYarnClick,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BentoStatCard(
                modifier = Modifier.weight(1f).heightIn(min = 96.dp),
                icon = Icons.Rounded.Pending,
                label = stringResource(R.string.in_progress),
                value = activeProjects,
                startColor = LavenderColor,
                endColor = Color(0xFF9B6DB5),
                onClick = onActiveProjectsClick,
            )
            BentoStatCard(
                modifier = Modifier.weight(1f).heightIn(min = 96.dp),
                icon = Icons.Rounded.Celebration,
                label = stringResource(R.string.completed),
                value = completedProjects,
                startColor = Color(0xFFE8B86C),
                endColor = Color(0xFFE09F4A),
                onClick = onCompletedProjectsClick,
            )
        }
    }
}

@Composable
private fun BentoStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: Int,
    startColor: Color,
    endColor: Color,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Surface(
        modifier = modifier.clickable(interactionSource = interactionSource, indication = null, onClick = onClick).pressAnimation(isPressed),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.large)
                .drawBehind {
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(startColor.copy(alpha = 0.70f), endColor.copy(alpha = 0.45f))
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx()),
                    )
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.White.copy(alpha = 0.25f), Color.Transparent),
                            start = androidx.compose.ui.geometry.Offset.Zero,
                            end = androidx.compose.ui.geometry.Offset(0f, size.height),
                        ),
                        size = androidx.compose.ui.geometry.Size(size.width, 1.dp.toPx()),
                    )
                },
        ) {
            Row(
                modifier = Modifier.padding(12.dp).fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(startColor.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = com.tieniilfilo.app.ui.theme.animatedCounterText(value),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontFamily = com.tieniilfilo.app.ui.theme.FrauncesFontFamily,
                            fontSize = 28.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    )
                }
            }
        }
    }
}

@Composable
private fun ScrollableColorPalette(colors: List<Int>, onYarnClick: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(vertical = 12.dp),
        ) {
            SectionHeader(
                title = stringResource(R.string.your_colors),
                modifier = Modifier.padding(horizontal = 16.dp),
                tintColor = MaterialTheme.colorScheme.tertiary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(colors) { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(squircleShape)
                            .background(Color(color))
                            .clickable(onClick = onYarnClick),
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionGrid(
    onPatternsClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onStatsClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            QuickActionTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.AutoStories,
                label = stringResource(R.string.patterns),
                color = SageColor,
                onClick = onPatternsClick,
            )
            QuickActionTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.PhotoLibrary,
                label = stringResource(R.string.gallery_title),
                color = CoralColor,
                onClick = onGalleryClick,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            QuickActionTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.Insights,
                label = stringResource(R.string.stats_title),
                color = LavenderColor,
                onClick = onStatsClick,
            )
            // filler for symmetry
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun QuickActionTile(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Surface(
        modifier = modifier.clickable(interactionSource = interactionSource, indication = null, onClick = onClick).pressAnimation(isPressed),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color.copy(alpha = 0.1f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = color,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = color,
            )
        }
    }
}

@Composable
private fun FeaturedActiveProjects(
    projects: List<com.tieniilfilo.app.data.local.entity.ProjectEntity>,
    onProjectDetailClick: (Long) -> Unit,
    onSeeAllClick: () -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Pending,
                    contentDescription = null,
                    tint = AmbraColor,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.active_projects),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            projects.forEachIndexed { index, project ->
                FeaturedProjectCard(
                    project = project,
                    onClick = { onProjectDetailClick(project.id) },
                )
                if (index < projects.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onSeeAllClick, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(stringResource(R.string.see_all))
            }
        }
    }
}

@Composable
private fun FeaturedProjectCard(
    project: com.tieniilfilo.app.data.local.entity.ProjectEntity,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .pressAnimation(isPressed),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AmbraColor.copy(alpha = 0.08f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AmbraColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Pending,
                    contentDescription = null,
                    tint = AmbraColor,
                    modifier = Modifier.size(20.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (project.startDate != null) {
                    Text(
                        text = "Iniziato: ${formatDate(project.startDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (project.notes.isNotEmpty()) {
                    Text(
                        text = project.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
