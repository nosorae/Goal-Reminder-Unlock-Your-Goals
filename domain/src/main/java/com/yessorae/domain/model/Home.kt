package com.yessorae.domain.model

data class Home(
    val finalGoal: String,
    val finalGoalYear: Int,
    val yearlyGoal: List<Goal>,
    val monthlyGoal: List<Goal>,
    val weeklyGoal: List<Goal>,
    val dailyTodo: List<Todo>,
)