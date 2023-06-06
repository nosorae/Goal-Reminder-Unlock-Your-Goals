package com.yessorae.domain.repository

import com.yessorae.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface GoalRepository {
    fun getYearlyGoalsFlow(day: LocalDateTime): Flow<List<Goal>>

    fun getMonthlyGoalsFlow(day: LocalDateTime): Flow<List<Goal>>

    fun getWeekdayGoalsFlow(day: LocalDateTime): Flow<List<Goal>>
}

