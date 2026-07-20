package com.tieniilfilo.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val status: ProjectStatus = ProjectStatus.DA_INIZIARE,
    @ColumnInfo(name = "start_date") val startDate: Long? = null,
    @ColumnInfo(name = "end_date") val endDate: Long? = null,
    val notes: String = "",
    @ColumnInfo(name = "pattern_id") val patternId: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_sample") val isSample: Boolean = false,
    @ColumnInfo(name = "target_deadline") val targetDeadline: Long? = null,
    @ColumnInfo(name = "rating") val rating: Int = 0,
)
