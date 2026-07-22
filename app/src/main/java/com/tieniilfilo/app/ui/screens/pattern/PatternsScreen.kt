package com.tieniilfilo.app.ui.screens.pattern

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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.PictureAsPdf
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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.R
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternSourceType
import com.tieniilfilo.app.data.local.entity.PatternType
import com.tieniilfilo.app.ui.components.EmptyState
import com.tieniilfilo.app.ui.theme.pressAnimation
import com.tieniilfilo.app.ui.theme.staggerEnter

private val PdfColor = Color(0xFFEF5350)
private val ImageColor = Color(0xFFEC407A)
private val LinkColor = Color(0xFF42A5F5)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PatternsScreen(
    onPatternClick: (Long) -> Unit,
    onAddClick: () -> Unit = {},
    viewModel: PatternViewModel = hiltViewModel(),
) {
    val patterns by viewModel.allPatterns.collectAsState()
    var selectedType by remember { mutableStateOf<PatternType?>(null) }
    var showBookmarked by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var searchExpanded by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var sortMode by remember { mutableStateOf(0) } // 0=titolo, 1=tipo

    val searched = if (searchQuery.isBlank()) patterns else patterns.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.notes.contains(searchQuery, ignoreCase = true)
    }

    val filteredPatterns = when {
        showBookmarked -> searched.filter { it.isBookmarked }
        selectedType != null -> searched.filter { it.type == selectedType }
        else -> searched
    }.let { list ->
        when (sortMode) {
            0 -> list.sortedBy { it.title.lowercase() }
            1 -> list.sortedByDescending { it.id }
            else -> list
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.patterns_title)) },
                actions = {
                    Row(
                        modifier = Modifier.clickable { showSortMenu = true }.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Ordina", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Rounded.AutoStories,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                        DropdownMenuItem(text = { Text("Titolo A-Z") }, onClick = { sortMode = 0; showSortMenu = false })
                        DropdownMenuItem(text = { Text("Più recenti") }, onClick = { sortMode = 1; showSortMenu = false })
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
                placeholder = { Text("Cerca schemi...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            ) {}
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                FilterChip(
                    selected = !showBookmarked && selectedType == null,
                    onClick = { showBookmarked = false; selectedType = null },
                    label = { Text("Tutti") },
                    modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                )
                FilterChip(
                    selected = showBookmarked,
                    onClick = { showBookmarked = !showBookmarked; selectedType = null },
                    label = { Text("Preferiti") },
                    modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                )
                PatternType.entries.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = {
                            selectedType = if (selectedType == type) null else type
                            showBookmarked = false
                        },
                        label = { Text(type.toDisplayString()) },
                        modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                    )
                }
            }

            if (filteredPatterns.isEmpty()) {
                EmptyState(
                    icon = Icons.Rounded.AutoStories,
                    title = "Nessuno schema",
                    subtitle = "Aggiungi schemi e pattern per i tuoi progetti",
                    actionLabel = "Aggiungi schema",
                    onActionClick = onAddClick,
                    illustration = { com.tieniilfilo.app.ui.components.OpenBookIllustration() },
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(filteredPatterns, key = { _, it -> it.id }) { index, pattern ->
                        AnimatedVisibility(visible = true, enter = staggerEnter(index)) {
                            PatternListItem(
                                pattern = pattern,
                                onClick = { onPatternClick(pattern.id) },
                                onBookmark = { viewModel.toggleBookmark(pattern) },
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
fun PatternListItem(
    pattern: PatternEntity,
    onClick: () -> Unit,
    onBookmark: () -> Unit,
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
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = pattern.sourceType.toIcon(),
                    contentDescription = null,
                    tint = pattern.sourceType.toColor(),
                    modifier = Modifier.padding(end = 4.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pattern.title,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = pattern.type.toDisplayString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (pattern.notes.isNotEmpty()) {
                        Text(
                            text = pattern.notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                IconButton(onClick = onBookmark) {
                    Icon(
                        imageVector = if (pattern.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (pattern.isBookmarked) "Rimuovi preferito" else "Aggiungi preferito",
                        tint = if (pattern.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
    }
}

fun PatternType.toDisplayString(): String = when (this) {
    PatternType.AMIGURUMI -> "Amigurumi"
    PatternType.ABBIGLIAMENTO -> "Abbigliamento"
    PatternType.ACCESSORI -> "Accessori"
    PatternType.COPERTE -> "Coperte"
    PatternType.CASA -> "Casa"
    PatternType.ALTRO -> "Altro"
}

fun PatternSourceType.toIcon() = when (this) {
    PatternSourceType.PDF -> Icons.Rounded.PictureAsPdf
    PatternSourceType.IMAGE -> Icons.Rounded.Image
    PatternSourceType.LINK -> Icons.Rounded.Link
}

fun PatternSourceType.toColor() = when (this) {
    PatternSourceType.PDF -> PdfColor
    PatternSourceType.IMAGE -> ImageColor
    PatternSourceType.LINK -> LinkColor
}
