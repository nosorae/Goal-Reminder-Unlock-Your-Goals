package com.yessorae.presentation.model

import com.yessorae.presentation.model.enum.GoalType
import com.yessorae.util.getMonthDisplay
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt


data class Goal(
    val title: String,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val totalScore: Int,
    val currentScore: Int,
    val type: GoalType
) {
    val subtitle by lazy {
        if (startTime == null || endTime == null) {
            null
        } else {
            when (type) {
                GoalType.YEARLY -> {
                    "${startTime.getMonthDisplay()}-${endTime.getMonthDisplay()}"
                }

                GoalType.MONTHLY -> {
                    val startDay = "%d".format(startTime.dayOfMonth)
                    val endDay = "%d".format(endTime.dayOfMonth)
                    "${startDay}-${endDay}"
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

val mockGoalData = listOf(
    Goal(
        "운동하기",
        LocalDateTime(2023, 1, 1, 0, 0),
        LocalDateTime(2023, 12, 31, 23, 59),
        365,
        150,
        GoalType.YEARLY
    ),
    Goal(
        "독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기",
        LocalDateTime(2023, 6, 1, 0, 0),
        LocalDateTime(2023, 6, 30, 23, 59),
        30,
        10,
        GoalType.MONTHLY
    ),
    Goal(
        "영어공부하기",
        LocalDateTime(2023, 1, 1, 0, 0),
        LocalDateTime(2023, 12, 31, 23, 59),
        365,
        200,
        GoalType.YEARLY
    ),
    Goal(
        "물 마시기",
        LocalDateTime(2023, 6, 1, 0, 0),
        LocalDateTime(2023, 6, 30, 23, 59),
        30,
        15,
        GoalType.MONTHLY
    ),
    Goal(
        "걷기",
        LocalDateTime(2023, 1, 1, 0, 0),
        LocalDateTime(2023, 12, 31, 23, 59),
        365,
        250,
        GoalType.YEARLY
    ),
    Goal(
        "일기쓰기",
        LocalDateTime(2023, 6, 1, 0, 0),
        LocalDateTime(2023, 6, 30, 23, 59),
        30,
        20,
        GoalType.MONTHLY
    ),
    Goal(
        "조기일어나기",
        LocalDateTime(2023, 1, 1, 0, 0),
        LocalDateTime(2023, 12, 31, 23, 59),
        365,
        180,
        GoalType.YEARLY
    ),
    Goal(
        "계단오르기",
        LocalDateTime(2023, 6, 1, 0, 0),
        LocalDateTime(2023, 6, 30, 23, 59),
        30,
        25,
        GoalType.MONTHLY
    ),
    Goal(
        "식사량 줄이기",
        LocalDateTime(2023, 1, 1, 0, 0),
        LocalDateTime(2023, 12, 31, 23, 59),
        365,
        200,
        GoalType.YEARLY
    ),
    Goal(
        "코드작성하기",
        LocalDateTime(2023, 6, 1, 0, 0),
        LocalDateTime(2023, 6, 30, 23, 59),
        30,
        30,
        GoalType.MONTHLY
    )
)
