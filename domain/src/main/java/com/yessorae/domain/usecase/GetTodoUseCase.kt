package com.yessorae.domain.usecase

import com.yessorae.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(todoId: Int) {
        todoRepository.getTodo(todoId = todoId)
    }
}
