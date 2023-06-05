package com.yessorae.goalreminder

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.yessorae.designsystem.theme.GoalReminderTheme
import com.yessorae.goalreminder.navigation.GoalReminderNavHost


@Composable
fun GoalReminderAppScreen() {
    GoalReminderTheme {
        val navController = rememberNavController()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GoalReminderNavHost(navController = navController)
        }
    }
}