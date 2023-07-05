package com.yessorae.data.repository

import com.yessorae.data.local.preference.PreferenceDataStore
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PreferencesDatastoreRepositoryImpl @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) : PreferencesDatastoreRepository {
    override suspend fun getCompleteOnBoardingMockData(): Boolean {
        return preferenceDataStore.getCompleteOnBoardingMockData()
    }

    override suspend fun setCompleteOnBoardingMockData() {
        preferenceDataStore.setCompleteOnBoardingMockData()
    }

    override fun getCompleteOnBoarding(): Flow<Boolean> {
        return preferenceDataStore.getCompleteOnBoarding()
    }

    override suspend fun setCompleteOnBoarding() {
        preferenceDataStore.setCompleteOnBoarding()
    }

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
