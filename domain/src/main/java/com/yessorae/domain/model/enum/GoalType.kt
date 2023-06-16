package com.yessorae.domain.model.enum

enum class GoalType {
    YEARLY,
    WEEKLY,
    MONTHLY,
    NONE
}

fun String.toGoalType(): GoalType {
    return when (this) {
        GoalType.YEARLY.name -> GoalType.YEARLY
        GoalType.WEEKLY.name -> GoalType.WEEKLY
        GoalType.MONTHLY.name -> GoalType.MONTHLY
        else -> GoalType.NONE
    }
}
