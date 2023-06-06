package com.yessorae.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yessorae.data.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDataStore @Inject constructor(
    private val preferences: DataStore<Preferences>
) {
    private val isServiceOnKey = booleanPreferencesKey(Constants.PREF_KEY_IS_SCREEN_ON)
    private val finalGoalKey = stringPreferencesKey(Constants.PREF_KEY_FINAL_GOAL)
    private val finalGoalYearKey = intPreferencesKey(Constants.PREF_KEY_FINAL_GOAL_YEAR)

    fun getServiceOnOff(): Flow<Boolean> {
        return preferences.data.map { preferences ->
            preferences[isServiceOnKey] ?: false
        }
    }

    suspend fun setServiceOnOff(on: Boolean) {
        preferences.edit { settings ->
            settings[isServiceOnKey] = on
        }
    }

    fun getFinalGoal(): Flow<String> {
        return preferences.data.map { preferences ->
            preferences[finalGoalKey] ?: ""
        }
    }

    suspend fun setFinalGoal(goal: String) {
        preferences.edit { settings ->
            settings[finalGoalKey] = goal
        }
    }

    fun getFinalGoalYear(): Flow<Int> {
        return preferences.data.map { preferences ->
            preferences[finalGoalYearKey] ?: 0
        }
    }

    suspend fun setFinalGoalYear(year: Int) {
        preferences.edit { settings ->
            settings[finalGoalYearKey] = year
        }
    }
}