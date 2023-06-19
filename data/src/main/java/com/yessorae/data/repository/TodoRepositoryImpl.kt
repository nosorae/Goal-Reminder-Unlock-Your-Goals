package com.yessorae.data.repository

import com.yessorae.common.Logger
import com.yessorae.data.local.database.dao.TodoDao
import com.yessorae.data.local.database.model.asDomainModel
import com.yessorae.data.local.database.model.asEntity
import com.yessorae.domain.model.Todo
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.util.toEndLocalDateTime
import com.yessorae.util.toStartLocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepository {
    override fun getDailyTodosFlow(day: LocalDateTime): Flow<List<Todo>> {
        val startDateTime = day.date.toStartLocalDateTime()
        val endDateTime = day.date.toEndLocalDateTime()
        Logger.uiDebug("getDailyTodosFlow / startDateTime $startDateTime / endDateTime $endDateTime")
        return todoDao.loadTodoFlow(
            start = startDateTime,
            end = endDateTime
        ).map { list ->
            Logger.uiDebug("getDailyTodosFlow / list $list")
            list.map { todo ->
                todo.asDomainModel()
            }
        }
    }

    override suspend fun getTodo(todoId: Int): Todo {
        return todoDao.loadTodoById(id = todoId).asDomainModel()
    }

    override suspend fun insertTodo(todo: Todo): Int {
        return todoDao.insert(todo.asEntity()).toInt()
    }

    override suspend fun updateTodo(todo: Todo) {
        Logger.uiDebug("data updateTodo $todo")
        todoDao.update(todo.asEntity())
    }

    override suspend fun deleteTodo(todo: Todo) {
        Logger.uiDebug("data deleteTodo $todo")
        todoDao.delete(todo.asEntity())
    }
}
