package com.yessorae.di

import com.yessorae.data.repository.GoalRepositoryImpl
import com.yessorae.data.repository.PreferencesDatastoreRepositoryImpl
import com.yessorae.data.repository.TodoRepositoryImpl
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.domain.repository.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindGoalRepository(
        goalRepository: GoalRepositoryImpl
    ): GoalRepository

    @Binds
    @Singleton
    fun bindTodoRepository(
        todoRepository: TodoRepositoryImpl
    ): TodoRepository

    @Binds
    @Singleton
    fun bindPreferencesDatastoreRepository(
        preferenceDataStoreRepository: PreferencesDatastoreRepositoryImpl
    ): PreferencesDatastoreRepository
}
