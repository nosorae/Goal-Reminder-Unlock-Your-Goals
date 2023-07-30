package com.yessorae.goalreminder.background.periodicalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yessorae.goalreminder.background.PeriodicNotificationManager
import com.yessorae.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeriodicNotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: PeriodicNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        val title = context.getString(R.string.daily_noti_title).format()
        val body = context.getString(R.string.daily_noti_body)

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
}
