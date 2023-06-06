package com.yessorae.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yessorae.data.Constants.PREF_KEY_FINAL_GOAL
import com.yessorae.data.Constants.PREF_KEY_FINAL_GOAL_YEAR
import com.yessorae.data.Constants.PREF_KEY_IS_SCREEN_ON
import com.yessorae.data.local.preference.PreferenceDataStore
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDatastoreRepositoryImpl @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) : PreferencesDatastoreRepository {

    override fun getServiceOnOff(): Flow<Boolean> {
        return preferenceDataStore.getServiceOnOff()
    }

    override suspend fun setServiceOnOff(on: Boolean) {
        preferenceDataStore.setServiceOnOff(on = on)
    }

    override fun getFinalGoal(): Flow<String> {
        return preferenceDataStore.getFinalGoal()
    }

    override suspend fun setFinalGoal(goal: String) {
        preferenceDataStore.setFinalGoal(goal = goal)
    }

    override fun getFinalGoalYear(): Flow<Int> {
        return preferenceDataStore.getFinalGoalYear()
    }

    override suspend fun setFinalGoalYear(year: Int) {
        preferenceDataStore.setFinalGoalYear(year = year)
    }
}
