package com.yessorae.goalreminder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkRequest
import com.yessorae.domain.repository.PreferencesDatastoreRepository
import com.yessorae.goalreminder.util.InitDataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.time.Duration

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesDatastoreRepository: PreferencesDatastoreRepository,
    private val initialDataHelper: InitDataHelper
) : ViewModel() {
    private val _isServiceOn = MutableSharedFlow<Boolean>()
    val isServiceOn: SharedFlow<Boolean> = _isServiceOn.asSharedFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d("SR-N", "${this::class.java.simpleName} coroutineExceptionHandler $exception")
    }

    private val scope = viewModelScope + coroutineExceptionHandler + Dispatchers.IO

    fun onCreateActivity() {
        getIsServiceOn()
        processOnBoardingMockData()
    }

    private fun getIsServiceOn() = scope.launch {
        preferencesDatastoreRepository.getServiceOnOff().collectLatest {
            _isServiceOn.emit(it)
        }
    }

    fun setServiceOn(on: Boolean) = scope.launch {
        preferencesDatastoreRepository.setServiceOnOff(on = on)
    }

    private fun processOnBoardingMockData() = scope.launch {
        preferencesDatastoreRepository.getCompleteOnBoarding().firstOrNull()?.let {
            if (it.not()) {
                initialDataHelper.processOnBoardingData()
            }
        }
    }
}
