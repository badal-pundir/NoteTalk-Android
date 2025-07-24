package com.example.notetalk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notetalk.data.SettingsManager
import com.example.notetalk.data.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManger: SettingsManager
) : ViewModel() {
    val theme = settingsManger.themeFlow

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            settingsManger.setTheme(theme)
        }
    }
}