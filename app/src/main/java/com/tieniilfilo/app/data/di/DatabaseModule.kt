package com.tieniilfilo.app.data.di

import android.content.Context
import androidx.room.Room
import com.tieniilfilo.app.data.local.AppDatabase
import com.tieniilfilo.app.data.local.dao.HookDao
import com.tieniilfilo.app.data.local.dao.PatternDao
import com.tieniilfilo.app.data.local.dao.ProjectDao
import com.tieniilfilo.app.data.local.dao.YarnDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tieniilfilo.db",
        ).addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4).build()
    }

    @Provides
    fun provideYarnDao(db: AppDatabase): YarnDao = db.yarnDao()

    @Provides
    fun provideHookDao(db: AppDatabase): HookDao = db.hookDao()

    @Provides
    fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()

    @Provides
    fun providePatternDao(db: AppDatabase): PatternDao = db.patternDao()
}
