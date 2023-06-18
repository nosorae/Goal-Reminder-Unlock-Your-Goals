package com.yessorae.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.yessorae.data.local.database.converter.GoalTypeConverter
import com.yessorae.data.local.database.converter.LocalDateTimeConverter
import com.yessorae.data.local.database.dao.GoalDao
import com.yessorae.data.local.database.dao.TodoDao
import com.yessorae.data.local.database.model.GoalEntity
import com.yessorae.data.local.database.model.TodoEntity

@Database(
    entities = [TodoEntity::class, GoalEntity::class],
    version = 1
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class GoalReminderDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun todoDao(): TodoDao
}