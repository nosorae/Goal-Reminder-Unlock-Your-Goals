package com.yessorae.goalreminder

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GoalReminderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initMobileAdmob()
        plantTimberDebugTree()
    }

    private fun initMobileAdmob() {
        MobileAds.initialize(this)
    }

    private fun plantTimberDebugTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
