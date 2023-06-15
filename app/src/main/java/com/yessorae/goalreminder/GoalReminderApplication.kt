package com.yessorae.goalreminder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GoalReminderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        plantTimberDebugTree()
    }

    private fun plantTimberDebugTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
