package com.yessorae.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesDatastoreRepository {
    fun getServiceOnOff(): Flow<Boolean>
    suspend fun setServiceOnOff(on: Boolean)

    fun getFinalGoal(): Flow<String>
    suspend fun setFinalGoal(goal: String)

    fun getFinalGoalYear(): Flow<Int>
    suspend fun setFinalGoalYear(year: Int)
}
