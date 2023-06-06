package com.yessorae.presentation.screen.home

import com.yessorae.base.BaseScreenViewModel
import com.yessorae.common.Logger
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.util.combine
import com.yessorae.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository
) : BaseScreenViewModel<HomeScreenState>() {

    init {
        getHomeScreenState()
    }

    private fun getHomeScreenState() = ioScope.launch {
        combine(
            preferencesDatastoreRepository.finalGoal,
            preferencesDatastoreRepository.finalGoalYear,
            ::Pair
        ).collectLatest { (finalGoal, finalGoalYear) ->
            Logger.uiDebug("finalGoal finalGoalYear")
            updateState {
                stateValue.copy(
                    finalGoal = "경제적 자유를 얻고, 선한 영향력을 미치는 사람사람사람사람사람사람사람사람", // todo replace
                    finalGoalYear = 2024
                )
            }
        }
        preferencesDatastoreRepository.finalGoal.collectLatest { finalGoal ->

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
    val showOverlayConfirmDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false
)
