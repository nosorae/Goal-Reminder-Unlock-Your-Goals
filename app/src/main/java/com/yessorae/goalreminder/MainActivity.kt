package com.yessorae.goalreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.yessorae.common.Logger
import com.yessorae.goalreminder.background.PeriodicNotificationManager
import com.yessorae.goalreminder.background.ScreenOnService
import com.yessorae.goalreminder.background.periodicalarm.PeriodicNotificationReceiver
import com.yessorae.goalreminder.background.worker.PeriodicNotificationWorker
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
//        setDailyNotification()
        testWorkManager2()
        tenSec()
        viewModel.onCreateActivity()
    }

    private fun setScreen() {
        setContent {
            GoalReminderAppScreen()
        }
    }


    private fun tenSec() {
        Logger.getNowSec(" tenSec - enqueue start")
        val workManager = WorkManager.getInstance(this)

        val notificationWorkRequest =
            OneTimeWorkRequest.Builder(PeriodicNotificationWorker::class.java)
                .setInitialDelay(10, TimeUnit.SECONDS) // 10초 후에 작업 실행
                .setInputData(Data.Builder().apply {
                    putString(PeriodicNotificationWorker.PARAM_TITLE, "tenSec Title")
                    putString(PeriodicNotificationWorker.PARAM_BODY, "tenSec Body")
                }.build())
                .build()

        workManager.enqueue(notificationWorkRequest)
        Logger.getNowSec(" tenSec - enqueue end")
    }


    private fun testWorkManager3() {

        Logger.getNowSec(" enqueue start now")
        val workManager = WorkManager.getInstance(this)

        val notificationWorkRequest = PeriodicWorkRequest.Builder(
            workerClass = PeriodicNotificationWorker::class.java,
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInitialDelay(1, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PeriodicNotificationWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )

        Logger.getNowSec("enqueue end")
    }


    private fun testWorkManager2() {

        Logger.getNowSec(" enqueue start")
        val workManager = WorkManager.getInstance(this)

        val notificationWorkRequest = PeriodicWorkRequest.Builder(
            workerClass = PeriodicNotificationWorker::class.java,
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInitialDelay(1, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PeriodicNotificationWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )

        Logger.getNowSec("enqueue end")
    }

//    private fun testWorkManager() {
//        // WorkManager 초기화
//        val workManager = WorkManager.getInstance(this)
//
//        // 매일 반복되는 작업 요청을 생성합니다.
//        val notificationWorkRequest = PeriodicWorkRequest.Builder(
//            PeriodicNotificationWorker::class.java,
//            24, TimeUnit.HOURS
//        ) // 24시간마다 반복
//            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS) // 첫 작업까지의 대기 시간
//            .build()
//
//        // WorkManager에 작업을 등록합니다. ExistingPeriodicWorkPolicy.KEEP는 이미 등록된 동일한 작업이 있다면 새 작업을 무시하라는 정책입니다.
//        workManager.enqueueUniquePeriodicWork(
//            "MidnightNotification",
//            ExistingPeriodicWorkPolicy.KEEP,
//            notificationWorkRequest
//        )
//    }

    fun calculateInitialDelay(): Long {
        val now = Calendar.getInstance()
        val nextMidnight = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        Timber.tag("SR-N").d("")

        return nextMidnight.timeInMillis - now.timeInMillis
    }
}
