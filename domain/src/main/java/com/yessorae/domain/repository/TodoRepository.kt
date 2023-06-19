package com.yessorae.domain.repository

import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.TodoWithGoal
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TodoRepository {
    fun getDailyTodosFlow(day: LocalDateTime): Flow<List<Todo>>

    suspend fun getTodo(todoId: Int): Todo

    suspend fun insertTodo(todo: Todo): Int

    suspend fun updateTodo(todo: Todo)

    suspend fun deleteTodo(todoId: Int)
}
