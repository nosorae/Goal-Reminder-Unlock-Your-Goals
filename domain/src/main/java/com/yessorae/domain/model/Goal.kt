package com.yessorae.domain.model

import com.yessorae.domain.model.enum.GoalType
import kotlinx.datetime.LocalDateTime

data class Goal(
    val goalId: Int = 0,
    val title: String,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val totalScore: Int,
    val currentScore: Int,
    val upperGoalId: Int? = null,
    val upperGoalContributionScore: Int? = null,
    val memo: String? = null,
    val notification: Boolean = false,
    val type: GoalType
)

val mockGoalModels = listOf(
    Goal(
        title = "운동하기",
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 150,
        type = GoalType.YEARLY
    ),
    Goal(
        title = "독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기",
        startTime = LocalDateTime(2023, 6, 1, 0, 0),
        endTime = LocalDateTime(2023, 6, 30, 23, 59),
        totalScore = 30,
        currentScore = 10,
        type = GoalType.MONTHLY
    ),
    Goal(
        title = "영어공부하기",
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 200,
        type = GoalType.YEARLY
    ),
    Goal(
        title = "물 마시기",
        startTime = LocalDateTime(2023, 6, 1, 0, 0),
        endTime = LocalDateTime(2023, 6, 30, 23, 59),
        totalScore = 30,
        currentScore = 15,
        type = GoalType.MONTHLY
    ),
    Goal(
        title = "걷기",
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 250,
        type = GoalType.YEARLY
    )
)
