package com.yessorae.goalreminder.background.periodicalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yessorae.goalreminder.background.PeriodicNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodicNotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: PeriodicNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(PARAM_TITLE) ?: DEFAULT_STRING
        val body = intent.getStringExtra(PARAM_BODY) ?: DEFAULT_STRING

        notificationManager.apply {
            createNotificationChannel(context = context)
            showNotification(
                context = context,
                builder = createNotification(
                    context = context,
                    title = title,
                    body = body
                )
            )
        }
    }

    companion object {
        const val PARAM_TITLE = "title"
        const val PARAM_BODY = "subtitle"
        const val DEFAULT_STRING = ""
    }
}
