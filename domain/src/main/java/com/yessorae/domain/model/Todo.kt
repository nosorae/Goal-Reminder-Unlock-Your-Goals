package com.yessorae.domain.model

import kotlinx.datetime.LocalDateTime

data class Todo(
    var todoId: Int = 0,
    val title: String,
    val done: Boolean = false,
    val date: LocalDateTime,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val upperGoalId: Int? = null,
    val upperGoalContributionScore: Int? = null,
    val notification: Boolean = false,
    val memo: String? = null
)

data class TodoWithGoal(
    val todo: Todo,
    val upperGoal: Goal? = null
)
