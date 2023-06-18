package com.yessorae.presentation.model

import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.presentation.R
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import com.yessorae.util.now
import com.yessorae.util.toDefaultLocalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class TodoModel(
    val todoId: Int = 0,
    val title: String = "",
    val done: Boolean = false,
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val goalModel: GoalModel? = null,
    val upperGoalContributionScore: Int? = null,
    val memo: String? = null
) {
    val subtitle: StringModel? by lazy {
        if (startTime == null || endTime == null) {
            null
        } else {
            ResString(R.string.common_hour_duration, startTime.time.hour, endTime.time.hour)
        }
    }

    val goalId: Int? = goalModel?.goalId
    val goalTitle: String? = goalModel?.title
}

fun Todo.asModel(): TodoModel {
    return TodoModel(
        todoId = todoId,
        title = title,
        done = done,
        date = date.date,
        startTime = startTime,
        endTime = endTime,
        goalModel = null,
        upperGoalContributionScore = upperGoalContributionScore,
        memo = memo
    )
}
fun TodoWithGoal.asModel(): TodoModel {
    return TodoModel(
        todoId = todoId,
        title = title,
        done = done,
        date = date.date,
        startTime = startTime,
        endTime = endTime,
        goalModel = goal?.asModel(),
        upperGoalContributionScore = upperGoalContributionScore,
        memo = memo
    )
}

fun TodoModel.asDomainModel(): Todo {
    return Todo(
        todoId = todoId,
        title = title,
        done = done,
        date = date.toDefaultLocalDateTime(),
        startTime = startTime,
        endTime = endTime,
        upperGoalId = goalId,
        upperGoalContributionScore = upperGoalContributionScore,
        memo = memo
    )
}

fun TodoModel.asDomainModelWithGoal(): TodoWithGoal {
    return TodoWithGoal(
        todoId = todoId,
        title = title,
        done = done,
        date = date.toDefaultLocalDateTime(),
        startTime = startTime,
        endTime = endTime,
        goal = goalModel?.asDomainModel(),
        upperGoalContributionScore = upperGoalContributionScore,
        memo = memo
    )
}

val mockTodoDatumModels = listOf(
    TodoModel(
        todoId = 1,
        upperGoalContributionScore = 5,
        title = "Morning Jogging",
        startTime = LocalDateTime(2023, 6, 7, 6, 0),
        endTime = LocalDateTime(2023, 6, 7, 7, 0),
        done = false,
        memo = "Jog around the local park for one hour"
    ),
    TodoModel(
        todoId = 2,
        upperGoalContributionScore = 8,
        title = "Python Learning",
        startTime = LocalDateTime(2023, 6, 7, 8, 0),
        endTime = LocalDateTime(2023, 6, 7, 10, 0),
        done = false,
        memo = "Complete the exercises from chapter 7 to 9 in the Python book"
    ),
    TodoModel(
        todoId = 3,
        upperGoalContributionScore = null,
        title = "Grocery Shopping",
        startTime = LocalDateTime(2023, 6, 7, 12, 0),
        endTime = LocalDateTime(2023, 6, 7, 13, 30),
        done = false,
        memo = "Buy fresh fruits, vegetables, milk, bread, and eggs"
    )
)
