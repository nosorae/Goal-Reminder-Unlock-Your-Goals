package com.yessorae.domain.usecase

import com.yessorae.domain.model.Home
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.domain.util.combine
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Singleton
class GetHomeUseCase @Inject constructor(
    private val goalRepository: GoalRepository,
    private val todoRepository: TodoRepository,
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository
) {
    operator fun invoke(date: LocalDateTime): Flow<Home> {
        return combine(
            preferencesDatastoreRepository.getFinalGoalYear(),
            preferencesDatastoreRepository.getFinalGoal(),
            goalRepository.getYearlyGoalsFlow(date),
            goalRepository.getMonthlyGoalsFlow(date),
            goalRepository.getWeekdayGoalsFlow(date),
            todoRepository.getDailyTodosFlow(date)
        ) { finalGoalYear, finalGoal, yearlyGoal, monthlyGoal, weeklyGoal, dailyTodo ->
            Home(
                finalGoalYear = finalGoalYear,
                finalGoal = finalGoal,
                yearlyGoal = yearlyGoal,
                monthlyGoal = monthlyGoal,
                weeklyGoal = weeklyGoal,
                dailyTodo = dailyTodo
            )
        }
    }
}
