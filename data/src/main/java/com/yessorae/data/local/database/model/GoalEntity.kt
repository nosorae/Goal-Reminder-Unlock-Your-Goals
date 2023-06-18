package com.yessorae.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yessorae.data.Constants
import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.enum.GoalType
import kotlinx.datetime.LocalDateTime

@Entity(tableName = Constants.TABLE_GOAL)
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
    @ColumnInfo(name = Constants.COL_UPPER_GOAL_ID)
    val upperGoalId: Int? = null,
    @ColumnInfo(name = Constants.COL_UPPER_GOAL_CONTRIBUTION_SCORE)
    val upperGoalContributionScore: Int? = null,
    val memo: String? = null,
    val notification: Boolean = false,
    val type: GoalType
)

fun GoalEntity.asDomainModel(): Goal {
    return Goal(
        goalId = goalId,
        title = title,
        startTime = startTime,
        endTime = endTime,
        totalScore = totalScore,
        currentScore = currentScore,
        upperGoalId = upperGoalId,
        upperGoalContributionScore = upperGoalContributionScore,
        memo = memo,
        notification = notification,
        type = type
    )
}

fun Goal.asEntity(): GoalEntity {
    return GoalEntity(
        goalId = goalId,
        title = title,
        startTime = startTime,
        endTime = endTime,
        totalScore = totalScore,
        currentScore = currentScore,
        upperGoalId = upperGoalId,
        upperGoalContributionScore = upperGoalContributionScore,
        memo = memo,
        notification = notification,
        type = type
    )
}

