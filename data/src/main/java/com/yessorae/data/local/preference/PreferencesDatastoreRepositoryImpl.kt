package com.yessorae.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yessorae.data.Constants.PREF_KEY_FINAL_GOAL
import com.yessorae.data.Constants.PREF_KEY_FINAL_GOAL_YEAR
import com.yessorae.data.Constants.PREF_KEY_IS_SCREEN_ON
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDatastoreRepositoryImpl @Inject constructor(
    private val preferences: DataStore<Preferences>
) : PreferencesDatastoreRepository {
    private val isServiceOnKey = booleanPreferencesKey(PREF_KEY_IS_SCREEN_ON)
    private val finalGoalKey = stringPreferencesKey(PREF_KEY_FINAL_GOAL)
    private val finalGoalYearKey = intPreferencesKey(PREF_KEY_FINAL_GOAL_YEAR)

    override val isServiceOn: Flow<Boolean> = preferences.data.map { preferences ->
        preferences[isServiceOnKey] ?: false
    }

    override suspend fun setServiceOnOff(on: Boolean) {
        preferences.edit { settings ->
            settings[isServiceOnKey] = on
        }
    }

    override val finalGoal: Flow<String> = preferences.data.map { preferences ->
        preferences[finalGoalKey] ?: ""
    }

    override suspend fun setFinalGoal(goal: String) {
        preferences.edit { settings ->
            settings[finalGoalKey] = goal
        }
    }

    override val finalGoalYear: Flow<Int> = preferences.data.map { preferences ->
        preferences[finalGoalYearKey] ?: 0
    }

    override suspend fun setFinalGoalYear(year: Int) {
        preferences.edit { settings ->
            settings[finalGoalYearKey] = year
        }
    }
}
