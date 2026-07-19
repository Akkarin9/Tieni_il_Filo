package com.tieniilfilo.app.data.repository

import com.tieniilfilo.app.data.local.crossref.ProjectHookCrossRef
import com.tieniilfilo.app.data.local.crossref.ProjectYarnCrossRef
import com.tieniilfilo.app.data.local.dao.ProjectDao
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.ProjectPhotoEntity
import com.tieniilfilo.app.data.local.entity.ProjectStatus
import com.tieniilfilo.app.data.local.entity.YarnStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val yarnRepository: YarnRepository,
) {
    fun getAllProjects(): Flow<List<ProjectEntity>> = projectDao.getAllProjects()

    fun getProjectsByStatus(status: String): Flow<List<ProjectEntity>> =
        projectDao.getProjectsByStatus(status)

    suspend fun getProjectById(id: Long): ProjectEntity? = projectDao.getProjectById(id)

    fun getProjectByIdFlow(id: Long): Flow<ProjectEntity?> = projectDao.getProjectByIdFlow(id)

    suspend fun insert(project: ProjectEntity): Long = projectDao.insert(project)

    suspend fun update(project: ProjectEntity) {
        projectDao.update(project)
        // When completing a project, release yarns
        if (project.status == ProjectStatus.COMPLETATO) {
            releaseYarnsForProject(project.id)
        }
    }

    suspend fun delete(project: ProjectEntity) = projectDao.delete(project)

    suspend fun deleteById(id: Long) {
        releaseYarnsForProject(id)
        projectDao.deleteById(id)
    }

    fun getActiveCount(): Flow<Int> = projectDao.getActiveProjectCount()

    fun getCompletedCount(): Flow<Int> = projectDao.getCompletedProjectCount()

    fun getTotalCount(): Flow<Int> = projectDao.getProjectCount()

    suspend fun countByStatus(status: String): Int = projectDao.countByStatus(status)

    // Photos
    suspend fun getPhotos(projectId: Long): List<ProjectPhotoEntity> =
        projectDao.getPhotosForProject(projectId)

    suspend fun getAllPhotos(): List<ProjectPhotoEntity> = projectDao.getAllPhotos()

    suspend fun insertPhoto(photo: ProjectPhotoEntity): Long =
        projectDao.insertPhoto(photo)

    suspend fun deletePhoto(photo: ProjectPhotoEntity) = projectDao.deletePhoto(photo)

    suspend fun deletePhotoById(id: Long) = projectDao.deletePhotoById(id)

    // Yarn links
    suspend fun linkYarn(projectId: Long, yarnId: Long) {
        projectDao.insertYarnCrossRef(ProjectYarnCrossRef(projectId, yarnId))
        val yarn = yarnRepository.getYarnById(yarnId)
        if (yarn != null && yarn.status != YarnStatus.ESAURITO) {
            yarnRepository.update(yarn.copy(status = YarnStatus.IN_USO))
        }
    }

    suspend fun unlinkYarn(projectId: Long, yarnId: Long) {
        projectDao.deleteYarnCrossRef(ProjectYarnCrossRef(projectId, yarnId))
        val yarn = yarnRepository.getYarnById(yarnId)
        if (yarn != null && yarn.status == YarnStatus.IN_USO) {
            yarnRepository.update(yarn.copy(status = YarnStatus.DISPONIBILE))
        }
    }

    // Hook links
    suspend fun linkHook(projectId: Long, hookId: Long) {
        projectDao.insertHookCrossRef(ProjectHookCrossRef(projectId, hookId))
    }

    suspend fun unlinkHook(projectId: Long, hookId: Long) {
        projectDao.deleteHookCrossRef(ProjectHookCrossRef(projectId, hookId))
    }

    suspend fun deleteSampleData() = projectDao.deleteSampleData()

    private suspend fun releaseYarnsForProject(projectId: Long) {
        val yarns = yarnRepository.getYarnsForProject(projectId)
        for (yarn in yarns) {
            if (yarn.status == YarnStatus.IN_USO) {
                yarnRepository.update(yarn.copy(status = YarnStatus.DISPONIBILE))
            }
        }
    }
}
