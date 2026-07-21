package com.tieniilfilo.app.ui.screens.yarn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.drawBehind
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.data.local.entity.YarnComposition
import com.tieniilfilo.app.data.local.entity.YarnEntity
import com.tieniilfilo.app.data.local.entity.YarnSource
import com.tieniilfilo.app.ui.components.FullScreenImageViewer
import com.tieniilfilo.app.ui.components.GradientSlider
import com.tieniilfilo.app.ui.components.PhotoThumb
import com.tieniilfilo.app.ui.components.StatusChip
import com.tieniilfilo.app.ui.components.StitchDivider
import com.tieniilfilo.app.ui.theme.FrauncesFontFamily
import com.tieniilfilo.app.ui.theme.HeroCoral
import com.tieniilfilo.app.ui.theme.HeroAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnDetailScreen(
    yarnId: Long,
    onBack: () -> Unit,
    onEdit: (YarnEntity) -> Unit = {},
    onDeleteWithUndo: (message: String, undo: () -> Unit) -> Unit = { _, _ -> },
    viewModel: YarnViewModel = hiltViewModel(),
) {
    val yarn by viewModel.observeYarn(yarnId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }
    var viewerUri by remember { mutableStateOf<String?>(null) }
    var sliderValue by remember(yarn?.id, yarn?.quantityUsed) {
        mutableFloatStateOf(yarn?.quantityUsed?.toFloat() ?: 0f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(yarn?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    yarn?.let { y ->
                        IconButton(onClick = { onEdit(y) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifica filato")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Elimina filato", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                },
            )
        },
    ) { padding ->
        yarn?.let { y ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                HeroYarnHeader(yarn = y, y.photoUri, onViewPhoto = { viewerUri = y.photoUri }, onDeletePhoto = { viewModel.deletePhoto(y) })
                StitchDivider(modifier = Modifier.padding(vertical = 4.dp))

                if (y.brand.isNotEmpty()) {
                    Text(
                        text = y.brand,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (y.colorName.isNotEmpty()) {
                    Text(text = "Colore: ${y.colorName}", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))
                val compDisplay = if (y.composition in setOf(YarnComposition.MISTO, YarnComposition.ALTRO) && !y.customComposition.isNullOrBlank()) {
                    "${y.composition.toDisplayString()}: ${y.customComposition}"
                } else {
                    y.composition.toDisplayString()
                }
                DetailRow(label = "Composizione", value = compDisplay)

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Quantità", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                val qtyParts = mutableListOf<String>()
                if (y.quantityBallsTotal > 0) qtyParts.add("${y.quantityBallsTotal.toInt()} gomitoli")
                if (y.quantityGramsTotal > 0) qtyParts.add("${y.quantityGramsTotal.toInt()} g")
                if (y.quantityMetersTotal > 0) qtyParts.add("${y.quantityMetersTotal.toInt()} m")
                Text(text = qtyParts.joinToString(" · "), style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Fonte", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (y.yarnSource == YarnSource.NEGOZIO_FISICO) {
                        Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (y.storeName.isNotBlank()) y.storeName else "Negozio fisico", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (y.storeLink.isNotBlank()) y.storeLink else "Online", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Quantità usata", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${"%.0f".format(sliderValue)}%",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        GradientSlider(
                            value = sliderValue.coerceIn(0f, 100f) / 100f,
                            onValueChange = { sliderValue = it * 100f },
                            onValueChangeFinished = {
                                viewModel.updateQuantityUsed(y, sliderValue.toDouble())
                            },
                        )
                    }
                }
                StitchDivider(modifier = Modifier.padding(vertical = 4.dp))

                if (y.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        ),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Note", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = y.notes, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        } ?: Text(
            text = "Filato non trovato",
            modifier = Modifier.padding(padding),
        )
    }

        viewerUri?.let { FullScreenImageViewer(path = it, onDismiss = { viewerUri = null }) }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Elimina filato") },
                text = { Text("Eliminare ${yarn?.name ?: ""}? L'operazione non può essere annullata.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        val item = yarn
                        if (item != null) {
                            viewModel.deleteYarn(item)
                            onDeleteWithUndo("${item.name} eliminato") { viewModel.undoDeleteYarn() }
                        }
                        onBack()
                    }) {
                        Text("Elimina", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Annulla")
                    }
                },
            )
        }
    }
}

@Composable
private fun HeroYarnHeader(
    yarn: com.tieniilfilo.app.data.local.entity.YarnEntity,
    photoUri: String?,
    onViewPhoto: () -> Unit,
    onDeletePhoto: () -> Unit,
) {
    val colors = parseColorHexes(yarn.colorHexes, yarn.colorHex)
    val gradientColors = colors.map { Color(it) }.ifEmpty { listOf(HeroCoral) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .drawBehind {
                drawRect(
                    brush = if (gradientColors.size >= 2) {
                        androidx.compose.ui.graphics.Brush.horizontalGradient(gradientColors)
                    } else {
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(gradientColors[0], gradientColors[0].copy(alpha = 0.5f))
                        )
                    },
                )
            }
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val hasPhoto = !photoUri.isNullOrBlank()
            if (hasPhoto) {
                Box {
                    PhotoThumb(path = photoUri, size = 120.dp, onClick = onViewPhoto)
                    IconButton(
                        onClick = onDeletePhoto,
                        modifier = Modifier.align(Alignment.TopEnd).size(24.dp),
                    ) {
                        Box(
                            modifier = Modifier.size(22.dp).background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.45f), androidx.compose.foundation.shape.CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(androidx.compose.material.icons.Icons.Default.Close, contentDescription = "Rimuovi foto", tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                }
                androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = yarn.name,
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium.copy(fontFamily = FrauncesFontFamily),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                )
                if (yarn.brand.isNotEmpty()) {
                    Text(
                        text = yarn.brand,
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }
                androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                if (colors.isNotEmpty()) {
                    Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy((-6).dp)) {
                        colors.take(5).forEach { hex ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(Color(hex))
                                    .border(2.dp, androidx.compose.material3.MaterialTheme.colorScheme.surface, androidx.compose.foundation.shape.CircleShape),
                            )
                        }
                    }
                }
            }
        }
    }
    }

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}
