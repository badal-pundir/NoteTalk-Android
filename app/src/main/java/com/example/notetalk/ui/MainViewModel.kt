package com.example.notetalk.ui

import androidx.lifecycle.ViewModel
import com.example.notetalk.data.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    settingsManager: SettingsManager,
) : ViewModel(){
    val theme = settingsManager.themeFlow
}