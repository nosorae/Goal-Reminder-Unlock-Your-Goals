package com.yessorae.goalreminder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yessorae.presentation.Destination
import com.yessorae.presentation.MainDestination
import com.yessorae.presentation.home.MainScreen


@Composable
fun GoalReminderNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Destination = MainDestination
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination.route
    ) {
        composable(
            route = MainDestination.route
        ) {
            MainScreen()
        }
    }
}
