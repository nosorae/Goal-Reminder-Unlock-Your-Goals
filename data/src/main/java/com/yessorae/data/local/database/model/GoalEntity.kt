package com.yessorae.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.yessorae.data.Constants
import com.yessorae.domain.model.enum.GoalType
import kotlinx.datetime.LocalDateTime

data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COL_GOAL_ID)
    val goalId: Int = 0,
    val title: String,
    @ColumnInfo(name = Constants.COL_START_TIME)
    val startTime: LocalDateTime? = null,
    @ColumnInfo(name = Constants.COL_END_TIME)
    val endTime: LocalDateTime? = null,
    @ColumnInfo(name = Constants.COL_TOTAL_SCORE)
    val totalScore: Int,
    @ColumnInfo(name = Constants.COL_CURRENT_SCORE)
    val currentScore: Int = 0,
    val memo: String? = null,
    val type: GoalType
)
