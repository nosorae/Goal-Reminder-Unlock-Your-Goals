package com.yessorae.goalreminder.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class UnlockBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("SR-N", "onReceive")
    }

    companion object {
        const val TAG = "UnlockBroadcastReceiver"
    }
}