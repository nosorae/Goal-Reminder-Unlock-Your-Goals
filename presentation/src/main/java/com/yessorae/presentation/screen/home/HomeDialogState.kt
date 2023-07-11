package com.yessorae.presentation.screen.home

import com.yessorae.domain.model.type.GoalType
import com.yessorae.presentation.R
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.HomeOptionListItem
import com.yessorae.presentation.model.TodoModel
import com.yessorae.util.ResString
import com.yessorae.util.StringModel

sealed class HomeDialogState {
    object None : HomeDialogState()
    object OverlayConfirmDialog : HomeDialogState()
    object DatePickerDialog : HomeDialogState()
    data class DeleteTodoConfirmDialog(val todoModel: TodoModel) : HomeDialogState() // todo 통합
    data class DeleteGoalConfirmDialog(val goalModel: GoalModel) : HomeDialogState() // todo 통합
    data class OptionDialog(val selectedItem: HomeOptionListItem) : HomeDialogState() {
        val postponeTitle: StringModel by lazy {
            when (selectedItem) {
                is TodoModel -> {
                    ResString(R.string.home_option_dialog_postpone_next_day)
                }

                is GoalModel -> {
                    when (selectedItem.type) {
                        GoalType.YEARLY -> {
                            ResString(R.string.home_option_dialog_postpone_next_year)
                        }

                        GoalType.MONTHLY -> {
                            ResString(R.string.home_option_dialog_postpone_next_month)
                        }

                        GoalType.WEEKLY -> {
                            ResString(R.string.home_option_dialog_postpone_next_week)
                        }

                        else -> {
                            ResString(R.string.common_error_occur)
                        }
                    }
                }

                else -> {
                    ResString(R.string.common_error_occur)
                }
            }
        }
    }
}
