package com.yessorae.data.repository

import com.yessorae.data.local.database.dao.TodoDao
import com.yessorae.data.local.database.model.asDomainModel
import com.yessorae.data.local.database.model.asEntity
import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.util.toEndLocalDateTime
import com.yessorae.util.toStartLocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepository {
    override fun getDailyTodosFlow(day: LocalDateTime): Flow<List<Todo>> {
        val startDateTime = day.date.toStartLocalDateTime()
        val endDateTime = day.date.toEndLocalDateTime()
        return todoDao.loadTodoFlow(
            start = startDateTime,
            end = endDateTime
        ).map { list ->
            list.map { todo ->
                todo.asDomainModel()
            }
        }
    }

    override suspend fun checkTodoTransaction(todoWithGoal: TodoWithGoal) {
        todoDao.checkTodoTransaction(
            todo = todoWithGoal.todo.asEntity(),
            upperGoal = todoWithGoal.upperGoal?.asEntity()
        )
    }

    override suspend fun getTodo(todoId: Int): Todo {
        return todoDao.loadTodoById(id = todoId).asDomainModel()
    }

    override suspend fun insertTodo(todo: Todo): Int {
        return todoDao.insert(todo.asEntity()).toInt()
    }

    override suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodoTransaction(todo.asEntity())
    }

    override suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodoTransaction(todo.asEntity())
    }
}
