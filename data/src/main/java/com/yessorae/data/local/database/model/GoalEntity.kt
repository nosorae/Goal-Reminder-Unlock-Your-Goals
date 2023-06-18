package com.yessorae.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yessorae.data.Constants
import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.type.GoalType
import kotlinx.datetime.LocalDateTime

@Entity(tableName = Constants.TABLE_GOAL)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COL_GOAL_ID)
    var goalId: Int = 0,
    var title: String = "",
    @ColumnInfo(name = Constants.COL_DATE_FROM)
    var dateFrom: LocalDateTime = LocalDateTime(0, 1, 1, 0, 0),
    @ColumnInfo(name = Constants.COL_START_TIME)
    var startTime: LocalDateTime? = null,
    @ColumnInfo(name = Constants.COL_END_TIME)
    var endTime: LocalDateTime? = null,
    @ColumnInfo(name = Constants.COL_TOTAL_SCORE)
    var totalScore: Int = 0,
    @ColumnInfo(name = Constants.COL_CURRENT_SCORE)
    var currentScore: Int = 0,
    @ColumnInfo(name = Constants.COL_UPPER_GOAL_ID)
    var upperGoalId: Int? = null,
    @ColumnInfo(name = Constants.COL_UPPER_GOAL_CONTRIBUTION_SCORE)
    var upperGoalContributionScore: Int? = null,
    var memo: String? = null,
    var notification: Boolean = false,
    var type: GoalType = GoalType.NONE
)

fun GoalEntity.asDomainModel(): Goal {
    return Goal(
        goalId = goalId,
        title = title,
        dateFrom = dateFrom,
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
        dateFrom = dateFrom,
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

