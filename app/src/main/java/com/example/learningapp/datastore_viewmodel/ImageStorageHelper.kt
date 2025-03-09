package com.example.learningapp.datastore_viewmodel

import android.content.Context
import android.net.Uri
import java.io.File
import androidx.core.net.toUri

class ImageStorageHelper (private val context: Context) {
    fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val directory = File(context.filesDir, "profile_images")
        directory.mkdirs()
        val file = File(directory, "profile.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    fun loadImageFromInternalStorage(path: String): Uri? {
        return if (File(path).exists()) path.toUri() else null
    }
}