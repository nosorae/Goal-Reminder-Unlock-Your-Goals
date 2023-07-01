package com.yessorae.designsystem.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.yessorae.designsystem.util.ComposableLifecycle

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier
) {
    val widthDp = LocalConfiguration.current.screenWidthDp

    var isDestroyed by remember {
        mutableStateOf(false)
    }
    ComposableLifecycle() { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            isDestroyed = true
        }
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context,
                        widthDp
                    )
                )
                adUnitId = "ca-app-pub-3940256099942544/6300978111"

                loadAd(AdRequest.Builder().build())
            }
        }
    ) {
        if (isDestroyed) {
            it.destroy()
        }
    }

}