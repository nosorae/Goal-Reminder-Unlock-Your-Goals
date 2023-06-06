package com.yessorae.data.repository

import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.mockGoalData
import com.yessorae.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor() : GoalRepository {
    override fun getYearlyGoalsFlow(day: LocalDateTime): Flow<List<Goal>> { // todo replace
        return flowOf(mockGoalData.take(3))// todo replace
    }

    override fun getMonthlyGoalsFlow(day: LocalDateTime): Flow<List<Goal>> { // todo replace
        return flowOf(mockGoalData.take(5)) // todo replace
    }

    override fun getWeekdayGoalsFlow(day: LocalDateTime): Flow<List<Goal>> { // todo replace
        return flowOf(mockGoalData.take(5)) // todo replace
    }
}