package com.getir.patika.foodmap.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.getir.patika.foodmap.data.PreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) : PreferencesRepository {
    override suspend fun getGreetingDialogState(): Boolean {
        val preferences = userPreferences.data.first()
        return preferences[GREETING_STATE] ?: false
    }

    override suspend fun setGreetingDialogState() {
        userPreferences.edit { preferences ->
            preferences[GREETING_STATE] = true
        }
    }

    companion object {
        val GREETING_STATE = booleanPreferencesKey("greeting_state")
    }
}
