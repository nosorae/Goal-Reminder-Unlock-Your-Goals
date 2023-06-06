package com.yessorae.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesDatastoreRepository {
    val isServiceOn: Flow<Boolean>
    suspend fun setServiceOnOff(on: Boolean)

    val finalGoal: Flow<String>
    suspend fun setFinalGoal(goal: String)

    val finalGoalYear: Flow<Int>
    suspend fun setFinalGoalYear(year:Int)
}
