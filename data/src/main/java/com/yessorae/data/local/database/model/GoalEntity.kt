package com.yessorae.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.yessorae.data.DataConstants
import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.type.GoalType
import com.yessorae.util.getWeekRangePair
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import java.time.YearMonth

@Entity(tableName = DataConstants.TABLE_GOAL)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataConstants.COL_GOAL_ID)
    var goalId: Int = 0,
    val title: String = "",
    @ColumnInfo(name = DataConstants.COL_DATE_FROM)
    val dateFrom: LocalDateTime = LocalDateTime(0, 1, 1, 0, 0),
    @ColumnInfo(name = DataConstants.COL_START_TIME)
    val startTime: LocalDateTime? = null,
    @ColumnInfo(name = DataConstants.COL_END_TIME)
    val endTime: LocalDateTime? = null,
    @ColumnInfo(name = DataConstants.COL_TOTAL_SCORE)
    val totalScore: Int = 0,
    @ColumnInfo(name = DataConstants.COL_CURRENT_SCORE)
    val currentScore: Int = 0,
    @ColumnInfo(name = DataConstants.COL_UPPER_GOAL_ID)
    val upperGoalId: Int? = null,
    @ColumnInfo(name = DataConstants.COL_UPPER_GOAL_CONTRIBUTION_SCORE)
    val upperGoalContributionScore: Int? = null,
    val memo: String? = null,
    val notification: Boolean = false,
    val type: GoalType = GoalType.NONE,
    @ColumnInfo(name = DataConstants.COL_SERVER_SYNC)
    val serverSync: Boolean = false,
    @ColumnInfo(name = DataConstants.COL_GOOGLE_CALENDAR_SYNC)
    val googleCalendarSync: Boolean = false
) {
    @Ignore
    val done = totalScore <= currentScore
}

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
