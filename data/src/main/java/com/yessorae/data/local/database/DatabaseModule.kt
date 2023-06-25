package com.yessorae.data.local.database

import android.content.Context
import androidx.room.Room
import com.yessorae.data.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideGoalReminderDatabase(
        @ApplicationContext context: Context
    ): GoalReminderDatabase = Room.databaseBuilder(
        context,
        GoalReminderDatabase::class.java,
        DATABASE_NAME
    ).build()
}
