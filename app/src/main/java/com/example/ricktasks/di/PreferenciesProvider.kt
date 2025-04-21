package com.example.ricktasks.di

import android.content.Context
import com.example.ricktasks.data.repository.PreferencesRepository
import com.example.ricktasks.utils.SharedPreferencesManager

object PreferencesProvider {

    fun provideRepository(context: Context): PreferencesRepository {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val manager = SharedPreferencesManager(prefs)
        return PreferencesRepository(manager)
    }
}
