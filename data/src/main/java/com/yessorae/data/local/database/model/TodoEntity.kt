package com.yessorae.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yessorae.data.Constants
import kotlinx.datetime.LocalDateTime

@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COL_TODO_ID)
    val todoId: Int = 0,
    val title: String,
    val done: Boolean,
    @ColumnInfo(name = Constants.COL_START_TIME)
    val startTime: LocalDateTime,
    @ColumnInfo(name = Constants.COL_END_TIME)
    val endTime: LocalDateTime,
    @ColumnInfo(name = Constants.COL_UPPER_GOAL_ID)
    val upperGoalId: Int? = null,
    @ColumnInfo(name = Constants.COL_UPPER_GOAL_CONTRIBUTION_SCORE)
    val upperGoalContributionScore: Int? = null,
    val memo: String? = null
)
