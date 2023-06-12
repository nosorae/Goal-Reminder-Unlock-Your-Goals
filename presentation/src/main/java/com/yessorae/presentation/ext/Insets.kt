package com.yessorae.presentation.ext

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

val BottomNavigationBarHeightDp
    @Composable
    get() = with(LocalDensity.current) {
        WindowInsets.systemBars.getBottom(this).toDp()
    }

val StatusBarHeightDp
    @Composable
    get() = with(LocalDensity.current) {
        WindowInsets.systemBars.getTop(this).toDp()
    }