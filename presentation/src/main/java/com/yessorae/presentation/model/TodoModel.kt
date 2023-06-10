package com.yessorae.presentation.model

import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.presentation.R
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import com.yessorae.util.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class TodoModel(
    val todoId: Int = 0,
    val title: String = "",
    val date: LocalDate = LocalDate.now(),
    val completed: Boolean = false,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val goalModel: GoalModel? = null,
    val goalContributionScore: Int? = null,
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
        completed = completed,
        startTime = startTime,
        endTime = endTime,
        goalModel = null,
        goalContributionScore = goalContributionScore,
        memo = memo
    )
}
fun TodoWithGoal.asModel(): TodoModel {
    return TodoModel(
        todoId = todoId,
        title = title,
        completed = completed,
        startTime = startTime,
        endTime = endTime,
        goalModel = goal?.asModel(),
        goalContributionScore = goalContributionScore,
        memo = memo
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
        memo = memo
    )
}

fun TodoModel.asTodoWithGoal(): TodoWithGoal {
    return TodoWithGoal(
        todoId = todoId,
        title = title,
        completed = completed,
        startTime = startTime,
        endTime = endTime,
        goal = goalModel?.asDomainModel(),
        memo = memo
    )
}

val mockTodoDatumModels = listOf(
    TodoModel(
        todoId = 1,
        goalContributionScore = 5,
        title = "Morning Jogging",
        startTime = LocalDateTime(2023, 6, 7, 6, 0),
        endTime = LocalDateTime(2023, 6, 7, 7, 0),
        completed = false,
        memo = "Jog around the local park for one hour"
    ),
    TodoModel(
        todoId = 2,
        goalContributionScore = 8,
        title = "Python Learning",
        startTime = LocalDateTime(2023, 6, 7, 8, 0),
        endTime = LocalDateTime(2023, 6, 7, 10, 0),
        completed = false,
        memo = "Complete the exercises from chapter 7 to 9 in the Python book"
    ),
    TodoModel(
        todoId = 3,
        goalContributionScore = null,
        title = "Grocery Shopping",
        startTime = LocalDateTime(2023, 6, 7, 12, 0),
        endTime = LocalDateTime(2023, 6, 7, 13, 30),
        completed = false,
        memo = "Buy fresh fruits, vegetables, milk, bread, and eggs"
    )
)
