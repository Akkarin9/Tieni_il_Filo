package com.tieniilfilo.app.ui.screens.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.ProjectStatus
import com.tieniilfilo.app.data.repository.PatternRepository
import com.tieniilfilo.app.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: ProjectRepository,
    private val patternRepository: PatternRepository,
) : ViewModel() {

    val allProjects: StateFlow<List<ProjectEntity>> = repository.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allPatterns: StateFlow<List<PatternEntity>> = patternRepository.getAllPatterns()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val activeCount = repository.getActiveCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val completedCount = repository.getCompletedCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun observeProject(id: Long) = repository.getProjectByIdFlow(id)

    fun addProject(name: String, status: ProjectStatus, notes: String, patternId: Long?) {
        viewModelScope.launch {
            repository.insert(
                ProjectEntity(
                    name = name,
                    status = status,
                    notes = notes,
                    patternId = patternId,
                )
            )
        }
    }

    fun updateProject(project: ProjectEntity) {
        viewModelScope.launch {
            val updated = if (project.status == ProjectStatus.COMPLETATO && project.endDate == null) {
                project.copy(endDate = System.currentTimeMillis())
            } else {
                project
            }
            repository.update(updated)
        }
    }

    fun completeProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.update(
                project.copy(
                    status = ProjectStatus.COMPLETATO,
                    endDate = System.currentTimeMillis(),
                )
            )
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.delete(project)
        }
    }

    fun linkYarn(projectId: Long, yarnId: Long) {
        viewModelScope.launch {
            repository.linkYarn(projectId, yarnId)
        }
    }

    fun unlinkYarn(projectId: Long, yarnId: Long) {
        viewModelScope.launch {
            repository.unlinkYarn(projectId, yarnId)
        }
    }

    fun addPhoto(projectId: Long, photoUri: String) {
        viewModelScope.launch {
            repository.insertPhoto(
                com.tieniilfilo.app.data.local.entity.ProjectPhotoEntity(
                    projectId = projectId,
                    photoUri = photoUri,
                )
            )
        }
    }

    suspend fun getPhotos(projectId: Long) = repository.getPhotos(projectId)

    fun updatePatternLink(projectId: Long, patternId: Long?) {
        viewModelScope.launch {
            val project = repository.getProjectById(projectId) ?: return@launch
            repository.update(project.copy(patternId = patternId))
        }
    }

    suspend fun getPatternById(id: Long): PatternEntity? = patternRepository.getPatternById(id)
}
