package com.yessorae.data.local.database

import com.yessorae.data.local.database.GoalReminderDatabase
import com.yessorae.data.local.database.dao.GoalDao
import com.yessorae.data.local.database.dao.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideGoalDao(
        database: GoalReminderDatabase
    ): GoalDao = database.goalDao()

    @Provides
    fun provideTodoDao(
        database: GoalReminderDatabase
    ): TodoDao = database.todoDao()
}