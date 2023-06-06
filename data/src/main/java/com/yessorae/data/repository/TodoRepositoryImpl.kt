package com.yessorae.data.repository

import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.mockTodoData
import com.yessorae.domain.repository.TodoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime

class TodoRepositoryImpl @Inject constructor() : TodoRepository {
    override fun getDailyTodosFlow(day: LocalDateTime): Flow<List<Todo>> { // todo replace
        return flowOf(mockTodoData) // todo replace
    }
}
