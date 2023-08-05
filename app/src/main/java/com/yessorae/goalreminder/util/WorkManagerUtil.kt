package com.yessorae.goalreminder.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.yessorae.goalreminder.background.worker.DailyNotificationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

private fun Context.setDailyNotification() {
    val workManager = WorkManager.getInstance(this)

    val notificationWorkRequest = PeriodicWorkRequest.Builder(
        workerClass = DailyNotificationWorker::class.java,
        repeatInterval = 24,
        repeatIntervalTimeUnit = TimeUnit.HOURS
    )
        .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
        .build()

    workManager.enqueueUniquePeriodicWork(
        DailyNotificationWorker.TAG,
        ExistingPeriodicWorkPolicy.KEEP,
        notificationWorkRequest
    )
}

private fun calculateInitialDelay(): Long {
    val now = Calendar.getInstance()
    val nextMidnight = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    return nextMidnight.timeInMillis - now.timeInMillis
}
