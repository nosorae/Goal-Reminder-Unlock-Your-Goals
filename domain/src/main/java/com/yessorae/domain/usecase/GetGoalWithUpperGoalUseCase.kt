package com.yessorae.domain.usecase

import com.yessorae.domain.model.GoalWithUpperGoal
import com.yessorae.domain.repository.GoalRepository
import javax.inject.Inject

class GetGoalWithUpperGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(goalId: Int): GoalWithUpperGoal {
        val goal = goalRepository.getGoalById(goalId)
        val upperGoal = goal.upperGoalId?.let { upperGoalId ->
            goalRepository.getGoalById(upperGoalId)
        }

        return GoalWithUpperGoal(
            goal = goal,
            upperGoal = upperGoal
        )
    }
}
