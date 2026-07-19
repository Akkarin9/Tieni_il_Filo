package com.tieniilfilo.app.ui.screens.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.ProjectStatus
import com.tieniilfilo.app.ui.components.BottomSheetForm
import com.tieniilfilo.app.ui.components.ChipSelector
import com.tieniilfilo.app.ui.components.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectFormSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSave: (name: String, status: ProjectStatus, notes: String, patternId: Long?) -> Unit,
    allPatterns: List<PatternEntity> = emptyList(),
) {
    if (!isVisible) return

    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ProjectStatus.DA_INIZIARE) }
    var notes by remember { mutableStateOf("") }
    var selectedPatternId by remember { mutableStateOf<Long?>(null) }
    var showPatternPicker by remember { mutableStateOf(false) }
    val selectedPattern = allPatterns.find { it.id == selectedPatternId }

    BottomSheetForm(
        title = "Nuovo progetto",
        isVisible = true,
        onDismiss = onDismiss,
        onConfirm = {
            if (name.isBlank()) return@BottomSheetForm
            onSave(name.trim(), status, notes.trim(), selectedPatternId)
            onDismiss()
        },
        confirmLabel = "Salva progetto",
    ) {
        FormTextField(value = name, onValueChange = { name = it }, label = "Nome *")
        Spacer(modifier = Modifier.height(12.dp))
        ChipSelector(
            label = "Stato",
            options = ProjectStatus.entries,
            selected = status,
            onSelect = { status = it },
            labelOf = { it.toDisplayString() },
        )
        Spacer(modifier = Modifier.height(12.dp))
        FormTextField(value = notes, onValueChange = { notes = it }, label = "Note", singleLine = false)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Schema collegato", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPatternPicker = true }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (selectedPattern != null) selectedPattern.title else "Nessuno schema",
                style = MaterialTheme.typography.bodyMedium,
                color = if (selectedPattern != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            if (selectedPattern != null) {
                IconButton(onClick = { selectedPatternId = null; showPatternPicker = false }) {
                    Icon(Icons.Default.Close, contentDescription = "Rimuovi schema", modifier = Modifier.size(18.dp))
                }
            }
        }
    }

    if (showPatternPicker) {
        AlertDialog(
            onDismissRequest = { showPatternPicker = false },
            title = { Text("Seleziona schema") },
            text = {
                if (allPatterns.isEmpty()) {
                    Text("Nessuno schema disponibile. Creane uno prima.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Column {
                        allPatterns.forEach { pattern ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPatternId = pattern.id
                                        showPatternPicker = false
                                    }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = pattern.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (pattern.id == selectedPatternId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPatternPicker = false }) {
                    Text("Chiudi")
                }
            },
        )
    }
}
