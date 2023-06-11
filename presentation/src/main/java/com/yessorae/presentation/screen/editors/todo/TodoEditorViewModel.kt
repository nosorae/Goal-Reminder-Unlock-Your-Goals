package com.yessorae.presentation.screen.editors.todo

import androidx.lifecycle.SavedStateHandle
import com.yessorae.base.BaseScreenViewModel
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.presentation.R
import com.yessorae.presentation.TodoEditorDestination
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.TodoModel
import com.yessorae.presentation.model.asDomainModel
import com.yessorae.presentation.model.asModel
import com.yessorae.presentation.screen.editors.EditorDialogState
import com.yessorae.util.ResString
import com.yessorae.util.fromHourMinute
import com.yessorae.util.getStartOfDay
import com.yessorae.util.now
import com.yessorae.util.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import javax.inject.Inject

@HiltViewModel
class TodoEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val goalRepository: GoalRepository,
    private val todoRepository: TodoRepository
) : BaseScreenViewModel<TodoEditorScreenState>() {
    private val todoId: Int =
        checkNotNull(savedStateHandle[TodoEditorDestination.todoIdArg])
    private val isUpdate: Boolean by lazy {
        todoId == TodoEditorDestination.defaultTodoId
    }
    private val todoDayMilliSec: Long =
        checkNotNull(savedStateHandle[TodoEditorDestination.todoDayMilliSecArg])

    init {
        initStateValue()
    }

    private fun initStateValue() = ioScope.launch {
        if (todoId != TodoEditorDestination.defaultTodoId) {
            val model = todoRepository.getTodo(todoId = todoId)
            updateState {
                stateValue.copy(
                    todoId = todoId,
                    title = model.title
                )
            }
        }

        if (todoDayMilliSec != TodoEditorDestination.defaultTodoDayMilliSec) {
            val date = todoDayMilliSec.toLocalDateTime()
            updateState {
                stateValue.copy(date = date.date)
            }
        }
    }

    fun onChangeTitle(title: String) {
        updateState {
            stateValue.copy(
                title = title
            )
        }
    }

    fun onClickDate() {
        updateState {
            stateValue.copy(
                editorDialogState = EditorDialogState.Date
            )
        }
    }

    fun onClickStartTime() {
        updateState {
            stateValue.copy(
                editorDialogState = EditorDialogState.StartTime
            )
        }
    }

    fun onClickEndTime() {
        updateState {
            stateValue.copy(
                editorDialogState = EditorDialogState.EndTime
            )
        }
    }

    fun onSelectDate(milliSec: Long) {
        val date = milliSec.toLocalDateTime().date
        updateState {
            stateValue.copy(
                date = date
            )
        }
        onCancelDialog()
    }

    fun onSelectTime(hour: Int, minute: Int, dialogState: EditorDialogState) {
        val time = LocalTime.fromHourMinute(hour = hour, minute = minute)
        updateState {
            when (dialogState) {
                EditorDialogState.StartTime -> {
                    stateValue.copy(
                        startTime = time
                    )
                }

                EditorDialogState.EndTime -> {
                    stateValue.copy(
                        endTime = time
                    )
                }

                else -> stateValue
            }
        }

        onCancelDialog()
    }

    fun onClickContributeGoal() = ioScope.launch {
        goalRepository
            .getWeekdayGoalsFlow(stateValue.date.toLocalDateTime())
            .collectLatest { goals ->
                val goalModels = goals.map { it.asModel() }
                updateState {
                    stateValue.copy(
                        editorDialogState = EditorDialogState.ContributeGoal(goalModels)
                    )
                }
            }
    }

    fun onSelectContributeGoal(goal: GoalModel) {
        updateState {
            stateValue.copy(
                contributeGoal = goal,
                contributionScore = 0
            )
        }
        onCancelDialog()
    }

    fun onSelectNoneGoal() {
        updateState {
            stateValue.copy(
                contributeGoal = null,
                contributionScore = 0
            )
        }
        onCancelDialog()
    }

    fun onChangeContributeGoalScore(score: Int) {
        updateState {
            stateValue.copy(
                contributionScore = score
            )
        }
    }

    fun onClickBack() = ioScope.launch {
        if (stateValue.enableSaveButton) {
            updateState {
                stateValue.copy(
                    editorDialogState = EditorDialogState.ExitConfirm
                )
            }
        } else {
            back()
        }
    }

    fun onConfirmBack() = ioScope.launch {
        onCancelDialog()
        back()
    }

    fun onPermissionLogicCompleted(result: Boolean) = ioScope.launch {
        onCancelDialog()
        if (result) {
            _toast.emit(ResString(R.string.todo_toast_will_send_you_notification))
        } else {
            _toast.emit(ResString(R.string.todo_toast_please_alarm_on))
        }
    }

    private suspend fun back() {
        _navigationEvent.emit(null)
    }

    fun onCancelDialog() {
        updateState {
            stateValue.copy(
                editorDialogState = EditorDialogState.None
            )
        }
    }

    fun onClickSave() = ioScope.launch {
        stateValue.getTodo()?.asDomainModel()?.let {
            if (isUpdate) {
                todoRepository.updateTodo(it)
            } else {
                todoRepository.insertTodo(it)
            }
            back()
        }
    }


    override fun createInitialState(): TodoEditorScreenState {
        return TodoEditorScreenState()
    }
}

data class TodoEditorScreenState(
    val todoId: Int? = null,
    val title: String? = null,
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val contributeGoal: GoalModel? = null,
    val contributionScore: Int = 0,
    val memo: String? = null,
    val editorDialogState: EditorDialogState = EditorDialogState.None
) {
    val enableSaveButton by lazy {
        title.isNullOrEmpty().not()
    }

    fun getTodo(): TodoModel? {
        return todoId?.let {
            title?.let {
                TodoModel(
                    todoId = todoId,
                    title = title,
                    date = date,
                    startTime = date.atTime(startTime ?: LocalTime.getStartOfDay()),
                    endTime = date.atTime(endTime ?: LocalTime.getStartOfDay()),
                    goalModel = contributeGoal,
                    goalContributionScore = contributionScore,
                    memo = memo
                )
            }
        } ?: run {
            title?.let {
                TodoModel(
                    title = title,
                    startTime = date.atTime(startTime ?: LocalTime.getStartOfDay()),
                    endTime = date.atTime(endTime ?: LocalTime.getStartOfDay()),
                    goalModel = contributeGoal,
                    goalContributionScore = contributionScore,
                    memo = memo
                )
            }
        }
    }
}
