package com.yessorae.common

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object Logger {
    private const val UI_TAG = "SRN-UI"
    private const val DATA_TAG = "SRN-DATA"
    private const val INSTANT_TAG = "SRN-INSTANT"

    fun instantDebug(message: String) {
        Timber.tag(INSTANT_TAG).d(message)
    }

    fun instantError(message: String) {
        Timber.tag(INSTANT_TAG).e(message)
    }

    fun uiDebug(message: String) {
        Timber.tag(UI_TAG).d(message)
    }

    fun uiError(message: String) {
        Timber.tag(UI_TAG).e(message)
    }

    fun dataDebug(message: String) {
        Timber.tag(DATA_TAG).d(message)
    }

    fun dataError(message: String) {
        Timber.tag(DATA_TAG).e(message)
    }

    fun recordException(e: Throwable) { // todo test
        Firebase.crashlytics.recordException(e)
    }

    fun logAnalyticsEvent( // todo test
        event: String,
        vararg params: Pair<String, Any?>
    ) {
        Bundle().apply {
            params.forEach { pair ->
                when (val value = pair.second) {
                    is Int -> {
                        putInt(pair.first, value)
                    }

                    is Boolean -> {
                        putBoolean(pair.first, value)
                    }

                    else -> {
                        (value as? String)?.let {
                            putString(pair.first, value)
                        } ?: run {
                            putString(
                                pair.first,
                                value.toString()
                            )
                        }
                    }
                }
            }
        }.also { bundle ->
            Firebase.analytics.logEvent(event, bundle)
        }
    }
}