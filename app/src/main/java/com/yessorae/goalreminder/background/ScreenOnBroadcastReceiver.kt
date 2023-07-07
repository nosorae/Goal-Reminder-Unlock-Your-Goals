package com.yessorae.goalreminder.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yessorae.goalreminder.MainActivity
import com.yessorae.util.startActivityByNewTask
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenOnBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            context.startActivityByNewTask(MainActivity::class.java)
        }
    }
}
