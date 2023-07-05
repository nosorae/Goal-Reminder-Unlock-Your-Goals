package com.yessorae.presentation.model

import com.yessorae.domain.common.DomainConstants
import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.GoalWithUpperGoal
import com.yessorae.domain.model.type.GoalType
import com.yessorae.presentation.R
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import com.yessorae.util.TextString
import com.yessorae.util.getMonthDisplay
import com.yessorae.util.getWeekDisplay
import kotlin.math.roundToInt
import kotlinx.datetime.LocalDateTime

data class GoalModel(
    var goalId: Int = 0,
    val title: String,
    val dateFrom: LocalDateTime,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val totalScore: Int = DomainConstants.DEFAULT_TOTAL_SCORE,
    val currentScore: Int = DomainConstants.DEFAULT_INITIAL_CURRENT_SCORE,
    val upperGoalId: Int? = null,
    val upperGoalContributionScore: Int? = null,
    val memo: String? = null,
    val notification: Boolean = false,
    val type: GoalType
) {
    val subtitle: StringModel? by lazy {
        if (startTime == null || endTime == null) {
            null
        } else {
            when (type) {
                GoalType.YEARLY -> {
                    TextString("${startTime.getMonthDisplay()}-${endTime.getMonthDisplay()}")
                }

                GoalType.MONTHLY -> {
                    ResString(
                        R.string.common_day_duration,
                        startTime.dayOfMonth,
                        endTime.dayOfMonth
                    )
                }

                GoalType.WEEKLY -> {
                    TextString("${startTime.getWeekDisplay()}-${endTime.getWeekDisplay()}")
                }

                else -> {
                    TextString("")
                }
            }
        }
    }

    val percent: Int by lazy {
        try {
            (currentScore / totalScore.toDouble() * 100).roundToInt()
        } catch (e: Exception) {
            0
        }
    }

    val progress: Float by lazy {
        try {
            (currentScore / totalScore.toFloat()).coerceIn(0f, 1f)
        } catch (e: Exception) {
            0f
        }
    }

    val showProgress: Boolean by lazy {
        totalScore > 0
    }

    val complete by lazy {
        if (showProgress) {
            currentScore >= totalScore
        } else {
            false
        }
    }
}

data class GoalWithUpperGoalModel(
    val goal: GoalModel,
    val upperGoal: GoalModel? = null
) {
    val contributionText: StringModel? by lazy {
        val upperGoalContributionScore = goal.upperGoalContributionScore
        val title = upperGoal?.title
        if (upperGoalContributionScore != null && title != null) {
            val maxUpperGoalLength = 20
            val shortTitle = if (title.length >= maxUpperGoalLength) {
                "${title.take(
                    maxUpperGoalLength
                )}..."
            } else {
                title
            }
            ResString(
                R.string.home_goal_contribution_,
                shortTitle,
                upperGoalContributionScore
            )
        } else {
            null
        }
    }
}

fun Goal.asModel(): GoalModel {
    return GoalModel(
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

fun GoalModel.asDomainModel(): Goal {
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

fun GoalWithUpperGoal.asModel(): GoalWithUpperGoalModel {
    return GoalWithUpperGoalModel(
        goal = goal.asModel(),
        upperGoal = upperGoal?.asModel()
    )
}

val mockGoalDatumModels = listOf(
    GoalModel(
        title = "운동하기",
        dateFrom = LocalDateTime(2023, 1, 1, 0, 0),
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 150,
        upperGoalContributionScore = 30,
        type = GoalType.YEARLY
    ),
    GoalModel(
        title = "독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기",
        dateFrom = LocalDateTime(2023, 6, 1, 0, 0),
        startTime = LocalDateTime(2023, 6, 1, 0, 0),
        endTime = LocalDateTime(2023, 6, 30, 23, 59),
        totalScore = 30,
        currentScore = 10,
        type = GoalType.MONTHLY
    ),
    GoalModel(
        title = "영어공부하기",
        dateFrom = LocalDateTime(2023, 1, 1, 0, 0),
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 200,
        upperGoalContributionScore = 1234,
        type = GoalType.YEARLY
    ),
    GoalModel(
        title = "물 마시기",
        dateFrom = LocalDateTime(2023, 6, 1, 0, 0),
        startTime = LocalDateTime(2023, 6, 1, 0, 0),
        endTime = LocalDateTime(2023, 6, 30, 23, 59),
        totalScore = 30,
        currentScore = 15,
        type = GoalType.MONTHLY
    ),
    GoalModel(
        title = "걷기",
        dateFrom = LocalDateTime(2023, 1, 1, 0, 0),
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 250,
        type = GoalType.YEARLY
    )
)
