package com.yessorae.goalreminder.background

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yessorae.goalreminder.MainActivity
import com.yessorae.goalreminder.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// todo make base class
class PeriodicNotificationManager @Inject constructor() {

    fun createNotification(
        context: Context,
        title: String,
        body: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, serviceChannelId)
            .setSmallIcon(R.drawable.ic_goal_reminder_launcher_foreground)
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
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun cancel(context: Context, serviceNotificationId: Int) {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            serviceNotificationId
        )
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                serviceChannelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = channelDescription
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val serviceChannelId = "Periodic notification channel"
        const val channelName = "Periodic notification channel"
        const val channelDescription = "daily remind"
    }
}
