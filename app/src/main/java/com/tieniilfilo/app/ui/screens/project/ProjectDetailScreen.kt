package com.tieniilfilo.app.ui.screens.project

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.ProjectPhotoEntity
import com.tieniilfilo.app.data.local.entity.ProjectStatus
import com.tieniilfilo.app.ui.components.ConfettiOverlay
import com.tieniilfilo.app.ui.components.PhotoThumb
import com.tieniilfilo.app.ui.components.StatusChip
import com.tieniilfilo.app.util.PhotoStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Long,
    onBack: () -> Unit,
    onPatternClick: (Long) -> Unit = {},
    viewModel: ProjectViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val photoStorage = remember { PhotoStorage(context) }

    val project by viewModel.observeProject(projectId).collectAsState(initial = null)
    var photos by remember { mutableStateOf<List<ProjectPhotoEntity>>(emptyList()) }
    var showConfetti by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val view = LocalView.current

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val path = withContext(Dispatchers.IO) { photoStorage.copyFromUri(uri) }
            if (path != null) {
                viewModel.addPhoto(projectId, path)
                photos = viewModel.getPhotos(projectId)
            }
        }
    }

    var cameraPhotoPath by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success: Boolean ->
        if (success && cameraPhotoPath != null) {
            scope.launch {
                viewModel.addPhoto(projectId, cameraPhotoPath!!)
                photos = viewModel.getPhotos(projectId)
            }
        }
    }

    fun launchCameraInternal() {
        val imagesDir = context.filesDir.resolve("images").also { it.mkdirs() }
        val tempFile = java.io.File(imagesDir, "camera_${java.util.UUID.randomUUID()}.jpg")
        cameraPhotoPath = tempFile.absolutePath
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
        cameraLauncher.launch(uri)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted: Boolean ->
        if (granted) {
            launchCameraInternal()
        }
    }

    fun launchCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCameraInternal()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var linkedPattern by remember { mutableStateOf<PatternEntity?>(null) }

    LaunchedEffect(projectId) {
        photos = viewModel.getPhotos(projectId)
    }

    val currentProject = project

    LaunchedEffect(currentProject?.patternId) {
        val pid = currentProject?.patternId
        linkedPattern = if (pid != null) viewModel.getPatternById(pid) else null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(project?.name ?: "") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Elimina progetto", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                )
            },
        ) { padding ->
            project?.let { proj ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                ) {
                    StatusChip(
                        label = proj.status.toDisplayString(),
                        chipColor = proj.status.toChipColor(),
                    )

                    if (proj.startDate != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Iniziato: ${formatDate(proj.startDate)}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    if (proj.endDate != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Celebration, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Completato: ${formatDate(proj.endDate)}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    if (linkedPattern != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPatternClick(linkedPattern!!.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            ),
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                            ) {
                                Text(text = "Schema collegato", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = linkedPattern!!.title, style = MaterialTheme.typography.bodyMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Tocca per aprire →", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    if (proj.notes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            ),
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Note", style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = proj.notes, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Foto progresso", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = {
                                picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Galleria", style = MaterialTheme.typography.labelSmall)
                        }
                        OutlinedButton(
                            onClick = { launchCamera() },
                            modifier = Modifier.weight(1f),
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Fotocamera", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    if (photos.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(photos) { photo ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    PhotoThumb(path = photo.photoUri, size = 120.dp)
                                    Text(
                                        text = formatDate(photo.takenAt),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 4.dp),
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ancora nessuna foto. Aggiungine una per tenere traccia del progresso!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    if (proj.status != ProjectStatus.COMPLETATO) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                                viewModel.completeProject(proj)
                                showConfetti = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(Icons.Default.Celebration, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Segna come completato")
                        }
                    }
                }
            } ?: Text(
                text = "Progetto non trovato",
                modifier = Modifier.padding(padding),
            )
        }

        ConfettiOverlay(
            active = showConfetti,
            onFinished = { showConfetti = false },
        )

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Elimina progetto") },
                text = { Text("Eliminare ${project?.name ?: ""}? L'operazione non può essere annullata.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        project?.let { viewModel.deleteProject(it) }
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
