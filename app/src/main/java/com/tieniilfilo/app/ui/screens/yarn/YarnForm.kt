package com.tieniilfilo.app.ui.screens.yarn

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.tieniilfilo.app.data.local.entity.YarnComposition
import com.tieniilfilo.app.data.local.entity.YarnSource
import com.tieniilfilo.app.data.local.entity.YarnEntity
import com.tieniilfilo.app.R
import com.tieniilfilo.app.ui.components.BottomSheetForm
import com.tieniilfilo.app.ui.components.ChipSelector
import com.tieniilfilo.app.ui.components.DefaultYarnColors
import com.tieniilfilo.app.ui.components.FormTextField
import com.tieniilfilo.app.ui.components.MultiColorPickerRow
import com.tieniilfilo.app.ui.components.PhotoThumb
import com.tieniilfilo.app.util.PhotoStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import androidx.compose.ui.res.stringResource

private val gson = Gson()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnFormSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    initialYarn: YarnEntity? = null,
    onSave: (
        name: String,
        brand: String,
        colorName: String,
        colorHex: Int,
        colorHexes: String?,
        composition: YarnComposition,
        customComposition: String?,
        quantityBallsTotal: Double,
        quantityGramsTotal: Double,
        quantityMetersTotal: Double,
        yarnSource: YarnSource,
        storeName: String,
        storeLink: String,
        notes: String,
        photoUri: String?,
    ) -> Unit,
    onUpdate: ((YarnEntity) -> Unit)? = null,
) {
    if (!isVisible) return

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val photoStorage = remember { PhotoStorage(context.applicationContext) }
    val isEditing = initialYarn != null
    val formKey = initialYarn?.id ?: 0L

    val initialColors = if (initialYarn != null) {
        val colors = parseColorHexes(initialYarn.colorHexes, initialYarn.colorHex)
        if (colors.isNotEmpty()) colors else emptyList()
    } else {
        emptyList()
    }

    var name by remember(formKey) { mutableStateOf(initialYarn?.name ?: "") }
    var brand by remember(formKey) { mutableStateOf(initialYarn?.brand ?: "") }
    var colorName by remember(formKey) { mutableStateOf(initialYarn?.colorName ?: "") }
    var selectedColors by remember(formKey) { mutableStateOf(initialColors) }
    var composition by remember(formKey) { mutableStateOf(initialYarn?.composition ?: YarnComposition.COTONE) }
    var customComposition by remember(formKey) { mutableStateOf(initialYarn?.customComposition ?: "") }
    var quantityBalls by remember(formKey) {
        mutableStateOf(
            initialYarn?.quantityBallsTotal?.takeIf { it > 0 }?.let {
                if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString()
            } ?: ""
        )
    }
    var quantityGrams by remember(formKey) {
        mutableStateOf(
            initialYarn?.quantityGramsTotal?.takeIf { it > 0 }?.let {
                if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString()
            } ?: ""
        )
    }
    var quantityMeters by remember(formKey) {
        mutableStateOf(
            initialYarn?.quantityMetersTotal?.takeIf { it > 0 }?.let {
                if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString()
            } ?: ""
        )
    }
    var yarnSource by remember(formKey) { mutableStateOf(initialYarn?.yarnSource ?: YarnSource.NEGOZIO_FISICO) }
    var storeName by remember(formKey) { mutableStateOf(initialYarn?.storeName ?: "") }
    var storeLink by remember(formKey) { mutableStateOf(initialYarn?.storeLink ?: "") }
    var unitPrice by remember(formKey) { mutableStateOf(initialYarn?.unitPrice?.let { "%.2f".format(it).trimEnd('0').trimEnd('.') } ?: "") }
    var isWishlist by remember(formKey) { mutableStateOf(initialYarn?.isWishlist ?: false) }
    var notes by remember(formKey) { mutableStateOf(initialYarn?.notes ?: "") }
    var photoPath by remember(formKey) { mutableStateOf(initialYarn?.photoUri) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            photoPath = withContext(Dispatchers.IO) { photoStorage.copyFromUri(uri) }
        }
    }

    var cameraPhotoPath by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success: Boolean ->
        if (success && cameraPhotoPath != null) {
            photoPath = cameraPhotoPath
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

    val title = if (isEditing) stringResource(R.string.edit_yarn) else stringResource(R.string.new_yarn)
    val confirmLabel = if (isEditing) stringResource(R.string.update_yarn) else stringResource(R.string.save_yarn)

    BottomSheetForm(
        title = title,
        isVisible = true,
        onDismiss = onDismiss,
        onConfirm = {
            if (name.isBlank()) return@BottomSheetForm
            val colorHexesJson = if (selectedColors.size > 1) gson.toJson(selectedColors) else null
            val customComp = if (composition in setOf(YarnComposition.MISTO, YarnComposition.ALTRO) && customComposition.isNotBlank()) customComposition.trim() else null

            if (isEditing && onUpdate != null) {
                onUpdate(
                    initialYarn!!.copy(
                        name = name.trim(),
                        brand = brand.trim(),
                        colorName = colorName.trim(),
                        colorHex = selectedColors.firstOrNull() ?: 0,
                        colorHexes = colorHexesJson,
                        composition = composition,
                        customComposition = customComp,
                        quantityBallsTotal = quantityBalls.toDoubleOrNull() ?: 0.0,
                        quantityGramsTotal = quantityGrams.toDoubleOrNull() ?: 0.0,
                        quantityMetersTotal = quantityMeters.toDoubleOrNull() ?: 0.0,
                        unitPrice = unitPrice.toDoubleOrNull(),

                        yarnSource = yarnSource,
                        storeName = storeName.trim(),
                        storeLink = storeLink.trim(),
                        isWishlist = isWishlist,
                        notes = notes.trim(),
                        photoUri = photoPath,
                    )
                )
            } else {
                onSave(
                    name.trim(),
                    brand.trim(),
                    colorName.trim(),
                    selectedColors.firstOrNull() ?: 0,
                    colorHexesJson,
                    composition,
                    customComp,
                    quantityBalls.toDoubleOrNull() ?: 0.0,
                    quantityGrams.toDoubleOrNull() ?: 0.0,
                    quantityMeters.toDoubleOrNull() ?: 0.0,
                    yarnSource,
                    storeName.trim(),
                    storeLink.trim(),
                    notes.trim(),
                    photoPath,
                )
            }
            onDismiss()
        },
        confirmLabel = confirmLabel,
    ) {
        FormTextField(value = name, onValueChange = { name = it }, label = stringResource(R.string.name_required))
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(value = brand, onValueChange = { brand = it }, label = stringResource(R.string.brand))
        Spacer(modifier = Modifier.height(8.dp))
        FormTextField(value = colorName, onValueChange = { colorName = it }, label = stringResource(R.string.color_name))
        Spacer(modifier = Modifier.height(12.dp))
        MultiColorPickerRow(
            colors = DefaultYarnColors,
            selected = selectedColors,
            onToggle = { hex ->
                selectedColors = if (hex in selectedColors) {
                    selectedColors - hex
                } else {
                    selectedColors + hex
                }
            },
        )
        Spacer(modifier = Modifier.height(12.dp))

        ChipSelector(
            label = stringResource(R.string.composition_label),
            options = YarnComposition.entries,
            selected = composition,
            onSelect = { composition = it },
            labelOf = { it.toDisplayString() },
        )

        if (composition in setOf(YarnComposition.MISTO, YarnComposition.ALTRO)) {
            Spacer(modifier = Modifier.height(6.dp))
            FormTextField(
                value = customComposition,
                onValueChange = { customComposition = it },
                label = "Specifica composizione (es. 50% Cotone, 50% Acrilico)",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Quantità", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(6.dp))
        FormTextField(value = quantityBalls, onValueChange = { quantityBalls = it.filter { c -> c.isDigit() || c == '.' } }, label = stringResource(R.string.balls), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        Spacer(modifier = Modifier.height(6.dp))
        FormTextField(value = quantityGrams, onValueChange = { quantityGrams = it.filter { c -> c.isDigit() || c == '.' } }, label = stringResource(R.string.grams), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        Spacer(modifier = Modifier.height(6.dp))
        FormTextField(value = quantityMeters, onValueChange = { quantityMeters = it.filter { c -> c.isDigit() || c == '.' } }, label = stringResource(R.string.meters), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))

        Spacer(modifier = Modifier.height(16.dp))
        FormTextField(value = unitPrice, onValueChange = { unitPrice = it.filter { c -> c.isDigit() || c == '.' } }, label = "Prezzo unitario (€)", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(R.string.source), style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = yarnSource == YarnSource.NEGOZIO_FISICO,
                onClick = { yarnSource = YarnSource.NEGOZIO_FISICO },
                label = { Text(stringResource(R.string.physical_store)) },
                leadingIcon = { Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(18.dp)) },
            )
            FilterChip(
                selected = yarnSource == YarnSource.ONLINE,
                onClick = { yarnSource = YarnSource.ONLINE },
                label = { Text(stringResource(R.string.online)) },
                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(18.dp)) },
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        if (yarnSource == YarnSource.NEGOZIO_FISICO) {
            FormTextField(value = storeName, onValueChange = { storeName = it }, label = stringResource(R.string.store_name))
        } else {
            FormTextField(value = storeLink, onValueChange = { storeLink = it }, label = stringResource(R.string.store_link))
        }

        Spacer(modifier = Modifier.height(12.dp))
        androidx.compose.material3.FilterChip(
            selected = isWishlist,
            onClick = { isWishlist = !isWishlist },
            label = { Text("Lista desideri") },
            leadingIcon = {
                Icon(
                    if (isWishlist) androidx.compose.material.icons.Icons.Default.Favorite else androidx.compose.material.icons.Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            },
        )

        Spacer(modifier = Modifier.height(12.dp))
        FormTextField(value = notes, onValueChange = { notes = it }, label = stringResource(R.string.notes), singleLine = false)
        Spacer(modifier = Modifier.height(12.dp))
        if (photoPath != null) {
            PhotoThumb(path = photoPath, size = 96.dp)
            Spacer(modifier = Modifier.height(8.dp))
        }
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
                Text(stringResource(R.string.edit_photo))
            }
            OutlinedButton(
                onClick = { launchCamera() },
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.camera))
            }
            if (photoPath != null) {
                OutlinedButton(
                    onClick = { photoPath = null },
                    modifier = Modifier.weight(0.7f),
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.remove_photo_btn))
                }
            }
        }
    }
}
