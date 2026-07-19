package com.tieniilfilo.app.ui.screens.pattern

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternSourceType
import com.tieniilfilo.app.data.local.entity.PatternType
import com.tieniilfilo.app.data.repository.PatternRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatternViewModel @Inject constructor(
    private val repository: PatternRepository,
) : ViewModel() {

    val allPatterns: StateFlow<List<PatternEntity>> = repository.getAllPatterns()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val bookmarkedPatterns: StateFlow<List<PatternEntity>> = repository.getBookmarkedPatterns()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun observePattern(id: Long) = repository.getPatternByIdFlow(id)

    fun addPattern(
        title: String,
        type: PatternType,
        sourceType: PatternSourceType,
        fileUri: String?,
        externalLink: String?,
        notes: String,
    ) {
        viewModelScope.launch {
            repository.insert(
                PatternEntity(
                    title = title,
                    type = type,
                    sourceType = sourceType,
                    fileUri = fileUri,
                    externalLink = externalLink,
                    notes = notes,
                )
            )
        }
    }

    fun updatePattern(pattern: PatternEntity) {
        viewModelScope.launch {
            repository.update(pattern)
        }
    }

    fun toggleBookmark(pattern: PatternEntity) {
        viewModelScope.launch {
            repository.update(pattern.copy(isBookmarked = !pattern.isBookmarked))
        }
    }

    fun deletePattern(pattern: PatternEntity) {
        viewModelScope.launch {
            repository.delete(pattern)
        }
    }
}
