package com.yessorae.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.yessorae.data.Constants.COL_TODO_ID
import com.yessorae.data.Constants.TABLE_TODO
import com.yessorae.data.local.database.model.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
abstract class TodoDao : BaseDao<TodoEntity> {
    @Query("SELECT * FROM $TABLE_TODO WHERE $COL_TODO_ID = :id")
    abstract suspend fun loadTodoById(id: Int): TodoEntity

    @Query("SELECT * FROM $TABLE_TODO WHERE date BETWEEN :start AND :end")
    abstract fun loadTodoFlow(
        start: LocalDateTime,
        end: LocalDateTime
    ): Flow<List<TodoEntity>>
}