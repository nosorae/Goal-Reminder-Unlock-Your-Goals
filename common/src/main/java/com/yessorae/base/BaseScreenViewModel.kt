package com.yessorae.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yessorae.util.StringModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class BaseScreenViewModel<T> : ViewModel() {

    protected val _state = MutableStateFlow(createInitialState())
    val state: StateFlow<T> = _state.asStateFlow()

    protected val stateValue: T get() = state.value

    protected val _toast = MutableSharedFlow<StringModel>()
    val toast: SharedFlow<StringModel> = _toast.asSharedFlow()

    protected val _navigationEvent = MutableSharedFlow<String?>()
    val navigationEvent: SharedFlow<String?> = _navigationEvent.asSharedFlow()

    protected val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    val ioScope = viewModelScope + Dispatchers.IO

    open fun updateState(newState: () -> T) = viewModelScope.launch {
        _state.update {
            newState.invoke()
        }
    }

    protected abstract fun createInitialState(): T
}
