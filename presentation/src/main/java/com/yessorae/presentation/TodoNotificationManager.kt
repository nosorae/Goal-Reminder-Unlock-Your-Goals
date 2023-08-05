package com.yessorae.presentation

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yessorae.common.Logger
import javax.inject.Inject

class TodoNotificationManager @Inject constructor() {
    fun createNotification(
        context: Context,
        title: String,
        body: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_goal_reminder_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(getDefaultPendingIntent(context))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }

    fun showNotification(context: Context, id: Int = 0, builder: NotificationCompat.Builder) =
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            notify(id, builder.build())
        }

    private fun getDefaultPendingIntent(context: Context): PendingIntent {
        Logger.debug("Timber getDefaultPendingIntent")
        Log.e("SR-N", "log getDefaultPendingIntent")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("goalreminder://main")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        Logger.debug("Timber getDefaultPendingIntent")
        Log.e("SR-N", "log getDefaultPendingIntent")
        return pendingIntent
    }

    fun cancel(context: Context, serviceNotificationId: Int) {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            serviceNotificationId
        )
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SERVICE_CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val SERVICE_CHANNEL_ID = "Todo notification channel"
        const val CHANNEL_NAME = "Todo notification channel"
        const val CHANNEL_DESCRIPTION = "task remind"
    }
}
