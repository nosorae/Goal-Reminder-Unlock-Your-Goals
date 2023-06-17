package com.yessorae.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.yessorae.data.Constants
import kotlinx.datetime.LocalDateTime

@Entity
data class TodoEntity(
    val todoId: Int,
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
