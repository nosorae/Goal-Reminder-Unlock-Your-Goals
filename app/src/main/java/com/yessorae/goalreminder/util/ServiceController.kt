package com.yessorae.goalreminder.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.yessorae.goalreminder.background.ScreenOnOffService

fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun Context.startScreenOnOffService() {
    Intent(
        this,
        ScreenOnOffService::class.java
    ).also { intent ->
        if (!isServiceRunning(ScreenOnOffService::class.java)) {
            Log.d("SR-N", "isServiceRunning is true")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }
}

fun Context.stopScreenOnOffService() {
    Intent(
        this,
        ScreenOnOffService::class.java
    ).also { intent ->
        stopService(intent)
    }
}