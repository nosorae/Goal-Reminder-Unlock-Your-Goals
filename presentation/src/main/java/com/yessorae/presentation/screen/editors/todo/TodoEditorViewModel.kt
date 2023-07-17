package com.yessorae.presentation.screen.editors.todo

import androidx.lifecycle.SavedStateHandle
import com.yessorae.base.BaseScreenViewModel
import com.yessorae.common.AnalyticsConstants
import com.yessorae.common.Logger
import com.yessorae.domain.common.DomainConstants
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.domain.usecase.GetTodoWithUpperGoalUseCase
import com.yessorae.presentation.R
import com.yessorae.presentation.TodoEditorDestination
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.TodoModel
import com.yessorae.presentation.model.asDomainModel
import com.yessorae.presentation.model.asModel
import com.yessorae.presentation.model.enums.AlarmType
import com.yessorae.presentation.screen.editors.EditorDialogState
import com.yessorae.util.ResString
import com.yessorae.util.StringModel
import com.yessorae.util.fromHourMinute
import com.yessorae.util.getWeekRangePair
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
                    upperGoal = model.upperGoalModel,
                    upperGoalContributionScore = model.upperGoalContributionScore ?: 0,
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
        val oldDate = stateValue.paramDate
        val newDate = milliSec.toLocalDateTime().date
        val oldWeekRangePair = oldDate.getWeekRangePair()
        val newUpperGoal: GoalModel? =
            if (newDate !in oldWeekRangePair.first..oldWeekRangePair.second) {
                null
            } else {
                stateValue.upperGoal
            }

        updateState {
            stateValue.copy(
                paramDate = newDate,
                changed = true,
                upperGoal = newUpperGoal,
                upperGoalContributionScore = if (newUpperGoal == null) {
                    DomainConstants.DEFAULT_UPPER_GOAL_CONTRIBUTION_SCORE
                } else {
                    stateValue.upperGoalContributionScore
                }
            )
        }

        onCancelDialog()
    }

    fun onSelectTime(hour: Int, minute: Int, dialogState: EditorDialogState) = ioScope.launch {
        val time = LocalTime.fromHourMinute(hour = hour, minute = minute)
        val newTimeMin = (hour * 60) + minute

        when (dialogState) {
            EditorDialogState.StartTime -> {
                stateValue.endTime?.let { endTime ->
                    val endTimeMin = (endTime.hour * 60) + endTime.minute
                    if (endTimeMin < newTimeMin) {
                        _toast.emit(ResString(R.string.goal_toast_out_of_order_start_time))
                        return@launch
                    }
                }
                updateState {
                    stateValue.copy(
                        startTime = time,
                        changed = true
                    )
                }
            }

            EditorDialogState.EndTime -> {
                stateValue.startTime?.let { startTime ->
                    val startTimeMin = (startTime.hour * 60) + startTime.minute
                    if (startTimeMin > newTimeMin) {
                        _toast.emit(ResString(R.string.goal_toast_out_of_order_end_time))
                        return@launch
                    }
                }
                updateState {
                    stateValue.copy(
                        endTime = time,
                        changed = true
                    )
                }
            }

            else -> {}
        }

        onCancelDialog()
    }

    fun onClickAddAlarm(permissionGranted: Boolean) {
        if (permissionGranted) {
            // todo goalId 로 이미 저장된 알람타입들 제거하고 Alarms 파라미터에 전달
            updateState {
                stateValue.copy(
                    editorDialogState = EditorDialogState.Alarms()
                )
            }
        } else {
            updateState {
                stateValue.copy(
                    editorDialogState = EditorDialogState.NotificationPermission
                )
            }
        }
    }

    fun onPermissionLogicCompleted(result: Boolean) = ioScope.launch {
        Logger.uiDebug("onPermissionLogicCompleted result $result")
        if (result) {
            // todo goalId 로 이미 저장된 알람타입들 제거하고 Alarms 파라미터에 전달
            updateState {
                stateValue.copy(
                    editorDialogState = EditorDialogState.Alarms()
                )
            }
            _toast.emit(ResString(R.string.todo_toast_will_send_you_notification))
        } else {
            _toast.emit(ResString(R.string.todo_toast_please_alarm_on))
        }

        onCancelDialog()
    }

    fun onSelectNotification(alarmType: AlarmType) {
        // todo impl
    }

    fun onDeleteNotification(alarmType: AlarmType) {
        // todo impl
    }

    fun onClickContributeGoal() = ioScope.launch {
        goalRepository
            .getWeekdayGoalsFlow(stateValue.paramDate.toStartLocalDateTime())
            .firstOrNull()
            ?.let { goals ->
                if (goals.isEmpty()) {
                    _toast.emit(ResString(R.string.common_no_upper_weekly_goal))
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
                upperGoal = goal,
                upperGoalContributionScore = 0,
                changed = true
            )
        }
        onCancelDialog()
    }

    fun onSelectNoneGoal() {
        updateState {
            stateValue.copy(
                upperGoal = null,
                upperGoalContributionScore = 0,
                changed = true
            )
        }
        onCancelDialog()
    }

    fun onChangeContributeGoalScore(score: Int) {
        updateState {
            stateValue.copy(
                upperGoalContributionScore = score,
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
            showLoading()
            if (isUpdate) {
                todoRepository.updateTodoTransaction(it)
            } else {
                with(AnalyticsConstants) {
                    Logger.logAnalyticsEvent(
                        event = EVENT_INSERT_TODO,
                        PARAM_TITLE to it.title,
                        PARAM_START to "${it.startTime}",
                        PARAM_END to "${it.endTime}",
                        PARAM_HAS_UPPER_GOAL to (it.upperGoalId != null)
                    )
                }
                todoRepository.insertTodo(it)
            }
            hideLoading()
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
    val upperGoal: GoalModel? = null,
    val upperGoalContributionScore: Int = DomainConstants.DEFAULT_UPPER_GOAL_CONTRIBUTION_SCORE,
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
                startTime = startTime?.let { time -> paramDate.atTime(time) },
                endTime = endTime?.let { it1 -> paramDate.atTime(it1) },
                upperGoalModel = upperGoal,
                upperGoalContributionScore = upperGoalContributionScore,
                memo = memo
            )
        } ?: run {
            TodoModel(
                title = title,
                date = paramDate,
                startTime = startTime?.let { paramDate.atTime(it) },
                endTime = endTime?.let { paramDate.atTime(it) },
                upperGoalModel = upperGoal,
                upperGoalContributionScore = upperGoalContributionScore,
                memo = memo
            )
        }
    }
}
