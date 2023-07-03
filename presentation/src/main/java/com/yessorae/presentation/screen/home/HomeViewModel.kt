package com.yessorae.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.yessorae.base.BaseScreenViewModel
import com.yessorae.domain.model.type.GoalType
import com.yessorae.domain.repository.GoalRepository
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.domain.repository.TodoRepository
import com.yessorae.domain.usecase.CheckTodoTransactionUseCase
import com.yessorae.domain.usecase.GetHomeUseCase
import com.yessorae.presentation.FinalGoalDestination
import com.yessorae.presentation.GoalEditorDestination
import com.yessorae.presentation.TodoEditorDestination
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.GoalWithUpperGoalModel
import com.yessorae.presentation.model.TodoModel
import com.yessorae.presentation.model.asDomainModel
import com.yessorae.presentation.model.asDomainWithGoalModel
import com.yessorae.presentation.model.asModel
import com.yessorae.util.getWeekRangePair
import com.yessorae.util.now
import com.yessorae.util.toLocalDateTime
import com.yessorae.util.toMilliSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: GetHomeUseCase,
    private val goalRepository: GoalRepository,
    private val todoRepository: TodoRepository,
    private val checkTodoTransactionUseCase: CheckTodoTransactionUseCase,
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository
) : BaseScreenViewModel<HomeScreenState>() {

    private val _currentDay = MutableStateFlow(LocalDateTime.now())
    val currentDay: StateFlow<LocalDateTime> = _currentDay.asStateFlow()

    private val _scrollToPageEvent = MutableSharedFlow<Int>()
    val scrollToPageEvent: SharedFlow<Int> = _scrollToPageEvent.asSharedFlow()

    private val _redirectToWebBrowserEvent = MutableSharedFlow<String>()
    val redirectToWebBrowserEvent: SharedFlow<String> = _redirectToWebBrowserEvent

    private val _completeOnBoarding = MutableStateFlow<Boolean?>(null)
    val completeOnBoarding: StateFlow<Boolean?> = _completeOnBoarding.asStateFlow()

    init {
        processOnBoarding()
        getHomeScreenState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getHomeScreenState() = ioScope.launch {
        currentDay.flatMapLatest { curDay ->
            getHomeUseCase(curDay)
        }.collectLatest { home ->
            updateState {
                HomeScreenState(
                    now = currentDay.value,
                    finalGoal = home.finalGoal,
                    finalGoalYear = home.finalGoalYear,
                    yearlyGoalModels = home.yearlyGoal.map { it.asModel() },
                    monthlyGoalModels = home.monthlyGoal.map { it.asModel() },
                    weeklyGoalModels = home.weeklyGoal.map { it.asModel() },
                    dailyTodoModels = home.dailyTodo.map { it.asModel() }
                )
            }
        }
    }

    private fun processOnBoarding() = ioScope.launch {
        preferencesDatastoreRepository.getCompleteOnBoarding().collectLatest { result ->
            _completeOnBoarding.value = result
        }
    }

    fun onOverlayConfirmed(confirmed: Boolean) {
        if (confirmed) {
            onCancelDialog()
        } else {
            updateState {
                stateValue.copy(
                    dialogState = HomeDialogState.OverlayConfirmDialog
                )
            }
        }
    }

    fun onSelectDate(timestamp: Long) {
        _currentDay.value = timestamp.toLocalDateTime()
        onCancelDialog()
    }

    fun onClickToolbarTitle() = ioScope.launch {
        _navigationEvent.emit(FinalGoalDestination.getRouteWithArgs())
    }

    fun onClickEditCalendar() {
        updateState {
            stateValue.copy(
                dialogState = HomeDialogState.DatePickerDialog
            )
        }
    }

    fun onClickNotice() = viewModelScope.launch {
        // todo url 을 remoteConfig 로 관리
        _redirectToWebBrowserEvent.emit(
            "https://nosorae.tistory.com/entry/" +
                    "%EC%9E%91%EC%84%B1%EC%A4%91-%EB%AA%A9%ED%91%9C" +
                    "-%EB%A6%AC%EB%A7%88%EC%9D%B8%EB%8D%94" +
                    "-%EB%8B%B9%EC%8B%A0%EC%9D%98" +
                    "-%EB%AA%A9%ED%91%9C%EB%A5%BC" +
                    "-%EC%9E%A0%EA%B8%88%ED%95%B4%EC%A0%9C" +
                    "-%EC%82%AC%EC%9A%A9%EB%B2%95"
        )
    }

    fun onClickTab(tab: HomeTabPage) = ioScope.launch {
        _scrollToPageEvent.emit(tab.index)
    }

    fun onClickGoal(goal: GoalModel) = ioScope.launch {
        _navigationEvent.emit(
            GoalEditorDestination.getRouteWithArgs(
                goalId = goal.goalId,
                goalDay = goal.startTime?.toMilliSecond() ?: currentDay.value.toMilliSecond(),
                goalType = goal.type
            )
        )
    }

    fun onClickGoalDelete(goal: GoalModel) {
        updateState {
            stateValue.copy(
                dialogState = HomeDialogState.DeleteGoalConfirmDialog(
                    goalModel = goal
                )
            )
        }
    }

    fun onConfirmGoalDelete(dialogState: HomeDialogState.DeleteGoalConfirmDialog) = ioScope.launch {
        goalRepository.deleteGoalTransaction(dialogState.goalModel.asDomainModel())
        onCancelDialog()
    }

    fun onClickAddGoal(goalType: GoalType) = ioScope.launch {
        _navigationEvent.emit(
            GoalEditorDestination.getRouteWithArgs(
                goalDay = stateValue.now.toMilliSecond(),
                goalType = goalType
            )
        )
    }

    fun onClickTodo(todo: TodoModel) = ioScope.launch {
        _navigationEvent.emit(
            TodoEditorDestination.getRouteWithArgs(
                todoId = todo.todoId,
                todoDay = todo.startTime?.toMilliSecond() ?: currentDay.value.toMilliSecond()
            )
        )
    }

    fun onClickAddTodo() = ioScope.launch {
        _navigationEvent.emit(
            TodoEditorDestination.getRouteWithArgs(todoDay = currentDay.value.toMilliSecond())
        )
    }

    fun onClickTodoDelete(todo: TodoModel) {
        updateState {
            stateValue.copy(
                dialogState = HomeDialogState.DeleteTodoConfirmDialog(
                    todoModel = todo
                )
            )
        }
    }

    fun onConfirmTodoDelete(dialogState: HomeDialogState.DeleteTodoConfirmDialog) = ioScope.launch {
        showLoading()
        todoRepository.deleteTodoTransaction(dialogState.todoModel.asDomainModel())
        hideLoading()
        onCancelDialog()
    }

    fun onClickTodoCheckBox(todo: TodoModel) = ioScope.launch {
        checkTodoTransactionUseCase.invoke(todo.asDomainWithGoalModel())
    }

    fun onCancelDialog() {
        updateState {
            stateValue.copy(
                dialogState = HomeDialogState.None
            )
        }
    }

    override fun createInitialState(): HomeScreenState {
        return HomeScreenState()
    }
}

data class HomeScreenState(
    val now: LocalDateTime = LocalDateTime.now(),
    val finalGoal: String = "",
    val finalGoalYear: Int = now.year + 3,
    val yearlyGoalModels: List<GoalWithUpperGoalModel> = listOf(),
    val monthlyGoalModels: List<GoalWithUpperGoalModel> = listOf(),
    val weeklyGoalModels: List<GoalWithUpperGoalModel> = listOf(),
    val dailyTodoModels: List<TodoModel> = listOf(),
    val dialogState: HomeDialogState = HomeDialogState.None
) {
    val weekPair by lazy {
        now.date.getWeekRangePair()
    }
}
