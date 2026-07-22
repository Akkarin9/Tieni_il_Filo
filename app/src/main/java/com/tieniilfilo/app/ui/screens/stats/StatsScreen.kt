package com.tieniilfilo.app.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.R
import com.tieniilfilo.app.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
) {
    val yarnCount by viewModel.yarnCount.collectAsState()
    val hooksCount by viewModel.hookCount.collectAsState()
    val activeProjects by viewModel.activeProjects.collectAsState()
    val completedProjects by viewModel.completedProjects.collectAsState()
    val pausedProjects by viewModel.pausedProjects.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val totalProjects = activeProjects + completedProjects + pausedProjects

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.stats_title)) })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatBox(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.yarns),
                    value = "$yarnCount",
                    color = Color(0xFFC2E0C6),
                )
                StatBox(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.hooks_title),
                    value = "$hooksCount",
                    color = Color(0xFFC4C4F7),
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatBox(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.in_progress),
                    value = "$activeProjects",
                    color = Color(0xFFE8A090),
                )
                StatBox(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.project_status_paused),
                    value = "$pausedProjects",
                    color = Color(0xFFFFD966),
                )
                StatBox(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.completed),
                    value = "$completedProjects",
                    color = Color(0xFFF4C2C2),
                )
            }

            if (totalPrice > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                StatBox(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.warehouse_value),
                    value = "€%.2f".format(totalPrice),
                    color = Color(0xFFC2E0C6),
                )
            }

            if (totalProjects > 0) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.projects_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hai $totalProjects progetti in totale. Continua così!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.no_data_yet),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.no_data_sub),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun StatBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
