package com.tieniilfilo.app.ui.screens.pattern

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternSourceType
import com.tieniilfilo.app.ui.components.FullScreenImageViewer
import com.tieniilfilo.app.ui.components.PhotoThumb
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternDetailScreen(
    patternId: Long,
    onBack: () -> Unit,
    onEdit: (PatternEntity) -> Unit = {},
    onDeleteWithUndo: (message: String, undo: () -> Unit) -> Unit = { _, _ -> },
    viewModel: PatternViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val pattern by viewModel.observePattern(patternId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }
    var viewerUri by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pattern?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    pattern?.let { pat ->
                        IconButton(onClick = { onEdit(pat) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifica schema")
                        }
                        IconButton(onClick = { viewModel.toggleBookmark(pat) }) {
                            Icon(
                                imageVector = if (pat.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = if (pat.isBookmarked) "Rimuovi preferito" else "Aggiungi preferito",
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Elimina schema", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                },
            )
        },
    ) { padding ->
        pattern?.let { pat ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                Text(
                    text = pat.type.toDisplayString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = pat.sourceType.toIcon(),
                        contentDescription = null,
                        tint = pat.sourceType.toColor(),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (pat.sourceType) {
                            PatternSourceType.PDF -> "Documento PDF"
                            PatternSourceType.IMAGE -> "Immagine"
                            PatternSourceType.LINK -> "Link esterno"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .drawBehind {
                            val heroColor = pat.sourceType.toColor()
                            drawRoundRect(heroColor.copy(alpha = 0.4f))
                            drawRoundRect(heroColor.copy(alpha = 0.15f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f))
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = pat.title, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
                }

                if (pat.sourceType == PatternSourceType.IMAGE && !pat.fileUri.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box {
                        PhotoThumb(path = pat.fileUri, size = 200.dp, onClick = { viewerUri = pat.fileUri })
                        IconButton(
                            onClick = { viewModel.deleteFile(pat) },
                            modifier = Modifier.align(Alignment.TopEnd),
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Rimuovi immagine",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }

                if (pat.sourceType == PatternSourceType.PDF && !pat.fileUri.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val file = File(pat.fileUri)
                            if (file.exists()) {
                                val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(contentUri, "application/pdf")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Apri PDF")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { viewModel.deleteFile(pat) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rimuovi PDF")
                    }
                }

                if (pat.externalLink != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pat.externalLink))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                    ) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Apri link")
                    }
                }

                if (pat.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        ),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Note", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pat.notes,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        } ?: Text(
            text = "Schema non trovato",
            modifier = Modifier.padding(padding),
        )
    }

    viewerUri?.let { FullScreenImageViewer(path = it, onDismiss = { viewerUri = null }) }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Elimina schema") },
            text = { Text("Eliminare ${pattern?.title ?: ""}? L'operazione non può essere annullata.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    val item = pattern
                    if (item != null) {
                        viewModel.deletePattern(item)
                        onDeleteWithUndo("${item.title} eliminato") { viewModel.undoDeletePattern() }
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
