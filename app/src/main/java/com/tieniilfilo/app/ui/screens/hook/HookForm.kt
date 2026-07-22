package com.tieniilfilo.app.ui.screens.hook

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tieniilfilo.app.data.local.entity.HookMaterial
import com.tieniilfilo.app.ui.components.BottomSheetForm
import com.tieniilfilo.app.ui.components.ChipSelector
import com.tieniilfilo.app.ui.components.FormTextField
import com.tieniilfilo.app.R
import androidx.compose.ui.res.stringResource
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HookFormSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSave: (sizeMm: Double, material: HookMaterial, brand: String) -> Unit,
) {
    if (!isVisible) return

    var sizeMm by remember { mutableFloatStateOf(4f) }
    var material by remember { mutableStateOf(HookMaterial.METALLO) }
    var brand by remember { mutableStateOf("") }

    BottomSheetForm(
        title = stringResource(R.string.new_hook),
        isVisible = true,
        onDismiss = onDismiss,
        onConfirm = {
            onSave((sizeMm * 2).roundToInt() / 2.0, material, brand.trim())
            onDismiss()
        },
        confirmLabel = stringResource(R.string.save_hook),
    ) {
        Text(text = "Misura: ${"%.1f".format(sizeMm)} mm")
        Slider(
            value = sizeMm,
            onValueChange = { sizeMm = it },
            valueRange = 1.5f..12f,
            steps = 20,
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChipSelector(
            label = stringResource(R.string.material),
            options = HookMaterial.entries,
            selected = material,
            onSelect = { material = it },
            labelOf = { it.toDisplayString() },
        )
        Spacer(modifier = Modifier.height(12.dp))
        FormTextField(value = brand, onValueChange = { brand = it }, label = "Marca")
    }
}
