package com.yessorae.presentation.screen.onboarding

import com.yessorae.base.BaseScreenViewModel
import com.yessorae.common.GlobalConstants
import com.yessorae.domain.model.FinalGoal
import com.yessorae.domain.usecase.GetFinalGoalUseCase
import com.yessorae.domain.usecase.UpdateFinalGoalUseCase
import com.yessorae.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@HiltViewModel
class FinalGoalViewModel @Inject constructor(
    private val getFinalGoalUseCase: GetFinalGoalUseCase,
    private val updateFinalGoalUseCase: UpdateFinalGoalUseCase
) : BaseScreenViewModel<FinalGoalScreenState>() {

    init {
        initState()
    }

    private fun initState() = ioScope.launch {
        getFinalGoalUseCase().collectLatest {
            updateState {
                stateValue.copy(
                    finalGoalYear = it.year,
                    finalGoalText = it.text
                )
            }
        }
    }

    fun onClickBack() = ioScope.launch {
        if (stateValue.finalGoalText.isEmpty()) {
            updateState {
                stateValue.copy(
                    dialogState = FinalGoalDialogState.ExitConfirm
                )
            }
        } else {
            _navigationEvent.emit(null)
        }
    }

    fun onClickYear() {
        updateState {
            stateValue.copy(
                dialogState = FinalGoalDialogState.YearPickerDialog(year = stateValue.finalGoalYear)
            )
        }
    }

    fun onChangeYearText(text: String) {
        updateState {
            stateValue.copy(
                finalGoalText = text
            )
        }
    }

    fun onSelectYear(year: Int) = ioScope.launch {
        updateState {
            stateValue.copy(
                finalGoalYear = year
            )
        }

        onCancelDialog()
    }

    fun onClickSave() = ioScope.launch {
        updateFinalGoalUseCase(stateValue.getFinalGoal())
        _navigationEvent.emit(null)
    }

    fun onConfirmBack() = ioScope.launch {
        _navigationEvent.emit(null)
    }

    fun onCancelDialog() {
        updateState {
            stateValue.copy(
                dialogState = FinalGoalDialogState.None
            )
        }
    }

    override fun createInitialState(): FinalGoalScreenState {
        return FinalGoalScreenState()
    }
}

data class FinalGoalScreenState(
    val finalGoalYear: Int = LocalDate.now().year + GlobalConstants.DEFAULT_FINAL_GOAL_OFFSET,
    val finalGoalText: String = "",
    val dialogState: FinalGoalDialogState = FinalGoalDialogState.None
) {
    val enableSaveButton = finalGoalText.isNotEmpty()

    fun getFinalGoal(): FinalGoal {
        return FinalGoal(
            year = finalGoalYear,
            text = finalGoalText
        )
    }
}

sealed class FinalGoalDialogState {
    object None : FinalGoalDialogState()
    object ExitConfirm : FinalGoalDialogState()
    data class YearPickerDialog(val year: Int = LocalDate.now().year) : FinalGoalDialogState()
}
