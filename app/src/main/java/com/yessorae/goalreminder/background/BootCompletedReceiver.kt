package com.yessorae.goalreminder.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yessorae.goalreminder.util.setDailyNotification

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            context.setDailyNotification()
        }
    }
}