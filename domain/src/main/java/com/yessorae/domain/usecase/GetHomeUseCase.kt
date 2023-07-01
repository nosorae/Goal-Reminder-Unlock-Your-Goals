package com.yessorae.domain.usecase

import com.yessorae.domain.model.Home
import com.yessorae.domain.model.type.GoalType
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.domain.util.combine
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

class GetHomeUseCase @Inject constructor(
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository,
    private val getTodoWithUpperGoalUseCaseFlow: GetTodoWithUpperGoalUseCaseFlow,
    private val getGoalWithUpperGoalUseCaseFlow: GetGoalWithUpperGoalUseCaseFlow
) {
    operator fun invoke(date: LocalDateTime): Flow<Home> {
        return combine(
            preferencesDatastoreRepository.getFinalGoalYear(),
            preferencesDatastoreRepository.getFinalGoal(),
            getGoalWithUpperGoalUseCaseFlow(date = date, goalType = GoalType.YEARLY),
            getGoalWithUpperGoalUseCaseFlow(date = date, goalType = GoalType.MONTHLY),
            getGoalWithUpperGoalUseCaseFlow(date = date, goalType = GoalType.WEEKLY),
            getTodoWithUpperGoalUseCaseFlow(date)
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
