package com.yessorae.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.yessorae.data.Constants
import com.yessorae.data.local.database.model.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TodoDao : BaseDao<TodoEntity> {
    @Query("SELECT * FROM ${Constants.TABLE_TODO}")
    abstract fun loadAll(): Flow<List<TodoEntity>>
}