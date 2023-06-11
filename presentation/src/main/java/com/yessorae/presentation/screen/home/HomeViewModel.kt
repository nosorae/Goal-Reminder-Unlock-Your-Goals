package com.yessorae.presentation.screen.home

import com.yessorae.base.BaseScreenViewModel
import com.yessorae.domain.usecase.GetHomeUseCase
import com.yessorae.presentation.TodoEditorDestination
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.TodoModel
import com.yessorae.presentation.model.asModel
import com.yessorae.util.now
import com.yessorae.util.toDefaultLocalDateTime
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
    private val getHomeUseCase: GetHomeUseCase
) : BaseScreenViewModel<HomeScreenState>() {

    private val _currentDay = MutableStateFlow(LocalDateTime.now())
    val currentDay: StateFlow<LocalDateTime> = _currentDay.asStateFlow()

    private val _scrollToPageEvent = MutableSharedFlow<Int>()
    val scrollToPageEvent: SharedFlow<Int> = _scrollToPageEvent.asSharedFlow()

    init {
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
                    daylyTodoModels = home.dailyTodo.map { it.asModel() }
                )
            }
        }
    }

    fun onOverlayConfirmed(confirmed: Boolean) {
        updateState {
            stateValue.copy(
                showOverlayConfirmDialog = confirmed.not()
            )
        }
    }

    fun onSelectDate(timestamp: Long) {
        _currentDay.value = timestamp.toDefaultLocalDateTime()
        onCancelDialog()
    }

    fun onClickEditCalendar() {
        updateState {
            stateValue.copy(
                showDatePickerDialog = true
            )
        }
    }

    fun onClickTab(tab: HomeTabPage) = ioScope.launch {
        _scrollToPageEvent.emit(tab.index)
    }

    fun onClickGoalMore(goal: GoalModel) {
        // todo impl
    }

    fun onClickGoal(goal: GoalModel) {
        // todo impl
    }

    fun onClickTodo(todo: TodoModel) = ioScope.launch {
       _navigationEvent.emit(TodoEditorDestination.getRouteWithArgs(todoId = todo.todoId, todoDay = stateValue.now.second * 1000L))
    }

    fun onClickAddTodo() = ioScope.launch {
        _navigationEvent.emit(TodoEditorDestination.getRouteWithArgs())
    }

    fun onClickAddGoal() = ioScope.launch {
        // todo impl
    }

    fun onClickTodoMore(todo: TodoModel) {
        // todo impl
    }

    fun onClickTodoCheckBox(todo: TodoModel) {
        // todo impl
    }

    fun onCancelDialog() {
        updateState {
            stateValue.copy(
                showOverlayConfirmDialog = false,
                showDatePickerDialog = false
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
    val yearlyGoalModels: List<GoalModel> = listOf(),
    val monthlyGoalModels: List<GoalModel> = listOf(),
    val weeklyGoalModels: List<GoalModel> = listOf(),
    val daylyTodoModels: List<TodoModel> = listOf(),
    val showOverlayConfirmDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false
)
