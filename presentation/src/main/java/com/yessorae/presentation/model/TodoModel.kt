package com.yessorae.presentation.model

import com.yessorae.domain.model.Todo
import com.yessorae.domain.model.TodoWithGoal
import com.yessorae.presentation.R
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import com.yessorae.util.now
import com.yessorae.util.toStartLocalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class TodoModel(
    var todoId: Int = 0,
    val title: String = "",
    val done: Boolean = false,
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val upperGoalModel: GoalModel? = null,
    val upperGoalContributionScore: Int? = null,
    val notification: Boolean = false,
    val memo: String? = null
) : HomeOptionListItem {
    override val id: Int = todoId
    override val display: String = title

    val subtitle: StringModel? by lazy {
        if (startTime == null || endTime == null) {
            null
        } else {
            ResString(R.string.common_hour_duration, startTime.time.hour, endTime.time.hour)
        }
    }

    val goalId: Int? = upperGoalModel?.goalId
    val goalTitle: String? = upperGoalModel?.title

    fun getNoUpperGoalTodoModel(): TodoModel {
        return this.copy(
            upperGoalModel = null,
            upperGoalContributionScore = null
        )
    }
}

fun TodoWithGoal.asModel(): TodoModel {
    return TodoModel(
        todoId = todo.todoId,
        title = todo.title,
        done = todo.done,
        date = todo.date.date,
        startTime = todo.startTime,
        endTime = todo.endTime,
        upperGoalModel = upperGoal?.asModel(),
        upperGoalContributionScore = todo.upperGoalContributionScore,
        notification = todo.notification,
        memo = todo.memo
    )
}

fun Todo.asModel(): TodoModel {
    return TodoModel(
        todoId = todoId,
        title = title,
        done = done,
        date = date.date,
        startTime = startTime,
        endTime = endTime,
        upperGoalModel = null, // todo refactor
        upperGoalContributionScore = upperGoalContributionScore,
        notification = notification,
        memo = memo
    )
}
fun TodoModel.asDomainModel(): Todo {
    return Todo(
        todoId = todoId,
        title = title,
        done = done,
        date = date.toStartLocalDateTime(),
        startTime = startTime,
        endTime = endTime,
        upperGoalId = goalId,
        upperGoalContributionScore = upperGoalContributionScore,
        notification = notification,
        memo = memo
    )
}

fun TodoModel.asDomainWithGoalModel(): TodoWithGoal {
    return TodoWithGoal(
        todo = this.asDomainModel(),
        upperGoal = upperGoalModel?.asDomainModel()
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
