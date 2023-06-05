package com.yessorae.goalreminder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository
) : ViewModel() {
    private val _isServiceOn = MutableSharedFlow<Boolean>()
    val isServiceOn: SharedFlow<Boolean> = _isServiceOn.asSharedFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d("SR-N", "${this::class.java.simpleName} coroutineExceptionHandler $exception")
    }

    private val scope = viewModelScope + coroutineExceptionHandler + Dispatchers.IO

    init {
        getIsServiceOn()
    }

    private fun getIsServiceOn() = scope.launch {
        preferencesDatastoreRepository.isServiceOn.collectLatest {
            _isServiceOn.emit(it)
        }
    }

    fun setServiceOn(on: Boolean) = scope.launch {
        preferencesDatastoreRepository.setServiceOnOff(on = on)
    }
}
