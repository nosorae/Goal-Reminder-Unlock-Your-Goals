package com.yessorae.goalreminder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.yessorae.common.Logger
import com.yessorae.goalreminder.background.PeriodicNotificationManager
import com.yessorae.goalreminder.background.ScreenOnService
import com.yessorae.goalreminder.background.worker.PeriodicNotificationWorker
import com.yessorae.goalreminder.util.isServiceRunning
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var notificationManager: PeriodicNotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        Logger.uiDebug("before")
        test()
        setScreen()
//        startScreenOnOffService()

        viewModel.onCreateActivity()
    }

    private fun setScreen() {
        setContent {
            GoalReminderAppScreen()
        }
    }

    private fun startScreenOnOffService() {
        Intent(
            this,
            ScreenOnService::class.java
        ).also { intent ->
            if (!isServiceRunning(ScreenOnService::class.java)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            }
        }
    }

    // todo delete
    private fun test() {

        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<PeriodicNotificationWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        WorkManager
            .getInstance(this)

            .enqueue(uploadWorkRequest)


        WorkManager.getInstance(this).getWorkInfoByIdLiveData(uploadWorkRequest.id).observeForever {
            Log.d("SR-N", "$it")
            Logger.uiDebug("$it")
        }
//
//        Log.d("SR-N","test1")
//        Logger.uiDebug("test1")
//        val worker = PeriodicWorkRequestBuilder<PeriodicNotificationWorker>(1, TimeUnit.MINUTES)
//            .setInputData(
//                Data.Builder().apply {
//                    putString(PeriodicNotificationWorker.PARAM_TITLE, "테스트 타이틀")
//                    putString(PeriodicNotificationWorker.PARAM_BODY, "테스트 바디")
//                }.build()
//            )
//            .build()
//        Log.d("SR-N","test2")
//        Logger.uiDebug("test2")
//
//        WorkManager
//            .getInstance(this)
//            .enqueue(worker)
//
//        Log.d("SR-N","test3")
//        Logger.uiDebug("test3")

    }
}
