package com.yessorae.goalreminder.background

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.yessorae.goalreminder.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

@AndroidEntryPoint
class ScreenOnService : Service() {

    private lateinit var screenOnOffReceiver: BroadcastReceiver

    @Inject
    lateinit var notification: ScreenOnNotification

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
        setupForegroundService()
        registerScreenOnOffReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notification.cancel(this, FOREGROUND_SERVICE_NOTIFICATION_ID) // todo test
        unregisterReceiver(screenOnOffReceiver)
        job.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun setupForegroundService() {
        notification.createForegroundServiceNotificationChannel(this)

        val noti = notification.createForegroundNotification(
            context = this,
            title = getString(R.string.foreground_service_notification_title),
            body = getString(R.string.foreground_service_notification_body)
        ).build()
        startForeground(FOREGROUND_SERVICE_NOTIFICATION_ID, noti)
    }

    private fun registerScreenOnOffReceiver() {
        val intentFilter = IntentFilter().apply { addAction(Intent.ACTION_SCREEN_OFF) }
        screenOnOffReceiver = ScreenOnBroadcastReceiver()
        registerReceiver(screenOnOffReceiver, intentFilter)
    }

    companion object {
        const val FOREGROUND_SERVICE_NOTIFICATION_ID = 1
    }
}
