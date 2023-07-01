package com.yessorae.designsystem.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScreenLoadingProgressbar(show: Boolean) {
    ItemLoadingProgressbar(
        modifier = Modifier.fillMaxSize(),
        show = show
    )
}
