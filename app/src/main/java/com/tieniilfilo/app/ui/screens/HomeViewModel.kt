package com.tieniilfilo.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.repository.HookRepository
import com.tieniilfilo.app.data.repository.ProjectRepository
import com.tieniilfilo.app.data.repository.YarnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val yarnRepository: YarnRepository,
    private val projectRepository: ProjectRepository,
    private val hookRepository: HookRepository,
) : ViewModel() {

    val yarnCount: StateFlow<Int> = yarnRepository.getCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val hookCount: StateFlow<Int> = hookRepository.getCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activeProjectCount: StateFlow<Int> = projectRepository.getActiveCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedProjectCount: StateFlow<Int> = projectRepository.getCompletedCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pausedProject: StateFlow<ProjectEntity?> = projectRepository.getProjectsByStatus("IN_PAUSA")
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val distinctYarnColors: StateFlow<List<Int>> = yarnRepository.getAllYarns()
        .map { yarns ->
            yarns.flatMap { yarn ->
                val fromHexes = if (!yarn.colorHexes.isNullOrBlank()) {
                    try {
                        val type = object : TypeToken<List<Int>>() {}.type
                        Gson().fromJson<List<Int>>(yarn.colorHexes, type)
                    } catch (_: Exception) { emptyList() }
                } else emptyList()
                fromHexes + if (yarn.colorHex != 0) listOf(yarn.colorHex) else emptyList()
            }.distinct()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
