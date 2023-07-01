package com.yessorae.domain.usecase

import com.yessorae.domain.model.GoalWithUpperGoal
import com.yessorae.domain.model.type.GoalType
import com.yessorae.domain.repository.GoalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class GetGoalWithUpperGoalUseCaseFlow @Inject constructor(
    private val goalRepository: GoalRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(date: LocalDateTime, goalType: GoalType): Flow<List<GoalWithUpperGoal>> {
        return when (goalType) {
            GoalType.WEEKLY -> {
                goalRepository.getWeekdayGoalsFlow(day = date)
            }
            GoalType.MONTHLY -> {
                goalRepository.getMonthlyGoalsFlow(day = date)
            }
            else -> {
                goalRepository.getYearlyGoalsFlow(day = date)
            }
        }.flatMapLatest { goals ->
            flow {
                emit(
                    goals.map { goal ->
                        goal.upperGoalId?.let { upperGoalId ->
                            val upperGoal = goalRepository.getGoalById(upperGoalId)
                            GoalWithUpperGoal(
                                goal = goal,
                                upperGoal = upperGoal
                            )
                        } ?: GoalWithUpperGoal(goal = goal)
                    }
                )
            }
        }
    }
}