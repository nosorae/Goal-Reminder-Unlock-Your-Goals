package com.yessorae.presentation.screen.home

import com.yessorae.base.BaseScreenViewModel
import com.yessorae.common.Logger
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.domain.usecase.GetHomeUseCase
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.TodoModel
import com.yessorae.presentation.model.asModel
import com.yessorae.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

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

    fun onClickEditCalendar() {
        updateState {
            stateValue.copy(
                showDatePickerDialog = true
            )
        }
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
