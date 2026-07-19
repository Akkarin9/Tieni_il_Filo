package com.tieniilfilo.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.tieniilfilo.app.data.local.crossref.ProjectHookCrossRef
import com.tieniilfilo.app.data.local.crossref.ProjectYarnCrossRef
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.ProjectPhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY created_at DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE status = :status ORDER BY created_at DESC")
    fun getProjectsByStatus(status: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Long): ProjectEntity?

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectByIdFlow(id: Long): Flow<ProjectEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: ProjectEntity): Long

    @Update
    suspend fun update(project: ProjectEntity)

    @Delete
    suspend fun delete(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM projects")
    fun getProjectCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM projects WHERE status = :status")
    suspend fun countByStatus(status: String): Int

    @Query("SELECT COUNT(*) FROM projects WHERE status = 'IN_CORSO'")
    fun getActiveProjectCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM projects WHERE status = 'COMPLETATO'")
    fun getCompletedProjectCount(): Flow<Int>

    // Project photos
    @Query("SELECT * FROM project_photos WHERE project_id = :projectId ORDER BY taken_at ASC")
    suspend fun getPhotosForProject(projectId: Long): List<ProjectPhotoEntity>

    @Query("SELECT * FROM project_photos ORDER BY taken_at DESC")
    suspend fun getAllPhotos(): List<ProjectPhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: ProjectPhotoEntity): Long

    @Delete
    suspend fun deletePhoto(photo: ProjectPhotoEntity)

    @Query("DELETE FROM project_photos WHERE id = :id")
    suspend fun deletePhotoById(id: Long)

    // Cross references
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYarnCrossRef(crossRef: ProjectYarnCrossRef)

    @Delete
    suspend fun deleteYarnCrossRef(crossRef: ProjectYarnCrossRef)

    @Query("DELETE FROM project_yarn_cross_ref WHERE project_id = :projectId")
    suspend fun deleteAllYarnCrossRefs(projectId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHookCrossRef(crossRef: ProjectHookCrossRef)

    @Delete
    suspend fun deleteHookCrossRef(crossRef: ProjectHookCrossRef)

    @Query("DELETE FROM project_hook_cross_ref WHERE project_id = :projectId")
    suspend fun deleteAllHookCrossRefs(projectId: Long)

    @Query("DELETE FROM projects WHERE is_sample = 1")
    suspend fun deleteSampleData()
}
