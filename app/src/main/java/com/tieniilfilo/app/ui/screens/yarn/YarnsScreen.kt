package com.tieniilfilo.app.ui.screens.yarn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.tieniilfilo.app.data.local.entity.YarnComposition
import com.tieniilfilo.app.data.local.entity.YarnEntity
import com.tieniilfilo.app.data.local.entity.YarnStatus
import com.tieniilfilo.app.ui.components.ChipColor
import com.tieniilfilo.app.ui.components.EmptyState
import com.tieniilfilo.app.ui.components.PhotoThumb
import com.tieniilfilo.app.ui.components.StatusChip

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun YarnsScreen(
    onYarnClick: (Long) -> Unit,
    onNavigateToHooks: () -> Unit,
    viewModel: YarnViewModel = hiltViewModel(),
) {
    val filter by viewModel.filter.collectAsState()
    val yarns by viewModel.filteredYarns.collectAsState()
    var searchExpanded by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filati") },
                actions = {
                    IconButton(onClick = onNavigateToHooks) {
                        Icon(Icons.Default.Spa, contentDescription = "Uncinetti")
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
            // Search bar
            SearchBar(
                query = filter.searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onSearch = { viewModel.updateSearchQuery(it) },
                active = searchExpanded,
                onActiveChange = { searchExpanded = it },
                placeholder = { Text("Cerca filati...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {}

            // Filter section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilterChip(
                    selected = showFilters,
                    onClick = { showFilters = !showFilters },
                    label = { Text("Filtri") },
                    leadingIcon = { Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp)) },
                )
                if (filter.isActive) {
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.clearFilters() },
                        label = { Text("Azzera") },
                        leadingIcon = { Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    )
                }
            }

            AnimatedVisibility(visible = showFilters) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Status filters
                    Text("Stato", style = MaterialTheme.typography.labelMedium)
                    FlowRow {
                        YarnStatus.entries.forEach { status ->
                            val selected = filter.status == status
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.setStatusFilter(status) },
                                label = { Text(status.toDisplayString()) },
                                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                            )
                        }
                    }
                    // Composition filters
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Composizione", style = MaterialTheme.typography.labelMedium)
                    FlowRow {
                        YarnComposition.entries.forEach { comp ->
                            val selected = filter.composition == comp
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.setCompositionFilter(comp) },
                                label = { Text(comp.toDisplayString()) },
                                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                            )
                        }
                    }
                }
            }

            if (yarns.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.FilterList,
                    title = "Nessun filato trovato",
                    subtitle = "Prova a cambiare i filtri o aggiungi un nuovo filato",
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(yarns, key = { it.id }) { yarn ->
                        YarnListItem(
                            yarn = yarn,
                            onClick = { onYarnClick(yarn.id) },
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun YarnListItem(
    yarn: YarnEntity,
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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!yarn.photoUri.isNullOrBlank()) {
                PhotoThumb(path = yarn.photoUri, size = 48.dp)
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                val colors = parseColorHexes(yarn.colorHexes, yarn.colorHex)
                if (colors.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                        colors.take(3).forEach { hex ->
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color(hex))
                                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = yarn.name,
                    style = MaterialTheme.typography.titleSmall,
                )
                if (yarn.brand.isNotEmpty()) {
                    Text(
                        text = yarn.brand,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (yarn.colorName.isNotEmpty()) {
                        Text(
                            text = yarn.colorName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    val compText = if (yarn.composition in setOf(YarnComposition.MISTO, YarnComposition.ALTRO) && !yarn.customComposition.isNullOrBlank()) {
                        "${yarn.composition.toDisplayString()}: ${yarn.customComposition}"
                    } else {
                        yarn.composition.toDisplayString()
                    }
                    Text(
                        text = compText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = buildQuantityLabel(yarn.quantityBallsTotal, yarn.quantityGramsTotal, yarn.quantityMetersTotal),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            StatusChip(
                label = yarn.status.toDisplayString(),
                chipColor = yarn.status.toChipColor(),
            )
        }
    }
}

fun buildQuantityLabel(balls: Double, grams: Double, meters: Double): String {
    val parts = mutableListOf<String>()
    if (balls > 0) parts.add("${balls.toInt()} gomitoli")
    if (grams > 0) parts.add("${grams.toInt()} g")
    if (meters > 0) parts.add("${meters.toInt()} m")
    return if (parts.isNotEmpty()) parts.joinToString(" · ") else "—"
}

fun YarnStatus.toDisplayString(): String = when (this) {
    YarnStatus.DISPONIBILE -> "Disponibile"
    YarnStatus.IN_USO -> "In uso"
    YarnStatus.ESAURITO -> "Esaurito"
}

fun YarnStatus.toChipColor(): ChipColor = when (this) {
    YarnStatus.DISPONIBILE -> ChipColor.GREEN
    YarnStatus.IN_USO -> ChipColor.LAVENDER
    YarnStatus.ESAURITO -> ChipColor.PINK
}

fun YarnComposition.toDisplayString(): String = when (this) {
    YarnComposition.LANA -> "Lana"
    YarnComposition.COTONE -> "Cotone"
    YarnComposition.MISTO -> "Misto"
    YarnComposition.ACRILICO -> "Acrilico"
    YarnComposition.ALPACA -> "Alpaca"
    YarnComposition.MERINO -> "Merino"
    YarnComposition.BAMBOO -> "Bamboo"
    YarnComposition.SETA -> "Seta"
    YarnComposition.LINO -> "Lino"
    YarnComposition.ALTRO -> "Altro"
}

fun parseColorHexes(json: String?, fallback: Int): List<Int> {
    if (!json.isNullOrBlank()) {
        try {
            val type = object : TypeToken<List<Int>>() {}.type
            val parsed: List<Int> = Gson().fromJson(json, type)
            if (parsed.isNotEmpty()) return parsed
        } catch (_: Exception) {}
    }
    return if (fallback != 0) listOf(fallback) else emptyList()
}
