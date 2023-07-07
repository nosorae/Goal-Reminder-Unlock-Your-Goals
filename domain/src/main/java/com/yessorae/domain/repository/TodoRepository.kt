package com.yessorae.domain.repository

import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.TodoWithGoal
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TodoRepository {
    fun getDailyTodosFlow(day: LocalDateTime): Flow<List<Todo>>

    suspend fun getTodosByUpperGoal(upperGoalId: Int): List<Todo>

    suspend fun getTodo(todoId: Int): Todo

    suspend fun insertTodo(todo: Todo): Int

    suspend fun checkTodoTransaction(todoWithGoal: TodoWithGoal)

    suspend fun updateTodoTransaction(todo: Todo)

    suspend fun deleteTodoTransaction(todo: Todo)
}
