package com.yessorae.presentation.screen.editors.todo

import androidx.lifecycle.SavedStateHandle
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
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
import com.yessorae.presentation.worker.TodoNotificationWorker
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
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import java.util.concurrent.TimeUnit

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

    fun onClickAddAlarm(permissionGranted: Boolean) = ioScope.launch {
        stateValue.startTime ?: run {
            _toast.emit(ResString(R.string.goal_toast_start_time_first_for_alarm))
            return@launch
        }

        if (permissionGranted) {
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
        if (result) {
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

    fun onSelectNotification(alarmType: AlarmType) = ioScope.launch {
        updateState {
            stateValue.copy(
                alarmTypes = stateValue.alarmTypes + setOf(alarmType)
            )
        }
        onCancelDialog()
    }

    fun onClickRemoveAlarm(alarmType: AlarmType) = ioScope.launch {
        updateState {
            stateValue.copy(
                alarmTypes = stateValue.alarmTypes - setOf(alarmType)
            )
        }
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
        stateValue.getTodo()?.let { todoModel ->
            val domainModel = todoModel.asDomainModel()
            showLoading()
            val todoId: Int = if (isUpdate) {
                todoRepository.updateTodoTransaction(domainModel)
                domainModel.todoId
            } else {
                with(AnalyticsConstants) {
                    Logger.logAnalyticsEvent(
                        event = EVENT_INSERT_TODO,
                        PARAM_TITLE to domainModel.title,
                        PARAM_START to "${domainModel.startTime}",
                        PARAM_END to "${domainModel.endTime}",
                        PARAM_HAS_UPPER_GOAL to (domainModel.upperGoalId != null)
                    )
                }

                todoRepository.insertTodo(domainModel)
            }
            handleNotification(todoId = todoId, startTime = todoModel.startTime, alarms = stateValue.alarmTypes)
            hideLoading()
            back()
        }
    }

    private fun handleNotification(todoId:Int, startTime: LocalDateTime?, alarms: Set<AlarmType>) =
        ioScope.launch {
            startTime ?: return@launch

            // todo 실제 DB 수정해서 notificationId 받아와서 전달, 제거된 것은 cancel해줘야함.
            enqueueNotification(
                todoId = todoId,
                notificationId = 0, // todo
                alarms = alarms,
                startTime = startTime
            )
        }

    private fun enqueueNotification(
        todoId: Int,
        notificationId: Int,
        alarms: Set<AlarmType>,
        startTime: LocalDateTime?
    ) = ioScope.launch {
        Logger.error("enqueueNotification $todoId")
        val workRequest = OneTimeWorkRequest.Builder(TodoNotificationWorker::class.java)
            .setInitialDelay(5, TimeUnit.SECONDS)
            .addTag(TodoNotificationWorker.createTag(todoId, notificationId))
            .setInputData(
                Data.Builder()
                    .apply {
                        putInt(TodoNotificationWorker.PARAM_TODO_ID, todoId)
                        putInt(TodoNotificationWorker.PARAM_NOTIFICATION_ID, notificationId)
                    }
                    .build()
            )
            .build()

        val workRequestUUID = workRequest.id
        Logger.error("enqueueNotification workRequestUUID $workRequestUUID")
        _oneTimeWorkRequestEvent.emit(workRequest)
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
    val alarmTypes: Set<AlarmType> = setOf(),
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
