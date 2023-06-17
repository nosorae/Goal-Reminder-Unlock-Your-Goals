package com.yessorae.goalreminder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yessorae.presentation.Destination
import com.yessorae.presentation.FinalGoalDestination
import com.yessorae.presentation.GoalEditorDestination
import com.yessorae.presentation.MainDestination
import com.yessorae.presentation.TodoEditorDestination
import com.yessorae.presentation.screen.editors.goal.GoalEditorScreen
import com.yessorae.presentation.screen.editors.todo.TodoEditorScreen
import com.yessorae.presentation.screen.home.HomeScreen
import com.yessorae.presentation.screen.onboarding.FinalGoalScreen

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
            HomeScreen(
                onNavOutEvent = { route ->
                    navController.navigateSingleTopTo(route)
                }
            )
        }
        
        composable(
            route = FinalGoalDestination.route
        ) {
            FinalGoalScreen(
                onBackEvent = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = TodoEditorDestination.routeWithArgs,
            arguments = TodoEditorDestination.arguments
        ) {
            TodoEditorScreen(
                onBackEvent = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = GoalEditorDestination.routeWithArgs,
            arguments = GoalEditorDestination.arguments
        ) {
            GoalEditorScreen(
                onBackEvent = {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
