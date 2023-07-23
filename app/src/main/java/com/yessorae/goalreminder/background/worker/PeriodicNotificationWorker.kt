package com.yessorae.goalreminder.background.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yessorae.common.Logger
import com.yessorae.goalreminder.background.PeriodicNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PeriodicNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val notificationManager: PeriodicNotificationManager
) : Worker(context, params) {
    override fun doWork(): Result {
        val title = inputData.getString(PARAM_TITLE) ?: DEFAULT_STRING
        val body = inputData.getString(PARAM_BODY) ?: DEFAULT_STRING

        Log.d("SR-N","title $title, body $body")
        Logger.uiDebug("title $title, body $body")
        notificationManager.apply {
            createNotificationChannel(context = applicationContext)
            showNotification(
                context = applicationContext,
                builder = createNotification(
                    context = applicationContext,
                    title = title,
                    body = body
                )
            )
        }

        return Result.success()
    }

    companion object {
        const val PARAM_TITLE = "title"
        const val PARAM_BODY = "subtitle"
        const val DEFAULT_STRING = ""
    }
}