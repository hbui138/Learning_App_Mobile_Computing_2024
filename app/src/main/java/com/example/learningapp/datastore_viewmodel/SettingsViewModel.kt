package com.example.learningapp.datastore_viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: UserPreferencesRepository,
    private val imageStorageHelper: ImageStorageHelper
) : ViewModel()
{

    val userPreferences: StateFlow<UserPreferences> = repository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    fun updateUsername(username: String) = viewModelScope.launch {
        repository.updateUsername(username)
    }

    fun updateDob(dob: String) = viewModelScope.launch {
        repository.updateDob(dob)
    }

    fun updateProfileImageUri(uri: String) = viewModelScope.launch {
        repository.updateProfileImageUri(uri)
    }

    fun saveProfileImage(uri: Uri) = viewModelScope.launch {
        val path = imageStorageHelper.saveImageToInternalStorage(uri)
        repository.updateProfileImagePath(path)
    }
}