package com.yessorae.domain.model

import com.yessorae.domain.common.DomainConstants
import com.yessorae.domain.model.type.GoalType
import kotlinx.datetime.LocalDateTime

data class Goal(
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
)

data class GoalWithUpperGoal(
    val goal: Goal,
    val upperGoal: Goal? = null
)
