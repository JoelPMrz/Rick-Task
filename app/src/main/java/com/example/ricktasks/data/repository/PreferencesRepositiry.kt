package com.example.ricktasks.data.repository

import com.example.ricktasks.utils.SharedPreferencesManager

class PreferencesRepository(private val preferencesManager: SharedPreferencesManager) {

    fun saveThemePreference(isDark: Boolean) {
        preferencesManager.saveBoolean("dark_mode", isDark)
    }

    fun getThemePreference(): Boolean {
        return preferencesManager.getBoolean("dark_mode", false)
    }

}