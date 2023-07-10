package com.yessorae.presentation.screen.home

import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.HomeOptionListItem
import com.yessorae.presentation.model.TodoModel

sealed class HomeDialogState {
    object None : HomeDialogState()
    object OverlayConfirmDialog : HomeDialogState()
    object DatePickerDialog : HomeDialogState()
    data class DeleteTodoConfirmDialog(val todoModel: TodoModel) : HomeDialogState() // todo 통합
    data class DeleteGoalConfirmDialog(val goalModel: GoalModel) : HomeDialogState() // todo 통합
    data class OptionDialog(val selectedItem: HomeOptionListItem) : HomeDialogState()
}
