package com.tieniilfilo.app.ui.screens.pattern

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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternSourceType
import com.tieniilfilo.app.data.local.entity.PatternType
import com.tieniilfilo.app.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PatternsScreen(
    onPatternClick: (Long) -> Unit,
    viewModel: PatternViewModel = hiltViewModel(),
) {
    val patterns by viewModel.allPatterns.collectAsState()
    var selectedType by remember { mutableStateOf<PatternType?>(null) }
    var showBookmarked by remember { mutableStateOf(false) }

    val filteredPatterns = when {
        showBookmarked -> patterns.filter { it.isBookmarked }
        selectedType != null -> patterns.filter { it.type == selectedType }
        else -> patterns
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Schemi") })
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
                    icon = Icons.AutoMirrored.Filled.LibraryBooks,
                    title = "Nessuno schema",
                    subtitle = "Aggiungi schemi e pattern per i tuoi progetti",
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(filteredPatterns, key = { it.id }) { pattern ->
                        PatternListItem(
                            pattern = pattern,
                            onClick = { onPatternClick(pattern.id) },
                            onBookmark = { viewModel.toggleBookmark(pattern) },
                        )
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(onClick = onClick),
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
                tint = MaterialTheme.colorScheme.primary,
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
    PatternSourceType.PDF -> Icons.Default.PictureAsPdf
    PatternSourceType.IMAGE -> Icons.AutoMirrored.Filled.LibraryBooks
    PatternSourceType.LINK -> Icons.Default.Link
}
