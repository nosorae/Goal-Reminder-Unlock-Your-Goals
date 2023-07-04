package com.yessorae.presentation.screen.editors

import com.yessorae.domain.model.type.GoalType
import com.yessorae.presentation.R
import com.yessorae.presentation.model.GoalModel
import com.yessorae.util.ResString

sealed class EditorDialogState {
    object None : EditorDialogState()
    object Date : EditorDialogState()

    object StartDate : EditorDialogState()
    object EndDate : EditorDialogState()
    object StartTime : EditorDialogState()
    object EndTime : EditorDialogState()
    data class ContributeGoal(
        val goals: List<GoalModel>,
        val goalType: GoalType
    ) : EditorDialogState() {
        val title = when (goalType) {
            GoalType.WEEKLY -> {
                ResString(R.string.common_dialog_title_select_monthly_contribution_goal_title)
            }
            GoalType.MONTHLY -> {
                ResString(R.string.common_dialog_title_select_yearly_contribution_goal_title)
            }
            else -> {
                ResString(R.string.common_dialog_title_select_default_contribution_title)
            }
        }
    }

    object ExitConfirm : EditorDialogState()
    object NotificationPermission : EditorDialogState()
}
