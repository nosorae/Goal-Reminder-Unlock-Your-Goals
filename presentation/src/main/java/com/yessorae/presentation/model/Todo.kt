package com.yessorae.presentation.model

import kotlinx.datetime.LocalDateTime

data class Todo(
    val todoId: Int,
    val title: String,
    val completed: Boolean = false,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val goalId: Int? = null,
    val goalTitle: String? = null,
    val goalContributionScore: Int? = null,
    val description: String? = null,
) {
    val subtitle by lazy {
        if (startTime == null || endTime == null) {
            null
        } else {
            "${startTime.time.hour}-${endTime.time.hour}"
        }
    }
}

val mockTodoData = listOf(
    Todo(
        todoId = 1,
        goalId = 101,
        goalTitle = "Health Improvement",
        goalContributionScore = 5,
        title = "Morning Jogging",
        startTime = LocalDateTime(2023, 6, 7, 6, 0),
        endTime = LocalDateTime(2023, 6, 7, 7, 0),
        completed = false,
        description = "Jog around the local park for one hour"
    ),
    Todo(
        todoId = 2,
        goalId = 102,
        goalTitle = "Programming Skills",
        goalContributionScore = 8,
        title = "Python Learning",
        startTime = LocalDateTime(2023, 6, 7, 8, 0),
        endTime = LocalDateTime(2023, 6, 7, 10, 0),
        completed = false,
        description = "Complete the exercises from chapter 7 to 9 in the Python book"
    ),
    Todo(
        todoId = 3,
        goalId = null,
        goalTitle = null,
        goalContributionScore = null,
        title = "Grocery Shopping",
        startTime = LocalDateTime(2023, 6, 7, 12, 0),
        endTime = LocalDateTime(2023, 6, 7, 13, 30),
        completed = false,
        description = "Buy fresh fruits, vegetables, milk, bread, and eggs"
    )
)




