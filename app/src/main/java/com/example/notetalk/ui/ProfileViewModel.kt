package com.example.notetalk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notetalk.data.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {
    fun onProfilePictureSelected(imageRes: Int) {
        viewModelScope.launch {
            settingsManager.setProfilePicture(imageRes)
        }
    }
}