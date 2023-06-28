package com.yessorae.presentation.model

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
    val totalScore: Int,
    val currentScore: Int,
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
        (currentScore / totalScore.toDouble() * 100).roundToInt()
    }

    val progress: Float by lazy {
        (currentScore / totalScore.toFloat()).coerceIn(0f, 1f)
    }

    val complete by lazy {
        currentScore >= totalScore
    }
}

data class GoalWithUpperGoalModel(
    val goal: GoalModel,
    val upperGoal: GoalModel? = null
)

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
