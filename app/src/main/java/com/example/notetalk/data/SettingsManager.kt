package com.example.notetalk.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.notetalk.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@Singleton
class SettingsManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val themeKey = stringPreferencesKey("theme_preference")
    private val profilePictureKey = intPreferencesKey("profile_picture_key")

    val themeFlow = context.dataStore.data.map { preferences ->
        val themeName = preferences[themeKey] ?: Theme.SYSTEM.name
        Theme.valueOf(themeName)
    }

    val profilePictureFlow = context.dataStore.data.map { preferences ->
        preferences[profilePictureKey] ?: R.drawable.person_28dp
    }

    suspend fun setTheme(theme: Theme) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }

    suspend fun setProfilePicture(imageRes: Int) {
        context.dataStore.edit { preferences ->
            preferences[profilePictureKey] = imageRes
        }
    }
}