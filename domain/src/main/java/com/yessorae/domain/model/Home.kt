package com.yessorae.domain.model

data class Home(
    val finalGoal: String,
    val finalGoalYear: Int,
    val yearlyGoal: List<GoalWithUpperGoal>,
    val monthlyGoal: List<GoalWithUpperGoal>,
    val weeklyGoal: List<GoalWithUpperGoal>,
    val dailyTodo: List<TodoWithGoal>
)
