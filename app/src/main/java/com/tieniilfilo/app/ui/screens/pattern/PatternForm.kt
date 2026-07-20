package com.tieniilfilo.app.ui.screens.pattern

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternSourceType
import com.tieniilfilo.app.data.local.entity.PatternType
import com.tieniilfilo.app.ui.components.BottomSheetForm
import com.tieniilfilo.app.ui.components.ChipSelector
import com.tieniilfilo.app.ui.components.FormTextField
import com.tieniilfilo.app.ui.components.PhotoThumb
import com.tieniilfilo.app.util.PhotoStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternFormSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    initialPattern: PatternEntity? = null,
    onSave: (
        title: String,
        type: PatternType,
        sourceType: PatternSourceType,
        fileUri: String?,
        externalLink: String?,
        notes: String,
    ) -> Unit,
    onUpdate: ((PatternEntity) -> Unit)? = null,
) {
    if (!isVisible) return

    val isEditing = initialPattern != null
    val formKey = initialPattern?.id ?: 0L

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val photoStorage = remember { PhotoStorage(context.applicationContext) }

    var title by remember(formKey) { mutableStateOf(initialPattern?.title ?: "") }
    var type by remember(formKey) { mutableStateOf(initialPattern?.type ?: PatternType.AMIGURUMI) }
    var sourceType by remember(formKey) { mutableStateOf(initialPattern?.sourceType ?: PatternSourceType.LINK) }
    var link by remember(formKey) { mutableStateOf(if (initialPattern?.sourceType == PatternSourceType.LINK) initialPattern?.externalLink ?: "" else "") }
    var notes by remember(formKey) { mutableStateOf(initialPattern?.notes ?: "") }
    var fileUri by remember(formKey) { mutableStateOf(initialPattern?.fileUri) }

    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            fileUri = withContext(Dispatchers.IO) { photoStorage.copyFromUri(uri) }
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            fileUri = withContext(Dispatchers.IO) { photoStorage.copyFromUri(uri) }
        }
    }

    var cameraPhotoPath by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success: Boolean ->
        if (success && cameraPhotoPath != null) {
            fileUri = cameraPhotoPath
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted: Boolean ->
        if (granted) {
            val imagesDir = File(context.filesDir, "images").also { it.mkdirs() }
            val tempFile = File(imagesDir, "camera_${UUID.randomUUID()}.jpg")
            cameraPhotoPath = tempFile.absolutePath
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
            cameraLauncher.launch(uri)
        }
    }

    fun launchCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val imagesDir = File(context.filesDir, "images").also { it.mkdirs() }
            val tempFile = File(imagesDir, "camera_${UUID.randomUUID()}.jpg")
            cameraPhotoPath = tempFile.absolutePath
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val dialogTitle = if (isEditing) "Modifica schema" else "Nuovo schema"
    val confirmLabel = if (isEditing) "Aggiorna schema" else "Salva schema"

    BottomSheetForm(
        title = dialogTitle,
        isVisible = true,
        onDismiss = onDismiss,
        onConfirm = {
            if (title.isBlank()) return@BottomSheetForm
            if (isEditing && onUpdate != null) {
                onUpdate(
                    initialPattern!!.copy(
                        title = title.trim(),
                        type = type,
                        sourceType = sourceType,
                        fileUri = fileUri,
                        externalLink = link.trim().ifBlank { null },
                        notes = notes.trim(),
                    )
                )
            } else {
                onSave(
                    title.trim(),
                    type,
                    sourceType,
                    fileUri,
                    link.trim().ifBlank { null },
                    notes.trim(),
                )
            }
            onDismiss()
        },
        confirmLabel = confirmLabel,
    ) {
        FormTextField(value = title, onValueChange = { title = it }, label = "Titolo *")
        Spacer(modifier = Modifier.height(12.dp))
        ChipSelector(
            label = "Tipo",
            options = PatternType.entries,
            selected = type,
            onSelect = { type = it },
            labelOf = { it.toDisplayString() },
        )
        Spacer(modifier = Modifier.height(12.dp))
        ChipSelector(
            label = "Sorgente",
            options = PatternSourceType.entries,
            selected = sourceType,
            onSelect = { sourceType = it; fileUri = null; link = "" },
            labelOf = {
                when (it) {
                    PatternSourceType.PDF -> "PDF"
                    PatternSourceType.IMAGE -> "Immagine"
                    PatternSourceType.LINK -> "Link"
                }
            },
        )
        if (sourceType == PatternSourceType.LINK) {
            Spacer(modifier = Modifier.height(8.dp))
            FormTextField(value = link, onValueChange = { link = it }, label = "URL (Ravelry, blog...)")
        }
        if (sourceType == PatternSourceType.PDF) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { pdfPicker.launch(arrayOf("application/pdf")) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (fileUri == null) "Seleziona PDF" else "Cambia PDF")
            }
        }
        if (sourceType == PatternSourceType.IMAGE) {
            Spacer(modifier = Modifier.height(8.dp))
            if (fileUri != null) {
                PhotoThumb(path = fileUri, size = 96.dp)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = {
                        imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Galleria")
                }
                OutlinedButton(
                    onClick = { launchCamera() },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Fotocamera")
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        FormTextField(value = notes, onValueChange = { notes = it }, label = "Note", singleLine = false)
    }
}
