package com.yessorae.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yessorae.common.GlobalConstants
import com.yessorae.data.DataConstants
import com.yessorae.util.now
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

@Singleton
class PreferenceDataStore @Inject constructor(
    private val preferences: DataStore<Preferences>
) {
    private val completeOnBoarding = booleanPreferencesKey(
        DataConstants.PREF_KEY_COMPLETE_ON_BOARDING
    )
    private val completeOnBoardingMockData = booleanPreferencesKey(
        DataConstants.PREF_KEY_COMPLETE_ON_BOARDING_MOCK_DATA
    )
    private val isServiceOnKey = booleanPreferencesKey(DataConstants.PREF_KEY_IS_SCREEN_ON)
    private val finalGoalKey = stringPreferencesKey(DataConstants.PREF_KEY_FINAL_GOAL)
    private val finalGoalYearKey = intPreferencesKey(DataConstants.PREF_KEY_FINAL_GOAL_YEAR)

    suspend fun getCompleteOnBoardingMockData(): Boolean {
        return preferences.data.map { preferences ->
            preferences[completeOnBoardingMockData] ?: false
        }.firstOrNull() ?: false
    }
    suspend fun setCompleteOnBoardingMockData() {
        preferences.edit { settings ->
            settings[completeOnBoardingMockData] = true
        }
    }
    fun getCompleteOnBoarding(): Flow<Boolean> {
        return preferences.data.map { preferences ->
            preferences[completeOnBoarding] ?: false
        }
    }

    suspend fun setCompleteOnBoarding() {
        preferences.edit { settings ->
            settings[completeOnBoarding] = true
        }
    }

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
            preferences[finalGoalYearKey]
                ?: (LocalDateTime.now().year + GlobalConstants.DEFAULT_FINAL_GOAL_OFFSET)
        }
    }

    suspend fun setFinalGoalYear(year: Int) {
        preferences.edit { settings ->
            settings[finalGoalYearKey] = year
        }
    }
}
