package com.yessorae.presentation.screen.editors.todo

import androidx.lifecycle.SavedStateHandle
import com.yessorae.base.BaseScreenViewModel
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.domain.usecase.GetTodoWithUpperGoalUseCase
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
import com.yessorae.util.toLocalDateTime
import com.yessorae.util.toStartLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime

@HiltViewModel
class TodoEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTodoWithUpperGoalUseCase: GetTodoWithUpperGoalUseCase,
    private val goalRepository: GoalRepository,
    private val todoRepository: TodoRepository
) : BaseScreenViewModel<TodoEditorScreenState>() {
    private val paramTodoId: Int =
        checkNotNull(savedStateHandle[TodoEditorDestination.todoIdArg])
    private val isUpdate: Boolean by lazy {
        paramTodoId != TodoEditorDestination.defaultTodoId
    }
    private val todoDayMilliSecParam: Long =
        checkNotNull(savedStateHandle[TodoEditorDestination.todoDayMilliSecArg])

    init {
        initStateValue()
    }

    private fun initStateValue() = ioScope.launch {
        if (paramTodoId != TodoEditorDestination.defaultTodoId) {
            val model = getTodoWithUpperGoalUseCase(todoId = paramTodoId).asModel()
            updateState {
                stateValue.copy(
                    paramTodoId = paramTodoId,
                    paramTodo = model,
                    todoTitle = model.title,
                    startTime = model.startTime?.time,
                    endTime = model.endTime?.time,
                    contributeGoal = model.upperGoalModel,
                    contributionScore = model.upperGoalContributionScore ?: 0,
                    memo = model.memo
                )
            }
        }

        val date = todoDayMilliSecParam.toLocalDateTime().date
        updateState {
            stateValue.copy(
                paramDate = date
            )
        }
    }

    fun onChangeTitle(title: String) {
        updateState {
            stateValue.copy(
                todoTitle = title,
                changed = true
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
                paramDate = date,
                changed = true
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
                        startTime = time,
                        changed = true
                    )
                }

                EditorDialogState.EndTime -> {
                    stateValue.copy(
                        endTime = time,
                        changed = true
                    )
                }

                else -> stateValue
            }
        }

        onCancelDialog()
    }

    fun onClickContributeGoal() = ioScope.launch {
        goalRepository
            .getWeekdayGoalsFlow(stateValue.paramDate.toStartLocalDateTime())
            .firstOrNull()
            ?.let { goals ->
                if (goals.isEmpty()) {
                    _toast.emit(ResString(R.string.common_no_upper_goal))
                } else {
                    val goalModels = goals.map { it.asModel() }
                    updateState {
                        stateValue.copy(
                            editorDialogState = EditorDialogState.ContributeGoal(goalModels)
                        )
                    }
                }
            }
    }

    fun onSelectContributeGoal(goal: GoalModel) {
        updateState {
            stateValue.copy(
                contributeGoal = goal,
                contributionScore = 0,
                changed = true
            )
        }
        onCancelDialog()
    }

    fun onSelectNoneGoal() {
        updateState {
            stateValue.copy(
                contributeGoal = null,
                contributionScore = 0,
                changed = true
            )
        }
        onCancelDialog()
    }

    fun onChangeContributeGoalScore(score: Int) {
        updateState {
            stateValue.copy(
                contributionScore = score,
                changed = true
            )
        }
    }

    fun onChangeMemo(memo: String) {
        updateState {
            stateValue.copy(
                memo = memo,
                changed = true
            )
        }
    }

    fun onClickBack() = ioScope.launch {
        if (stateValue.changed) {
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
                todoRepository.updateTodoTransaction(it)
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
    val paramTodoId: Int? = null,
    val paramTodo: TodoModel? = null,
    val paramDate: LocalDate = LocalDate.now(),
    val todoTitle: String? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val contributeGoal: GoalModel? = null,
    val contributionScore: Int = 0,
    val memo: String? = null,
    val changed: Boolean = false,
    val editorDialogState: EditorDialogState = EditorDialogState.None
) {
    val enableSaveButton by lazy {
        todoTitle.isNullOrEmpty().not()
    }

    private val paramIsUpdate: Boolean by lazy {
        paramTodoId != null
    }

    val toolbarTitle: StringModel by lazy {
        if (paramIsUpdate) {
            ResString(R.string.todo_edit_toolbar_title)
        } else {
            ResString(R.string.todo_add_toolbar_title)
        }
    }

    fun getTodo(): TodoModel? {
        val title = todoTitle ?: return null
        return paramTodo?.let {
            paramTodo.copy(
                title = title,
                date = paramDate,
                startTime = paramDate.atTime(startTime ?: LocalTime.getStartOfDay()),
                endTime = paramDate.atTime(endTime ?: LocalTime.getStartOfDay()),
                upperGoalModel = contributeGoal,
                upperGoalContributionScore = contributionScore,
                memo = memo
            )
        } ?: run {
            TodoModel(
                title = title,
                date = paramDate,
                startTime = paramDate.atTime(startTime ?: LocalTime.getStartOfDay()),
                endTime = paramDate.atTime(endTime ?: LocalTime.getStartOfDay()),
                upperGoalModel = contributeGoal,
                upperGoalContributionScore = contributionScore,
                memo = memo
            )
        }
    }
}
