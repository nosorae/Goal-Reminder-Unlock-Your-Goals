package com.yessorae.presentation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.yessorae.domain.model.type.GoalType

interface Destination {
    val route: String
}

object MainDestination : Destination {
    override val route = "main"
}

object FinalGoalDestination : Destination {
    override val route: String = "final_goal"
    const val onBoardingArg = "on_boarding"
    val routeWithArgs = "$route/{$onBoardingArg}"

    private const val defaultOnBoardingValue = false

    val arguments = listOf(
        navArgument(onBoardingArg) {
            defaultValue = defaultOnBoardingValue
            type = NavType.BoolType
        }
    )

    fun getRouteWithArgs(onBoarding: Boolean = defaultOnBoardingValue) = "$route/$onBoarding"
}

object TodoEditorDestination : Destination {
    override val route: String = "todo_editor"
    const val todoIdArg = "todo_id"
    const val todoDayMilliSecArg = "todo_day_milli_sec"
    val routeWithArgs = "$route?$todoIdArg={$todoIdArg}&$todoDayMilliSecArg={$todoDayMilliSecArg}"

    const val defaultTodoId = Int.MAX_VALUE
    const val defaultTodoDayMilliSec = Long.MAX_VALUE

    val arguments = listOf(
        navArgument(todoIdArg) {
            defaultValue = defaultTodoId
            type = NavType.IntType
        },
        navArgument(todoDayMilliSecArg) {
            defaultValue = defaultTodoDayMilliSec
            type = NavType.LongType
        }
    )

    fun getRouteWithArgs(todoId: Int = defaultTodoId, todoDay: Long = defaultTodoDayMilliSec) =
        "$route?$todoIdArg=$todoId&$todoDayMilliSecArg=$todoDay"
}

object GoalEditorDestination : Destination {
    override val route: String = "goal_editor"
    const val goalIdArg = "goal_id"
    const val goalDayMilliSecArg = "goal_day_milli_sec"
    const val goalTypeArg = "goal_type"
    val routeWithArgs =
        "$route?$goalIdArg={$goalIdArg}" +
            "&$goalDayMilliSecArg={$goalDayMilliSecArg}" +
            "&$goalTypeArg={$goalTypeArg}"

    const val defaultGoalId = Int.MAX_VALUE
    const val defaultGoalDayMilliSec = Long.MAX_VALUE
    val defaultGoalType: String = GoalType.NONE.name

    val arguments = listOf(
        navArgument(goalIdArg) {
            defaultValue = defaultGoalId
            type = NavType.IntType
        },
        navArgument(goalDayMilliSecArg) {
            defaultValue = defaultGoalDayMilliSec
            type = NavType.LongType
        },
        navArgument(goalTypeArg) {
            defaultValue = defaultGoalType
            type = NavType.StringType
        }
    )

    fun getRouteWithArgs(
        goalId: Int = defaultGoalId,
        goalDay: Long = defaultGoalDayMilliSec,
        goalType: GoalType = GoalType.NONE
    ) =
        "$route?$goalIdArg=$goalId&$goalDayMilliSecArg=$goalDay&$goalTypeArg=${goalType.name}"
}

object SettingDestination : Destination {
    override val route: String = "setting"
}

object RepeatedTodoEditorDestination : Destination {
    override val route: String = "repeated_todo_editor_destination"
}
