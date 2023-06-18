package com.yessorae.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yessorae.data.Constants
import com.yessorae.domain.model.Todo
import kotlinx.datetime.LocalDateTime

@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COL_TODO_ID)
    val todoId: Int = 0,
    val title: String,
    val done: Boolean,
    val date: LocalDateTime,
    @ColumnInfo(name = Constants.COL_START_TIME)
    val startTime: LocalDateTime? = null,
    @ColumnInfo(name = Constants.COL_END_TIME)
    val endTime: LocalDateTime? = null,
    @ColumnInfo(name = Constants.COL_UPPER_GOAL_ID)
    val upperGoalId: Int? = null,
    @ColumnInfo(name = Constants.COL_UPPER_GOAL_CONTRIBUTION_SCORE)
    val upperGoalContributionScore: Int? = null,
    val notification: Boolean = false,
    val memo: String? = null
)

fun TodoEntity.asDomainModel(): Todo {
    return Todo(
        todoId = todoId,
        title = title,
        done = done,
        date = date,
        startTime = startTime,
        endTime = endTime,
        upperGoalId = upperGoalId,
        upperGoalContributionScore = upperGoalContributionScore,
        notification = notification,
        memo = memo
    )
}

fun Todo.asEntity(): TodoEntity {
    return TodoEntity(
        todoId = todoId,
        title = title,
        done = done,
        date = date,
        startTime = startTime,
        endTime = endTime,
        upperGoalId = upperGoalId,
        upperGoalContributionScore = upperGoalContributionScore,
        notification = notification,
        memo = memo
    )
}