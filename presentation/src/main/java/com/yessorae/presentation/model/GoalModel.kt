package com.yessorae.presentation.model

import com.yessorae.domain.model.Goal
import com.yessorae.domain.model.enum.GoalType
import com.yessorae.presentation.R
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import com.yessorae.util.TextString
import com.yessorae.util.getMonthDisplay
import com.yessorae.util.getWeekDisplay
import kotlin.math.roundToInt
import kotlinx.datetime.LocalDateTime

data class GoalModel(
    val goalId: Int = 0,
    val title: String,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val totalScore: Int,
    val currentScore: Int,
    val contributeGoalId: Int? = null,
    val contributeScore: Int? = null,
    val memo: String? = null,
    val type: GoalType,
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

fun Goal.asModel(): GoalModel {
    return GoalModel(
        goalId = goalId,
        title = title,
        startTime = startTime,
        endTime = endTime,
        totalScore = totalScore,
        currentScore = currentScore,
        type = type,
        memo = memo
    )
}

fun GoalModel.asDomainModel(): Goal {
    return Goal(
        title = title,
        startTime = startTime,
        endTime = endTime,
        totalScore = totalScore,
        currentScore = currentScore,
        type = type,
        memo = memo
    )
}

val mockGoalDatumModels = listOf(
    GoalModel(
        title = "운동하기",
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 150,
        type = GoalType.YEARLY
    ),
    GoalModel(
        title = "독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기",
        startTime = LocalDateTime(2023, 6, 1, 0, 0),
        endTime = LocalDateTime(2023, 6, 30, 23, 59),
        totalScore = 30,
        currentScore = 10,
        type = GoalType.MONTHLY
    ),
    GoalModel(
        title ="영어공부하기",
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 200,
        type = GoalType.YEARLY
    ),
    GoalModel(
        title  ="물 마시기",
        startTime = LocalDateTime(2023, 6, 1, 0, 0),
        endTime = LocalDateTime(2023, 6, 30, 23, 59),
        totalScore = 30,
        currentScore = 15,
        type = GoalType.MONTHLY
    ),
    GoalModel(
        title = "걷기",
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 250,
        type = GoalType.YEARLY
    )
)
