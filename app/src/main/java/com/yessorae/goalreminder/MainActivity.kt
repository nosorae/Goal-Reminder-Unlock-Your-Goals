package com.yessorae.goalreminder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.yessorae.common.Logger
import com.yessorae.goalreminder.background.PeriodicNotificationManager
import com.yessorae.goalreminder.background.ScreenOnService
import com.yessorae.goalreminder.background.worker.PeriodicNotificationWorker
import com.yessorae.goalreminder.util.isServiceRunning
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var notificationManager: PeriodicNotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

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
}
