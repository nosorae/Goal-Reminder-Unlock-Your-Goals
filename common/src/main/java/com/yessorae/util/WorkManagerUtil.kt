package com.yessorae.util

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun getOneTimeWorkRequest(
    workerClass: Class<out ListenableWorker>,
    delayDurationMillis: Long,
    tagId: String,
    data: Data
): OneTimeWorkRequest {
    return OneTimeWorkRequest.Builder(workerClass)
        .setInitialDelay(delayDurationMillis, TimeUnit.MILLISECONDS)
        .addTag(tag = tagId)
        .setInputData(data)
        .build()
}

fun enqueueOneTimeWork(context: Context, workRequest: OneTimeWorkRequest) {
    WorkManager.getInstance(context).enqueueUniqueWork(
        "${workRequest.id}",
        ExistingWorkPolicy.KEEP,
        workRequest
    )
}

fun cancelWorkByTag(context: Context, tag: String) {
    WorkManager.getInstance(context).cancelAllWorkByTag(tag)
}