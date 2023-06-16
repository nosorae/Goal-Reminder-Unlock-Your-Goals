package com.yessorae.presentation.screen.editors

import com.yessorae.presentation.model.GoalModel

sealed class EditorDialogState {
    object None : EditorDialogState()
    object Date : EditorDialogState()

    object StartDate : EditorDialogState()
    object EndDate : EditorDialogState()
    object StartTime : EditorDialogState()
    object EndTime : EditorDialogState()
    data class ContributeGoal(val goals: List<GoalModel>) : EditorDialogState()
    object ExitConfirm : EditorDialogState()
    object NotificationPermission : EditorDialogState()
}
