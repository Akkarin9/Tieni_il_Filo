package com.tieniilfilo.app.ui.screens.hook

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.Handyman
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tieniilfilo.app.R
import com.tieniilfilo.app.data.local.entity.HookEntity
import com.tieniilfilo.app.data.local.entity.HookMaterial
import com.tieniilfilo.app.ui.components.EmptyState
import com.tieniilfilo.app.util.toUkHookSize
import com.tieniilfilo.app.util.toUsHookSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HooksScreen(
    onAddClick: () -> Unit = {},
    viewModel: HookViewModel = hiltViewModel(),
) {
    val hooks by viewModel.hooks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.hooks_title)) })
        },
    ) { padding ->
        if (hooks.isEmpty()) {
            EmptyState(
                modifier = Modifier.padding(padding),
                icon = Icons.Rounded.Handyman,
                title = "Nessun uncinetto",
                subtitle = "Aggiungi il tuo primo uncinetto per iniziare",
                actionLabel = "Aggiungi uncinetto",
                onActionClick = onAddClick,
                illustration = { com.tieniilfilo.app.ui.components.HookIllustration() },
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(hooks, key = { it.id }) { hook ->
                    HookListItem(hook = hook)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun HookListItem(hook: HookEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "${hook.sizeMm} mm",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                val usSize = hook.sizeMm.toUsHookSize()
                val ukSize = hook.sizeMm.toUkHookSize()
                if (usSize != null) {
                    Text(
                        text = "US $usSize",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (ukSize.isNotEmpty()) {
                    Text(
                        text = "UK $ukSize",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            if (hook.brand.isNotEmpty()) {
                Text(
                    text = hook.brand,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = hook.material.displayText(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

fun HookMaterial.toDisplayString(): String = when (this) {
    HookMaterial.METALLO -> "Metallo"
    HookMaterial.BAMBOO -> "Bamboo"
    HookMaterial.ERGONOMICO -> "Ergonomico"
    HookMaterial.PLASTICA -> "Plastica"
    HookMaterial.LEGNO -> "Legno"
    HookMaterial.ALTRO -> "Altro"
}

@Composable
fun HookMaterial.displayText(): String = stringResource(when (this) {
    HookMaterial.METALLO -> R.string.hook_material_metal
    HookMaterial.BAMBOO -> R.string.hook_material_bamboo
    HookMaterial.ERGONOMICO -> R.string.hook_material_ergonomic
    HookMaterial.PLASTICA -> R.string.hook_material_plastic
    HookMaterial.LEGNO -> R.string.hook_material_wood
    HookMaterial.ALTRO -> R.string.hook_material_other
})
