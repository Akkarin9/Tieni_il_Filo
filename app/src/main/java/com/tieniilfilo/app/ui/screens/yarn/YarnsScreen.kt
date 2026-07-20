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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.data.local.entity.YarnComposition
import com.tieniilfilo.app.data.local.entity.YarnEntity
import com.tieniilfilo.app.data.local.entity.YarnStatus
import com.tieniilfilo.app.ui.components.ChipColor
import com.tieniilfilo.app.ui.components.EmptyState
import com.tieniilfilo.app.ui.components.FullScreenImageViewer
import com.tieniilfilo.app.ui.components.PhotoThumb
import com.tieniilfilo.app.ui.components.StatusChip
import com.tieniilfilo.app.ui.theme.AppIcons
import com.tieniilfilo.app.ui.theme.pressAnimation
import com.tieniilfilo.app.ui.theme.staggerEnter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun YarnsScreen(
    onYarnClick: (Long) -> Unit,
    onNavigateToHooks: () -> Unit,
    onAddClick: () -> Unit = {},
    viewModel: YarnViewModel = hiltViewModel(),
) {
    val filter by viewModel.filter.collectAsState()
    val yarns by viewModel.filteredYarns.collectAsState()
    var searchExpanded by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }
    var viewerUri by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filati") },
                actions = {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onNavigateToHooks)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(AppIcons.CrochetHook, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Uncinetti", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
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
                    actionLabel = "Aggiungi filato",
                    onActionClick = onAddClick,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(yarns, key = { _, it -> it.id }) { index, yarn ->
                        AnimatedVisibility(visible = true, enter = staggerEnter(index)) {
                            YarnListItem(
                                yarn = yarn,
                                onClick = { onYarnClick(yarn.id) },
                                onPhotoClick = { viewerUri = it },
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

        viewerUri?.let { FullScreenImageViewer(path = it, onDismiss = { viewerUri = null }) }
    }
}

@Composable
fun YarnListItem(
    yarn: YarnEntity,
    onClick: () -> Unit,
    onPhotoClick: (String) -> Unit = {},
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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val listColors = parseColorHexes(yarn.colorHexes, yarn.colorHex)
            if (!yarn.photoUri.isNullOrBlank()) {
                Box(modifier = Modifier.size(48.dp)) {
                    PhotoThumb(path = yarn.photoUri, size = 48.dp, onClick = { onPhotoClick(yarn.photoUri) })
                    if (listColors.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 2.dp, y = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy((-4).dp),
                        ) {
                            listColors.take(3).forEach { hex ->
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex))
                                        .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                if (listColors.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                        listColors.take(3).forEach { hex ->
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
