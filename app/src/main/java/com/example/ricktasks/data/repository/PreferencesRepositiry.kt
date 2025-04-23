package com.example.ricktasks.data.repository

import com.example.ricktasks.utils.SharedPreferencesManager

class PreferencesRepository(private val preferencesManager: SharedPreferencesManager) {
    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_TASK_FILTER = "task_filter"
    }

    fun saveThemePreference(isDark: Boolean) {
        preferencesManager.saveBoolean(KEY_DARK_MODE, isDark)
    }

    fun getThemePreference(): Boolean {
        return preferencesManager.getBoolean(KEY_DARK_MODE, false)
    }

    fun saveTaskFilter(filter: Int) {
        preferencesManager.saveInt(KEY_TASK_FILTER, filter)
    }

    fun getTaskFilter(): Int {
        return preferencesManager.getInt(KEY_TASK_FILTER, 2)
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