package com.yessorae.presentation.model

import com.yessorae.domain.model.Todo
import com.yessorae.presentation.R
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import kotlinx.datetime.LocalDateTime

data class TodoModel(
    val todoId: Int,
    val title: String,
    val completed: Boolean = false,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val goalId: Int? = null,
    val goalTitle: String? = null,
    val goalContributionScore: Int? = null,
    val description: String? = null
) {
    val subtitle: StringModel? by lazy {
        if (startTime == null || endTime == null) {
            null
        } else {
            ResString(R.string.common_hour_duration, startTime.time.hour, endTime.time.hour)
        }
    }
}

fun Todo.asModel(): TodoModel {
    return TodoModel(
        todoId = todoId,
        title = title,
        completed = completed,
        startTime = startTime,
        endTime = endTime,
        goalId = goalId,
        goalTitle = goalTitle,
        goalContributionScore = goalContributionScore,
        description = description
    )
}

fun TodoModel.asDomainModel(): Todo {
    return Todo(
        todoId = todoId,
        title = title,
        completed = completed,
        startTime = startTime,
        endTime = endTime,
        goalId = goalId,
        goalTitle = goalTitle,
        goalContributionScore = goalContributionScore,
        description = description
    )
}

val mockTodoDatumModels = listOf(
    TodoModel(
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
    TodoModel(
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
    TodoModel(
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