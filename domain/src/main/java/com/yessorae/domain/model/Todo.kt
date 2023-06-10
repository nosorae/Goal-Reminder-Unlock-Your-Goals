package com.yessorae.domain.model

import kotlinx.datetime.LocalDateTime

data class Todo(
    val todoId: Int,
    val title: String,
    val completed: Boolean = false,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val goalId: Int? = null,
    val goalContributionScore: Int? = null,
    val memo: String? = null
)

data class TodoWithGoal(
    val todoId: Int,
    val title: String,
    val completed: Boolean = false,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val goal: Goal? = null,
    val goalContributionScore: Int? = null,
    val memo: String? = null
)
val mockTodoWithGoalData = listOf(
    TodoWithGoal(
        todoId = 1,
        goal = null,
        goalContributionScore = null,
        title = "Morning Jogging",
        startTime = LocalDateTime(2023, 6, 7, 6, 0),
        endTime = LocalDateTime(2023, 6, 7, 7, 0),
        completed = false,
        memo = "Jog around the local park for one hour"
    ),
    TodoWithGoal(
        todoId = 2,
        goal = null,
        goalContributionScore = null,
        title = "Python Learning",
        startTime = LocalDateTime(2023, 6, 7, 8, 0),
        endTime = LocalDateTime(2023, 6, 7, 10, 0),
        completed = false,
        memo = "Complete the exercises from chapter 7 to 9 in the Python book"
    ),
    TodoWithGoal(
        todoId = 3,
        goal = null,
        goalContributionScore = null,
        title = "Grocery Shopping",
        startTime = LocalDateTime(2023, 6, 7, 12, 0),
        endTime = LocalDateTime(2023, 6, 7, 13, 30),
        completed = false,
        memo = "Buy fresh fruits, vegetables, milk, bread, and eggs"
    )
)

val mockTodoData = listOf(
    Todo(
        todoId = 1,
        goalId = 101,
        goalContributionScore = 5,
        title = "Morning Jogging",
        startTime = LocalDateTime(2023, 6, 7, 6, 0),
        endTime = LocalDateTime(2023, 6, 7, 7, 0),
        completed = false,
        memo = "Jog around the local park for one hour"
    ),
    Todo(
        todoId = 2,
        goalId = 102,
        goalContributionScore = 8,
        title = "Python Learning",
        startTime = LocalDateTime(2023, 6, 7, 8, 0),
        endTime = LocalDateTime(2023, 6, 7, 10, 0),
        completed = false,
        memo = "Complete the exercises from chapter 7 to 9 in the Python book"
    ),
    Todo(
        todoId = 3,
        goalId = null,
        goalContributionScore = null,
        title = "Grocery Shopping",
        startTime = LocalDateTime(2023, 6, 7, 12, 0),
        endTime = LocalDateTime(2023, 6, 7, 13, 30),
        completed = false,
        memo = "Buy fresh fruits, vegetables, milk, bread, and eggs"
    )
)
