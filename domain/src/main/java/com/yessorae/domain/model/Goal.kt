package com.yessorae.domain.model

import com.yessorae.domain.model.type.GoalType
import kotlinx.datetime.LocalDateTime

data class Goal(
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
)

data class GoalWithUpperGoal(
    val goal: Goal,
    val upperGoal: Goal? = null
)

val mockGoalModels = listOf(
    Goal(
        title = "운동하기",
        dateFrom = LocalDateTime(2023, 1, 1, 0, 0),
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 150,
        type = GoalType.YEARLY
    ),
    Goal(
        title = "독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기독서하기",
        dateFrom = LocalDateTime(2023, 6, 1, 0, 0),
        startTime = LocalDateTime(2023, 6, 1, 0, 0),
        endTime = LocalDateTime(2023, 6, 30, 23, 59),
        totalScore = 30,
        currentScore = 10,
        type = GoalType.MONTHLY
    ),
    Goal(
        title = "영어공부하기",
        dateFrom = LocalDateTime(2023, 1, 1, 0, 0),
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 200,
        type = GoalType.YEARLY
    ),
    Goal(
        title = "물 마시기",
        dateFrom = LocalDateTime(2023, 6, 1, 0, 0),
        startTime = LocalDateTime(2023, 6, 1, 0, 0),
        endTime = LocalDateTime(2023, 6, 30, 23, 59),
        totalScore = 30,
        currentScore = 15,
        type = GoalType.MONTHLY
    ),
    Goal(
        title = "걷기",
        dateFrom = LocalDateTime(2023, 1, 1, 0, 0),
        startTime = LocalDateTime(2023, 1, 1, 0, 0),
        endTime = LocalDateTime(2023, 12, 31, 23, 59),
        totalScore = 365,
        currentScore = 250,
        type = GoalType.YEARLY
    )
)
