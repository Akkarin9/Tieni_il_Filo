package com.tieniilfilo.app.ui.screens.hook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tieniilfilo.app.data.local.entity.HookEntity
import com.tieniilfilo.app.data.local.entity.HookMaterial
import com.tieniilfilo.app.data.repository.HookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HookViewModel @Inject constructor(
    private val repository: HookRepository,
) : ViewModel() {

    val hooks: StateFlow<List<HookEntity>> = repository.getAllHooks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addHook(sizeMm: Double, material: HookMaterial, brand: String) {
        viewModelScope.launch {
            repository.insert(
                HookEntity(
                    sizeMm = sizeMm,
                    material = material,
                    brand = brand,
                )
            )
        }
    }

    fun updateHook(hook: HookEntity) {
        viewModelScope.launch {
            repository.update(hook)
        }
    }

    fun deleteHook(hook: HookEntity) {
        viewModelScope.launch {
            repository.delete(hook)
        }
    }
}
