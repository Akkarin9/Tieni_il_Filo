package com.tieniilfilo.app.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tieniilfilo.app.data.repository.HookRepository
import com.tieniilfilo.app.data.repository.ProjectRepository
import com.tieniilfilo.app.data.repository.YarnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val yarnRepository: YarnRepository,
    private val hookRepository: HookRepository,
    private val projectRepository: ProjectRepository,
) : ViewModel() {
    private val _totalPrice = MutableStateFlow(0.0)

    init {
        viewModelScope.launch {
            _totalPrice.value = yarnRepository.getTotalPrice()
        }
    }

    val yarnCount: StateFlow<Int> = yarnRepository.getCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val hookCount: StateFlow<Int> = hookRepository.getCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val activeProjects: StateFlow<Int> = projectRepository.getActiveCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val completedProjects: StateFlow<Int> = projectRepository.getCompletedCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val pausedProjects: StateFlow<Int> = projectRepository.getPausedCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()
}
