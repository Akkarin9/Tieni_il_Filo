package com.tieniilfilo.app.ui.screens.project

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.ProjectStatus
import com.tieniilfilo.app.ui.components.ChipColor
import com.tieniilfilo.app.ui.components.EmptyState
import com.tieniilfilo.app.ui.components.StatusChip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProjectsScreen(
    onProjectClick: (Long) -> Unit,
    viewModel: ProjectViewModel = hiltViewModel(),
) {
    val projects by viewModel.allProjects.collectAsState()
    var selectedStatus by remember { mutableStateOf<ProjectStatus?>(null) }

    val filteredProjects = if (selectedStatus != null) {
        projects.filter { it.status == selectedStatus }
    } else {
        projects
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Progetti") })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("Tutti") },
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
                                    modifier = Modifier.padding(end = 4.dp),
                                )
                                Text(status.toDisplayString())
                            }
                        },
                        modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                    )
                }
            }

            if (filteredProjects.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.Spa,
                    title = "Nessun progetto",
                    subtitle = "Inizia il tuo primo progetto creativo!",
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(filteredProjects, key = { it.id }) { project ->
                        ProjectListItem(
                            project = project,
                            onClick = { onProjectClick(project.id) },
                        )
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(onClick = onClick),
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
                    label = project.status.toDisplayString(),
                    chipColor = project.status.toChipColor(),
                )
            }

            if (project.startDate != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Event,
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

            if (project.endDate != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
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

fun ProjectStatus.toChipColor(): ChipColor = when (this) {
    ProjectStatus.IN_CORSO -> ChipColor.GREEN
    ProjectStatus.IN_PAUSA -> ChipColor.HONEY
    ProjectStatus.COMPLETATO -> ChipColor.PINK
    ProjectStatus.DA_INIZIARE -> ChipColor.LAVENDER
}

fun ProjectStatus.toIcon() = when (this) {
    ProjectStatus.IN_CORSO -> Icons.Default.AutoAwesome
    ProjectStatus.IN_PAUSA -> Icons.Default.Pause
    ProjectStatus.COMPLETATO -> Icons.Default.Celebration
    ProjectStatus.DA_INIZIARE -> Icons.Default.Schedule
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
