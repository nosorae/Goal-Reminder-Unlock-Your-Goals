package com.yessorae.common

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

}