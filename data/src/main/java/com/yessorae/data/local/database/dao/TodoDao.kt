package com.yessorae.data.local.database.dao

import androidx.room.Dao
import com.yessorae.data.local.database.model.TodoEntity

@Dao
abstract class TodoDao : BaseDao<TodoEntity> {

}