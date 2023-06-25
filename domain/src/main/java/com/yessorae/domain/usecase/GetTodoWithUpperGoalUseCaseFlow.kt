package com.yessorae.domain.usecase

import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.TodoRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

class GetTodoWithUpperGoalUseCaseFlow @Inject constructor(
    private val todoRepository: TodoRepository,
    private val goalRepository: GoalRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(date: LocalDateTime): Flow<List<TodoWithGoal>> {
        return todoRepository.getDailyTodosFlow(date).flatMapLatest { list ->
            flow {
                emit(
                    list.map { todo ->
                        todo.upperGoalId?.let { upperGoalId ->
                            val upperGoal = goalRepository.getGoalById(goalId = upperGoalId)
                            TodoWithGoal(
                                todo = todo,
                                upperGoal = upperGoal
                            )
                        } ?: TodoWithGoal(todo = todo)
                    }
                )
            }
        }
    }
}
