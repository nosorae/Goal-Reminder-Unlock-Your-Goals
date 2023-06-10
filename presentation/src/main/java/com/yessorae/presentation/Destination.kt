package com.yessorae.presentation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
}

object MainDestination : Destination {
    override val route = "main"
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

//object CategoryEditDestination : TimeLedgerDestination {
//    override val route = "category_edit"
//    const val categoryIdArg = "category_id"
//    val routeWithArgs = "$route?$categoryIdArg={$categoryIdArg}"
//    const val defaultCategoryId = Int.MAX_VALUE
//    val arguments = listOf(
//        navArgument(categoryIdArg) {
//            defaultValue = defaultCategoryId
//            type = NavType.IntType
//        }
//    )
//
//    fun getRouteWithArgs(categoryId: Int = defaultCategoryId) = "$route?$categoryIdArg=$categoryId"
//}

object GoalEditorDestination : Destination {
    override val route: String = "goal_editor"
}

object SettingDestination : Destination {
    override val route: String = "setting"
}

object RepeatedTodoEditorDestination : Destination {
    override val route: String = "repeated_todo_editor_destination"
}