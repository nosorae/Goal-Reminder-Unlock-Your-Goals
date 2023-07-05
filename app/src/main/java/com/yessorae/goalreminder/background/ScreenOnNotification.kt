package com.yessorae.goalreminder.background

import android.Manifest
import android.app.Notification
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenOnNotification @Inject constructor() {
    fun createToDoNotification(
        context: Context,
        title: String,
        body: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, FOREGROUND_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }

    fun createGoalNotification(
        context: Context,
        title: String,
        body: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, FOREGROUND_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }

    fun createForegroundNotification(
        context: Context,
        title: String,
        body: String
    ): NotificationCompat.Builder {
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, FOREGROUND_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .setOngoing(true) todo plan
            .setPriority(NotificationCompat.PRIORITY_LOW)
    }

    fun createForegroundServiceNotificationChannel(context: Context) {
        createNotificationChannel(
            context = context,
            id = FOREGROUND_SERVICE_CHANNEL_ID,
            name = FOREGROUND_SERVICE_CHANNEL_NAME,
            desc = FOREGROUND_SERVICE_CHANNEL_DESCRIPTION,
            importance = NotificationManager.IMPORTANCE_LOW
        )
    }

    fun showNotification(context: Context, id: Int = 0, builder: Notification) =
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(id, builder)
        }

    fun createGoalNotificationChannel(context: Context) {
        createNotificationChannel(
            context = context,
            id = GOAL_CHANNEL_ID,
            name = GOAL_CHANNEL_NAME,
            desc = GOAL_CHANNEL_DESCRIPTION,
            importance = NotificationManager.IMPORTANCE_HIGH
        )
    }

    fun createTodoNotificationChannel(context: Context) {
        createNotificationChannel(
            context = context,
            id = TODO_CHANNEL_ID,
            name = TODO_CHANNEL_NAME,
            desc = TODO_CHANNEL_DESCRIPTION,
            importance = NotificationManager.IMPORTANCE_HIGH
        )
    }

    private fun getDefaultPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun cancel(context: Context, serviceNotificationId: Int) {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            serviceNotificationId
        )
    }

    private fun createNotificationChannel(
        context: Context,
        id: String,
        name: String,
        desc: String,
        importance: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                id,
                name,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = desc
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val TODO_CHANNEL_ID = "Todo push channel"
        const val TODO_CHANNEL_NAME = "Todo push channel"
        const val TODO_CHANNEL_DESCRIPTION = "give you todo push"
        const val GOAL_CHANNEL_ID = "Goal push channel"
        const val GOAL_CHANNEL_NAME = "Goal push channel"
        const val GOAL_CHANNEL_DESCRIPTION = "give you goal push"
        const val FOREGROUND_SERVICE_CHANNEL_ID = "Foreground service channel"
        const val FOREGROUND_SERVICE_CHANNEL_NAME = "Foreground service channel"
        const val FOREGROUND_SERVICE_CHANNEL_DESCRIPTION = "launch app every screen on"
    }
}
