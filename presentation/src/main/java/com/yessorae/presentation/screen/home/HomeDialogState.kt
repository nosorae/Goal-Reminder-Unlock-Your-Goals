package com.yessorae.presentation.screen.home

import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.TodoModel

sealed class HomeDialogState {
    object None : HomeDialogState()
    object OverlayConfirmDialog : HomeDialogState()
    object DatePickerDialog : HomeDialogState()
    data class DeleteTodoConfirmDialog(val todoModel: TodoModel) : HomeDialogState()
    data class DeleteGoalConfirmDialog(val goalModel: GoalModel) : HomeDialogState()
}
