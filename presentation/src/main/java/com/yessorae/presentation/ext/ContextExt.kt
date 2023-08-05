package com.yessorae.presentation.ext

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yessorae.common.Logger

fun Context.redirectToWebBrowser(link: String, onActivityNotFoundException: () -> Unit) {
    Logger.debug("redirectToWebBrowser $link")
    Intent(Intent.ACTION_VIEW, Uri.parse(link)).also { intent ->
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            onActivityNotFoundException()
            Logger.recordException(e)
        } catch (e: Exception) {
            Logger.recordException(e)
        }
    }
}
