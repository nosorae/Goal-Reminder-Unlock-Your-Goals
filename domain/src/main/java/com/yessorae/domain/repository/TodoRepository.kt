package com.yessorae.domain.repository

import com.yessorae.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TodoRepository {
    fun getDailyTodosFlow(day: LocalDateTime): Flow<List<Todo>>
}