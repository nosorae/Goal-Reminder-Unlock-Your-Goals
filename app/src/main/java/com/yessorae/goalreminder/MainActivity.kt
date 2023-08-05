package com.yessorae.goalreminder

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.yessorae.common.Logger
import com.yessorae.goalreminder.background.PeriodicNotificationManager
import com.yessorae.goalreminder.background.worker.DailyNotificationWorker
import com.yessorae.goalreminder.util.setDailyNotification
import com.yessorae.goalreminder.util.startScreenOnOffService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar
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

        setScreen()
        startScreenOnOffService()
        setDailyNotification()
        viewModel.onCreateActivity()
    }

    private fun setScreen() {
        setContent {
            GoalReminderAppScreen()
        }
    }
}
