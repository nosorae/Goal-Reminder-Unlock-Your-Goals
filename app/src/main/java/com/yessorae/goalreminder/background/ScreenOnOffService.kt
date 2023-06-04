package com.yessorae.goalreminder.background

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.content.Context
import android.os.IBinder
import android.util.Log
import com.yessorae.goalreminder.MainActivity

class ScreenOnOffService : Service() {

    private lateinit var screenOnOffReceiver: BroadcastReceiver
    private val notification = ScreenOnOffNotification()

    override fun onCreate() {
        super.onCreate()
        setupForegroundService()
        registerScreenOnOffReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Service START_STICKY to ensure service will run continuously
        // even after the system kills it due to low memory.
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the receiver when the service is destroyed
        unregisterReceiver(screenOnOffReceiver)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun setupForegroundService() {
        notification.createNotificationChannel(this)
        val noti = notification.createForegroundNotification(this, "hi", "i am sorae").build()
        startForeground(1, noti)
    }

    private fun registerScreenOnOffReceiver() {
        // Intent filter with Screen On/Off actions
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }

        // Create a BroadcastReceiver
        screenOnOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_SCREEN_OFF -> {
                        // Screen turned off, do something
                        Log.e("SR-N", "Screen turned OFF")

                    }

                    Intent.ACTION_SCREEN_ON -> {
                        // Screen turned on, do something
                        Log.d("SR-N", "Screen turned ON")
                        val i = Intent(context, MainActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                    }
                }
            }
        }

        // Register the receiver
        registerReceiver(screenOnOffReceiver, intentFilter)
    }

    private fun startActivity() {

    }
}