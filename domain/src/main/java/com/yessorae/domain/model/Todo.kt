package com.yessorae.domain.model

import kotlinx.datetime.LocalDateTime

data class Todo(
    val todoId: Int,
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
    val todoId: Int,
    val title: String,
    val done: Boolean = false,
    val date: LocalDateTime,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val goal: Goal? = null,
    val upperGoalContributionScore: Int? = null,
    val notification: Boolean = false,
    val memo: String? = null
)
val mockTodoWithGoalData = listOf(
    TodoWithGoal(
        todoId = 1,
        goal = null,
        upperGoalContributionScore = null,
        title = "Morning Jogging",
        date = LocalDateTime(2023, 6, 7, 6, 0),
        startTime = LocalDateTime(2023, 6, 7, 6, 0),
        endTime = LocalDateTime(2023, 6, 7, 7, 0),
        done = false,
        memo = "Jog around the local park for one hour"
    ),
    TodoWithGoal(
        todoId = 2,
        goal = null,
        upperGoalContributionScore = null,
        title = "Python Learning",
        date = LocalDateTime(2023, 6, 7, 8, 0),
        startTime = LocalDateTime(2023, 6, 7, 8, 0),
        endTime = LocalDateTime(2023, 6, 7, 10, 0),
        done = false,
        memo = "Complete the exercises from chapter 7 to 9 in the Python book"
    ),
    TodoWithGoal(
        todoId = 3,
        goal = null,
        upperGoalContributionScore = null,
        title = "Grocery Shopping",
        date = LocalDateTime(2023, 6, 7, 12, 0),
        startTime = LocalDateTime(2023, 6, 7, 12, 0),
        endTime = LocalDateTime(2023, 6, 7, 13, 30),
        done = false,
        memo = "Buy fresh fruits, vegetables, milk, bread, and eggs"
    )
)

val mockTodoData = listOf(
    Todo(
        todoId = 1,
        upperGoalId = 101,
        upperGoalContributionScore = 5,
        title = "Morning Jogging",
        date = LocalDateTime(2023, 6, 7, 6, 0),
        startTime = LocalDateTime(2023, 6, 7, 6, 0),
        endTime = LocalDateTime(2023, 6, 7, 7, 0),
        done = false,
        memo = "Jog around the local park for one hour"
    ),
    Todo(
        todoId = 2,
        upperGoalId = 102,
        upperGoalContributionScore = 8,
        title = "Python Learning",
        date = LocalDateTime(2023, 6, 7, 8, 0),
        startTime = LocalDateTime(2023, 6, 7, 8, 0),
        endTime = LocalDateTime(2023, 6, 7, 10, 0),
        done = false,
        memo = "Complete the exercises from chapter 7 to 9 in the Python book"
    ),
    Todo(
        todoId = 3,
        upperGoalId = null,
        upperGoalContributionScore = null,
        title = "Grocery Shopping",
        date = LocalDateTime(2023, 6, 7, 12, 0),
        startTime = LocalDateTime(2023, 6, 7, 12, 0),
        endTime = LocalDateTime(2023, 6, 7, 13, 30),
        done = false,
        memo = "Buy fresh fruits, vegetables, milk, bread, and eggs"
    )
)
