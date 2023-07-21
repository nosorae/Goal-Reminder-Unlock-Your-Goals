package com.yessorae.goalreminder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yessorae.common.Logger
import com.yessorae.goalreminder.background.ScreenOnService
import com.yessorae.goalreminder.background.worker.PeriodicNotificationWorker
import com.yessorae.goalreminder.util.isServiceRunning
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)


        Logger.uiDebug("before")
        test()
        setScreen()
        startScreenOnOffService()

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
        Logger.uiDebug("test1")
        val worker = PeriodicWorkRequestBuilder<PeriodicNotificationWorker>(1, TimeUnit.MINUTES)
            .setInputData(
                Data.Builder().apply {
                    putString(PeriodicNotificationWorker.PARAM_TITLE, "테스트 타이틀")
                    putString(PeriodicNotificationWorker.PARAM_BODY, "테스트 바디")
                }.build()
            )
//            .addTag()
            .build()
        Logger.uiDebug("test2")

        WorkManager
            .getInstance(this)
            .enqueue(worker)

        Logger.uiDebug("test3")

    }
}
