package com.tieniilfilo.app.util

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val imagesDir: File
        get() = File(context.filesDir, "images").also { if (!it.exists()) it.mkdirs() }

    fun copyFromUri(sourceUri: Uri): String? {
        return try {
            val ext = context.contentResolver.getType(sourceUri)
                ?.substringAfterLast('/')
                ?.takeIf { it.isNotBlank() }
                ?: "jpg"
            val dest = File(imagesDir, "${UUID.randomUUID()}.$ext")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            } ?: return null
            dest.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    fun deleteIfExists(path: String?) {
        if (path.isNullOrBlank()) return
        runCatching { File(path).takeIf { it.exists() }?.delete() }
    }
}
