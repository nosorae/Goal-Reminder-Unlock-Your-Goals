package com.yessorae.domain.usecase

import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoWithUpperGoalUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
    private val goalRepository: GoalRepository
) {

    suspend operator fun invoke(todoId: Int): TodoWithGoal {
        val todo = todoRepository.getTodo(todoId = todoId)
        val upperGoal = todo.upperGoalId?.let { goalId ->
            goalRepository.getGoalById(goalId)
        }

        return TodoWithGoal(
            todo = todo,
            upperGoal = upperGoal
        )
    }
}
