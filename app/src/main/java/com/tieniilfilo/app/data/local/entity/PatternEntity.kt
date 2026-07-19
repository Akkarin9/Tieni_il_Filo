package com.tieniilfilo.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patterns")
data class PatternEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: PatternType = PatternType.ALTRO,
    @ColumnInfo(name = "source_type") val sourceType: PatternSourceType = PatternSourceType.LINK,
    @ColumnInfo(name = "file_uri") val fileUri: String? = null,
    @ColumnInfo(name = "external_link") val externalLink: String? = null,
    @ColumnInfo(name = "is_bookmarked") val isBookmarked: Boolean = false,
    val notes: String = "",
    @ColumnInfo(name = "is_sample") val isSample: Boolean = false,
)
