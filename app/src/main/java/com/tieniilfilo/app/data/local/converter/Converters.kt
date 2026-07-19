package com.tieniilfilo.app.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tieniilfilo.app.data.local.entity.HookMaterial
import com.tieniilfilo.app.data.local.entity.PatternSourceType
import com.tieniilfilo.app.data.local.entity.PatternType
import com.tieniilfilo.app.data.local.entity.ProjectStatus
import com.tieniilfilo.app.data.local.entity.YarnComposition
import com.tieniilfilo.app.data.local.entity.YarnSource
import com.tieniilfilo.app.data.local.entity.YarnStatus

class Converters {
    @TypeConverter
    fun fromYarnStatus(value: YarnStatus): String = value.name

    @TypeConverter
    fun toYarnStatus(value: String): YarnStatus = YarnStatus.valueOf(value)

    @TypeConverter
    fun fromYarnComposition(value: YarnComposition): String = value.name

    @TypeConverter
    fun toYarnComposition(value: String): YarnComposition = YarnComposition.valueOf(value)

    @TypeConverter
    fun fromYarnSource(value: YarnSource): String = value.name

    @TypeConverter
    fun toYarnSource(value: String): YarnSource = YarnSource.valueOf(value)

    @TypeConverter
    fun fromHookMaterial(value: HookMaterial): String = value.name

    @TypeConverter
    fun toHookMaterial(value: String): HookMaterial = HookMaterial.valueOf(value)

    @TypeConverter
    fun fromProjectStatus(value: ProjectStatus): String = value.name

    @TypeConverter
    fun toProjectStatus(value: String): ProjectStatus = ProjectStatus.valueOf(value)

    @TypeConverter
    fun fromPatternType(value: PatternType): String = value.name

    @TypeConverter
    fun toPatternType(value: String): PatternType = PatternType.valueOf(value)

    @TypeConverter
    fun fromPatternSourceType(value: PatternSourceType): String = value.name

    @TypeConverter
    fun toPatternSourceType(value: String): PatternSourceType = PatternSourceType.valueOf(value)

    private val gson = Gson()

    @TypeConverter
    fun fromColorIntList(value: List<Int>): String = gson.toJson(value)

    @TypeConverter
    fun toColorIntList(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}
