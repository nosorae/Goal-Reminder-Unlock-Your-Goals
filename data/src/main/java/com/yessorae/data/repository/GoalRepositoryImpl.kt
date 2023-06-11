package com.yessorae.data.repository

import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.mockGoalModels
import com.yessorae.domain.repository.GoalRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime

class GoalRepositoryImpl @Inject constructor() : GoalRepository {
    override fun getYearlyGoalsFlow(day: LocalDateTime): Flow<List<Goal>> { // todo impl
        return flowOf(mockGoalModels.take(3)) // todo impl
    }

    override fun getMonthlyGoalsFlow(day: LocalDateTime): Flow<List<Goal>> { // todo impl
        return flowOf(mockGoalModels.take(5)) // todo impl
    }

    override fun getWeekdayGoalsFlow(day: LocalDateTime): Flow<List<Goal>> { // todo impl
        return flowOf(mockGoalModels.take(5)) // todo impl
    }

    override suspend fun getGoalById(goalId: Int): Goal {
        return mockGoalModels[0] // todo impl
    }

    override suspend fun insertGoal(goal: Goal): Int {
        return 0 // todo impl
    }

    override suspend fun updateGoal(goal: Goal) {
        // todo impl
    }
}
