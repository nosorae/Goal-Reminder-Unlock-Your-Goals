package com.yessorae.domain.usecase

import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.domain.repository.TodoRepository
import javax.inject.Inject

class CheckTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend operator fun invoke(todoWithGoal: TodoWithGoal) {
        todoRepository.checkTodo(todoWithGoal)
    }
}