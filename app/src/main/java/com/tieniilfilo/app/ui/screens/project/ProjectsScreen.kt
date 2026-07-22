package com.tieniilfilo.app.ui.screens.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.ProjectStatus
import com.tieniilfilo.app.ui.components.ChipColor
import com.tieniilfilo.app.ui.components.EmptyState
import com.tieniilfilo.app.ui.components.StatusChip
import com.tieniilfilo.app.ui.theme.pressAnimation
import com.tieniilfilo.app.ui.theme.staggerEnter
import androidx.compose.ui.res.stringResource
import com.tieniilfilo.app.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val InCorsoColor = Color(0xFFFFB74D)
private val InPausaColor = Color(0xFF90A4AE)
private val CompletatoColor = Color(0xFF81C784)
private val DaIniziareColor = Color(0xFFB39DDB)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProjectsScreen(
    onProjectClick: (Long) -> Unit,
    onAddClick: () -> Unit = {},
    initialStatus: ProjectStatus? = null,
    viewModel: ProjectViewModel = hiltViewModel(),
) {
    val projects by viewModel.allProjects.collectAsState()
    var selectedStatus by remember(initialStatus) { mutableStateOf(initialStatus) }
    var searchQuery by remember { mutableStateOf("") }
    var searchExpanded by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var sortMode by remember { mutableStateOf(0) } // 0=nome, 1=data creazione, 2=scadenza

    val searched = if (searchQuery.isBlank()) projects else projects.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.notes.contains(searchQuery, ignoreCase = true)
    }

    val filteredProjects = if (selectedStatus != null) {
        searched.filter { it.status == selectedStatus }
    } else {
        searched
    }.let { list ->
        when (sortMode) {
            0 -> list.sortedBy { it.name.lowercase() }
            1 -> list.sortedByDescending { it.createdAt }
            2 -> list.sortedBy { it.targetDeadline ?: Long.MAX_VALUE }
            else -> list
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.projects_title)) },
                actions = {
                    Row(
                        modifier = Modifier.clickable { showSortMenu = true }.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(stringResource(R.string.sort), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Rounded.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                        DropdownMenuItem(text = { Text(stringResource(R.string.sort_name)) }, onClick = { sortMode = 0; showSortMenu = false })
                        DropdownMenuItem(text = { Text(stringResource(R.string.sort_recent)) }, onClick = { sortMode = 1; showSortMenu = false })
                        DropdownMenuItem(text = { Text(stringResource(R.string.sort_deadline)) }, onClick = { sortMode = 2; showSortMenu = false })
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { searchExpanded = false },
                active = searchExpanded,
                onActiveChange = { searchExpanded = it },
                placeholder = { Text(stringResource(R.string.search_projects)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            ) {}
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text(stringResource(R.string.all)) },
                    modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                )
                ProjectStatus.entries.forEach { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = {
                            selectedStatus = if (selectedStatus == status) null else status
                        },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = status.toIcon(),
                                    contentDescription = null,
                                    tint = status.toColor(),
                                    modifier = Modifier.padding(end = 4.dp),
                                )
                                Text(status.displayText())
                            }
                        },
                        modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                    )
                }
            }

            if (filteredProjects.isEmpty()) {
                EmptyState(
                    icon = Icons.Rounded.Palette,
                    title = stringResource(R.string.no_projects),
                    subtitle = stringResource(R.string.no_projects_sub),
                    actionLabel = stringResource(R.string.add_project),
                    onActionClick = onAddClick,
                    illustration = { com.tieniilfilo.app.ui.components.BasketEmptyIllustration() },
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(filteredProjects, key = { _, it -> it.id }) { index, project ->
                        AnimatedVisibility(visible = true, enter = staggerEnter(index)) {
                            ProjectListItem(
                                project = project,
                                onClick = { onProjectClick(project.id) },
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun ProjectListItem(
    project: ProjectEntity,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .pressAnimation(isPressed),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                StatusChip(
                    label = project.status.displayText(),
                    chipColor = project.status.toChipColor(),
                    isActive = project.status == ProjectStatus.IN_CORSO,
                )
            }

            if (project.startDate != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Event,
                        contentDescription = null,
                        modifier = Modifier.height(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Iniziato: ${formatDate(project.startDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (project.targetDeadline != null) {
                val daysLeft = daysUntil(project.targetDeadline)
                val deadlineColor = when {
                    daysLeft < 0 -> Color(0xFFE53935) // red
                    daysLeft <= 3 -> Color(0xFFFFB74D) // amber
                    else -> Color(0xFF81C784) // green
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        modifier = Modifier.height(14.dp),
                        tint = deadlineColor,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when {
                            daysLeft < 0 -> "Scaduto da ${-daysLeft} giorni"
                            daysLeft == 0 -> "Scade oggi!"
                            daysLeft == 1 -> "Scade domani!"
                            else -> "Scade tra $daysLeft giorni"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = deadlineColor,
                    )
                }
            }

            if (project.endDate != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.height(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Completato: ${formatDate(project.endDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (project.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = project.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                )
            }
        }
    }
} 

fun ProjectStatus.toDisplayString(): String = when (this) {
    ProjectStatus.IN_CORSO -> "In corso"
    ProjectStatus.IN_PAUSA -> "In pausa"
    ProjectStatus.COMPLETATO -> "Completato"
    ProjectStatus.DA_INIZIARE -> "Da iniziare"
}

@Composable
fun ProjectStatus.displayText(): String = stringResource(when (this) {
    ProjectStatus.IN_CORSO -> R.string.project_status_in_progress
    ProjectStatus.IN_PAUSA -> R.string.project_status_paused
    ProjectStatus.COMPLETATO -> R.string.project_status_completed_label
    ProjectStatus.DA_INIZIARE -> R.string.project_status_to_start
})

fun ProjectStatus.toChipColor(): ChipColor = when (this) {
    ProjectStatus.IN_CORSO -> ChipColor.GREEN
    ProjectStatus.IN_PAUSA -> ChipColor.HONEY
    ProjectStatus.COMPLETATO -> ChipColor.PINK
    ProjectStatus.DA_INIZIARE -> ChipColor.LAVENDER
}

fun ProjectStatus.toIcon() = when (this) {
    ProjectStatus.IN_CORSO -> Icons.Rounded.Pending
    ProjectStatus.IN_PAUSA -> Icons.Rounded.Pause
    ProjectStatus.COMPLETATO -> Icons.Rounded.CheckCircle
    ProjectStatus.DA_INIZIARE -> Icons.Rounded.Schedule
}

fun ProjectStatus.toColor() = when (this) {
    ProjectStatus.IN_CORSO -> InCorsoColor
    ProjectStatus.IN_PAUSA -> InPausaColor
    ProjectStatus.COMPLETATO -> CompletatoColor
    ProjectStatus.DA_INIZIARE -> DaIniziareColor
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun daysUntil(timestamp: Long): Int {
    val now = System.currentTimeMillis()
    val diff = timestamp - now
    return (diff / (1000L * 60L * 60L * 24L)).toInt()
}
