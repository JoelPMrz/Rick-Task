package com.example.ricktasks.data.repository

import com.example.ricktasks.utils.SharedPreferencesManager

class PreferencesRepository(private val preferencesManager: SharedPreferencesManager) {

    fun saveThemePreference(isDark: Boolean) {
        preferencesManager.saveBoolean("dark_mode", isDark)
    }

    fun getThemePreference(): Boolean {
        return preferencesManager.getBoolean("dark_mode", false)
    }

    fun toggleFavorite(characterId: Int) {
        val key = "fav_$characterId"
        val current = preferencesManager.getBoolean(key, false)
        preferencesManager.saveBoolean(key, !current)
    }

    fun isFavorite(characterId: Int): Boolean {
        return preferencesManager.getBoolean("fav_$characterId", false)
    }

    fun getFavorites(): Set<Int> {
        return preferencesManager.getAll()
            .filter { it.key.startsWith("fav_") && it.value == true }
            .mapNotNull { it.key.removePrefix("fav_").toIntOrNull() }
            .toSet()
    }

}