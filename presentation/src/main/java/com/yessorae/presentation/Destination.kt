package com.yessorae.presentation

interface Destination {
    val route: String
}

object MainDestination : Destination {
    override val route = "main"
}

object TodoEditorDestination : Destination {
    override val route: String = "todo_editor"
}

object GoalEditorDestination : Destination {
    override val route: String = "goal_editor"
}

object SettingDestination : Destination {
    override val route: String = "setting"
}

object RepeatedTodoEditorDestination : Destination {
    override val route: String = "repeated_todo_editor_destination"
}