package com.example.learningapp.datastore_viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object UserPreferencesKeys {
    val USERNAME = stringPreferencesKey("username")
    val DOB = stringPreferencesKey("dob")
    val PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri")
    val PROFILE_IMAGE_PATH = stringPreferencesKey("profile_image_path")
}

data class UserPreferences(
    val username: String = "",
    val dob: String = "",
    val profileImageUri: String = "",
    val profileImagePath: String = ""
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .map { preferences ->
            UserPreferences(
                username = preferences[UserPreferencesKeys.USERNAME] ?: "",
                dob = preferences[UserPreferencesKeys.DOB] ?: "",
                profileImageUri = preferences[UserPreferencesKeys.PROFILE_IMAGE_URI] ?: ""
            )
        }

    suspend fun updateUsername(username: String) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.USERNAME] = username
        }
    }

    suspend fun updateDob(dob: String) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.DOB] = dob
        }
    }

    suspend fun updateProfileImageUri(uri: String) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.PROFILE_IMAGE_URI] = uri
        }
    }

    suspend fun updateProfileImagePath(path: String) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.PROFILE_IMAGE_PATH] = path
        }
    }
}