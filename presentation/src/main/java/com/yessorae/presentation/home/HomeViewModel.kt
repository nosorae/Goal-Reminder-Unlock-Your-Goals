package com.yessorae.presentation.home

import androidx.lifecycle.ViewModel
import com.yessorae.base.BaseScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseScreenViewModel<HomeScreenState>() {


    fun onOverlayConfirmed(confirmed: Boolean) {
        updateState {
            stateValue.copy(
                showOverlayConfirmDialog = confirmed.not()
            )
        }
    }

    fun onCancelDialog() {
        updateState {
            stateValue.copy(
                showOverlayConfirmDialog = false
            )
        }
    }

    override fun createInitialState(): HomeScreenState {
        return HomeScreenState()
    }
}

data class HomeScreenState(
    val showOverlayConfirmDialog: Boolean = false
)