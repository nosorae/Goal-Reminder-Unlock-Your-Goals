package com.yessorae.goalreminder.background

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
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
        Timber.tag("SR-N").d("service onCreate")
        setupForegroundService()
        registerScreenOnOffReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag("SR-N").d("service onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("SR-N").d("service onDestroy")
        notification.cancel(this, FOREGROUND_SERVICE_NOTIFICATION_ID) // todo test
        unregisterReceiver(screenOnOffReceiver)
        job.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun setupForegroundService() {
        notification.createForegroundServiceNotificationChannel(this)
        val noti = notification.createForegroundNotification(this, "hi", "i am sorae").build()
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
