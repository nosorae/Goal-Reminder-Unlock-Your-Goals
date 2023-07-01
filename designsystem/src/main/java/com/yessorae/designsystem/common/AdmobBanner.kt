package com.yessorae.designsystem.common

import androidx.compose.animation.animateContentSize
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.yessorae.common.Logger
import com.yessorae.designsystem.util.ComposableLifecycle

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    adId: String
) {
    val widthDp = LocalConfiguration.current.screenWidthDp

    var isDestroyed by remember {
        mutableStateOf(false)
    }
    ComposableLifecycle { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            isDestroyed = true
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context,
                        widthDp
                    )
                )
                adUnitId = adId

                loadAd(AdRequest.Builder().build())
            }
        }
    ) {
        if (isDestroyed) {
            it.destroy()
        }
    }
}

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    adView: AdView? = null,
    onAdViewLoad: (AdView) -> Unit,
    adId: String
) {
    val widthDp = LocalConfiguration.current.screenWidthDp

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        factory = { context ->
            adView ?: AdView(context).apply {
                Logger.uiDebug("AdView load")
                setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context,
                        widthDp
                    )
                )

                adUnitId = adId

                adListener = object: AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        onAdViewLoad(this@apply)
                    }
                }

                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
