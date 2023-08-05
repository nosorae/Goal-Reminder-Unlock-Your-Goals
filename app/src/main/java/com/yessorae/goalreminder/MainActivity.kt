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
            OneTimeWorkRequest.Builder(DailyNotificationWorker::class.java)
                .setInitialDelay(10, TimeUnit.SECONDS) // 10초 후에 작업 실행
                .setInputData(Data.Builder().apply {
                    putString(DailyNotificationWorker.PARAM_TITLE, "tenSec Title")
                    putString(DailyNotificationWorker.PARAM_BODY, "tenSec Body")
                }.build())
                .build()

        workManager.enqueue(notificationWorkRequest)
        Logger.getNowSec(" tenSec - enqueue end")
    }


    private fun testWorkManager3() {

        Logger.getNowSec(" enqueue start now")
        val workManager = WorkManager.getInstance(this)

        val notificationWorkRequest = PeriodicWorkRequest.Builder(
            workerClass = DailyNotificationWorker::class.java,
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInitialDelay(1, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            DailyNotificationWorker.TAG,
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
}
