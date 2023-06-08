package com.yessorae.presentation.screen.editors.todo

import com.yessorae.base.BaseScreenViewModel
import com.yessorae.domain.model.Goal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TodoEditorViewModel @Inject constructor() : BaseScreenViewModel<TodoEditorScreenState>() {
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

}