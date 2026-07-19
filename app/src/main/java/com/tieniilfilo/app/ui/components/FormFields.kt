package com.tieniilfilo.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        shape = MaterialTheme.shapes.small,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ChipSelector(
    label: String,
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    labelOf: (T) -> String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = option == selected,
                    onClick = { onSelect(option) },
                    label = { Text(labelOf(option)) },
                )
            }
        }
    }
}

@Composable
fun MultiColorPickerRow(
    colors: List<Int>,
    selected: List<Int>,
    onToggle: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var hue by remember { mutableFloatStateOf(0f) }
    var saturation by remember { mutableFloatStateOf(0.7f) }
    var value by remember { mutableFloatStateOf(0.85f) }
    val customColor = Color.hsv(hue = hue, saturation = saturation, value = value)
    val isCustomSelected = customColor.toArgb() in selected

    Column(modifier = modifier) {
        Text(text = "Colori", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        MultiFlowRowColors(colors = colors, selected = selected, onToggle = onToggle)

        // Colore personalizzato con 3 slider
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Personalizza", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(customColor)
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), CircleShape),
            )
            Column(modifier = Modifier.weight(1f)) {
                ColorSliderRow(label = "Tonalità", value = hue, onValueChange = { hue = it }, range = 0f..360f)
                ColorSliderRow(label = "Sat.", value = saturation, onValueChange = { saturation = it }, range = 0f..1f)
                ColorSliderRow(label = "Lum.", value = value, onValueChange = { value = it }, range = 0f..1f)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Button(
            onClick = { onToggle(customColor.toArgb()) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                if (isCustomSelected) Icons.Default.Close else Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isCustomSelected) "Rimuovi colore personalizzato" else "Aggiungi colore personalizzato")
        }

        if (selected.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Selezionati (tocca per rimuovere):",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                selected.forEach { hex ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(hex))
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape)
                            .clickable { onToggle(hex) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Rimuovi colore",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(36.dp))
        Slider(value = value, onValueChange = onValueChange, valueRange = range, modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MultiFlowRowColors(
    colors: List<Int>,
    selected: List<Int>,
    onToggle: (Int) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        colors.forEach { hex ->
            val isSelected = hex in selected
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(hex))
                    .then(
                        if (isSelected) {
                            Modifier.border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        } else {
                            Modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), CircleShape)
                        }
                    )
                    .clickable { onToggle(hex) },
                contentAlignment = Alignment.Center,
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

// Mantenuta per retrocompatibilità, usa MultiColorPickerRow per multi-selezione
@Composable
fun ColorPickerRow(
    colors: List<Int>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    MultiColorPickerRow(
        colors = colors,
        selected = if (selected != 0) listOf(selected) else emptyList(),
        onToggle = { onSelect(it) },
        modifier = modifier,
    )
}

val DefaultYarnColors = listOf(
    // Rossi
    0xFF8B0000.toInt(),      // Bordeaux
    0xFFDC143C.toInt(),      // Cremisi
    0xFFFF6F61.toInt(),      // Corallo
    0xFFFA8072.toInt(),      // Salmone
    // Arancioni
    0xFFFF8C00.toInt(),      // Mandarino
    0xFFFFDAB9.toInt(),      // Pesca
    0xFFE8835A.toInt(),      // Terracotta
    // Gialli
    0xFFE8B86C.toInt(),      // Senape
    0xFFD4A853.toInt(),      // Oro
    0xFFFFFDD0.toInt(),      // Crema
    0xFFFFFACD.toInt(),      // Giallo pastello
    // Verdi
    0xFF7BA37B.toInt(),      // Salvia
    0xFF2E8B57.toInt(),      // Smeraldo
    0xFF556B2F.toInt(),      // Oliva
    0xFF98FB98.toInt(),      // Menta
    // Blu
    0xFF2C3E50.toInt(),      // Navy
    0xFF5C8A9E.toInt(),      // Cielo
    0xFF008080.toInt(),      // Teal
    0xFFB0C4DE.toInt(),      // Pervinca
    // Viola
    0xFFB084CC.toInt(),      // Lavanda
    0xFF8B008B.toInt(),      // Prugna
    0xFF9B59B6.toInt(),      // Viola
    // Rosa
    0xFFE8A090.toInt(),      // Rosa antico
    0xFFFF1493.toInt(),      // Fucsia
    0xFFFFE4E1.toInt(),      // Blush
    // Neutri
    0xFF5C4033.toInt(),      // Cioccolato
    0xFFC0C0C0.toInt(),      // Grigio caldo
    0xFFF5E6D3.toInt(),      // Avorio
    0xFF2C2C2C.toInt(),      // Nero
)
