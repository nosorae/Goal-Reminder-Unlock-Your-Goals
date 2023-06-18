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
import com.yessorae.util.StringModel
import com.yessorae.util.fromHourMinute
import com.yessorae.util.getStartOfDay
import com.yessorae.util.now
import com.yessorae.util.toDefaultLocalDateTime
import com.yessorae.util.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime

@HiltViewModel
class TodoEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val goalRepository: GoalRepository,
    private val todoRepository: TodoRepository
) : BaseScreenViewModel<TodoEditorScreenState>() {
    private val todoIdParam: Int =
        checkNotNull(savedStateHandle[TodoEditorDestination.todoIdArg])
    private val isUpdate: Boolean by lazy {
        todoIdParam == TodoEditorDestination.defaultTodoId
    }
    private val todoDayMilliSecParam: Long =
        checkNotNull(savedStateHandle[TodoEditorDestination.todoDayMilliSecArg])

    init {
        initStateValue()
    }

    private fun initStateValue() = ioScope.launch {
        if (todoIdParam != TodoEditorDestination.defaultTodoId) {
            val model = todoRepository.getTodo(todoId = todoIdParam).asModel()
            updateState {
                stateValue.copy(
                    paramIsUpdate = true,
                    paramTodo = model,
                    todoTitle = model.title,
                    startTime = model.startTime?.time,
                    endTime = model.endTime?.time,
                    contributeGoal = model.goalModel,
                    contributionScore = model.upperGoalContributionScore ?: 0,
                    memo = model.memo
                )
            }
        }

        if (todoDayMilliSecParam != TodoEditorDestination.defaultTodoDayMilliSec) {
            val date = todoDayMilliSecParam.toLocalDateTime().date
            updateState {
                stateValue.copy(
                    paramDate = date
                )
            }
        }
    }

    fun onChangeTitle(title: String) {
        updateState {
            stateValue.copy(
                todoTitle = title
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
                paramDate = date
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
            .getWeekdayGoalsFlow(stateValue.paramDate.toDefaultLocalDateTime())
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

    fun onChangeMemo(memo: String) {
        updateState {
            stateValue.copy(
                memo = memo
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
    val paramTodo: TodoModel? = null,
    val paramDate: LocalDate = LocalDate.now(),
    val paramIsUpdate: Boolean = false,
    val todoTitle: String? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val contributeGoal: GoalModel? = null,
    val contributionScore: Int = 0,
    val memo: String? = null,
    val editorDialogState: EditorDialogState = EditorDialogState.None
) {
    val enableSaveButton by lazy {
        todoTitle.isNullOrEmpty().not()
    }

    val toolbarTitle: StringModel by lazy {
        if (paramIsUpdate) {
            ResString(R.string.todo_edit_toolbar_title)
        } else {
            ResString(R.string.todo_add_toolbar_title)
        }
    }

    fun getTodo(): TodoModel? {
        return paramTodo?.let {
            todoTitle?.let {
                paramTodo.copy(
                    title = todoTitle,
                    date = paramDate,
                    startTime = paramDate.atTime(startTime ?: LocalTime.getStartOfDay()),
                    endTime = paramDate.atTime(endTime ?: LocalTime.getStartOfDay()),
                    goalModel = contributeGoal,
                    upperGoalContributionScore = contributionScore,
                    memo = memo
                )
            }
        } ?: run {
            todoTitle?.let {
                TodoModel(
                    title = todoTitle,
                    startTime = paramDate.atTime(startTime ?: LocalTime.getStartOfDay()),
                    endTime = paramDate.atTime(endTime ?: LocalTime.getStartOfDay()),
                    goalModel = contributeGoal,
                    upperGoalContributionScore = contributionScore,
                    memo = memo
                )
            }
        }
    }
}
