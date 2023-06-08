package com.yessorae.presentation.screen.editors.todo

import com.yessorae.base.BaseScreenViewModel
import com.yessorae.domain.model.Goal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TodoEditorViewModel @Inject constructor() : BaseScreenViewModel<TodoEditorScreenState>() {

    fun onChangeTitle(title: String) {
        updateState {
            stateValue.copy(
                title = title
            )
        }

    }

    fun onClickStartDate() {
        updateState {
            stateValue.copy(
                todoEditorDialogState = TodoEditorDialogState.StartDate
            )
        }
    }

    fun onClickEndDate() {
        updateState {
            stateValue.copy(
                todoEditorDialogState = TodoEditorDialogState.EndDate
            )
        }
    }

    fun onClickStartTime() {
        updateState {
            stateValue.copy(
                todoEditorDialogState = TodoEditorDialogState.StartTime
            )
        }
    }

    fun onClickEndTime() {
        updateState {
            stateValue.copy(
                todoEditorDialogState = TodoEditorDialogState.EndTime
            )
        }
    }

    fun onSelectDate(date: LocalDate) {
        updateState {
            when (stateValue.todoEditorDialogState) {
                TodoEditorDialogState.StartDate -> {
                    stateValue.copy(
                        startDate = date
                    )
                }

                TodoEditorDialogState.EndDate -> {
                    stateValue.copy(
                        endDate = date
                    )
                }

                else -> stateValue
            }
        }
        onCloseDialog()
    }

    fun onSelectTime(time: LocalDateTime) {
        updateState {
            when (stateValue.todoEditorDialogState) {
                TodoEditorDialogState.StartTime -> {
                    stateValue.copy(
                        startTime = time
                    )
                }

                TodoEditorDialogState.EndTime -> {
                    stateValue.copy(
                        endTime = time
                    )
                }

                else -> stateValue
            }
        }
    }

    fun onClickContributeGoal() {
        updateState {
            stateValue.copy(
                todoEditorDialogState = TodoEditorDialogState.ContributeGoal
            )
        }
    }

    fun onSelectContributeGoal(goal: Goal) {
        updateState {
            stateValue.copy(
                contributeGoal = goal
            )
        }
    }

    fun onChangeContributeGoalScore(score: Int) {
        updateState {
            stateValue.copy(
                contributeScore = score
            )
        }
    }

    fun onClickBack() {
        // todo 다시 안보기 조건 따져서 하자
        updateState {
            stateValue.copy(
                todoEditorDialogState = TodoEditorDialogState.ExitConfirm
            )
        }
    }

    fun onCloseDialog() {
        updateState {
            stateValue.copy(
                todoEditorDialogState = TodoEditorDialogState.None
            )
        }
    }

    fun onClickSave() {
        // todo insert or update TODO
    }


    override fun createInitialState(): TodoEditorScreenState {
        return TodoEditorScreenState()
    }
}

data class TodoEditorScreenState(
    val title: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val contributeGoal: Goal? = null,
    val contributeScore: Int? = null,
    val memo: String? = null,
    val todoEditorDialogState: TodoEditorDialogState = TodoEditorDialogState.None
)

sealed class TodoEditorDialogState {
    object None : TodoEditorDialogState()
    object StartDate : TodoEditorDialogState()
    object StartTime : TodoEditorDialogState()
    object EndDate : TodoEditorDialogState()
    object EndTime : TodoEditorDialogState()
    object ContributeGoal : TodoEditorDialogState()

    object ExitConfirm : TodoEditorDialogState()


}