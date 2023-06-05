package com.yessorae.goalreminder.background

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScreenOnService : Service() {

    private lateinit var screenOnOffReceiver: BroadcastReceiver

    @Inject
    lateinit var notification: ScreenOnNotification

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
        val intentFilter = IntentFilter().apply { addAction(Intent.ACTION_SCREEN_ON) }
        screenOnOffReceiver = ScreenOnBroadcastReceiver()
        registerReceiver(screenOnOffReceiver, intentFilter)
    }

    private fun startActivity() {

    }

    companion object {
        const val FOREGROUND_SERVICE_NOTIFICATION_ID = 1
    }
}