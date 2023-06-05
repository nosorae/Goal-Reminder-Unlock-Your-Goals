package com.yessorae.designsystem.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.theme.GoalReminderTheme


@Composable
fun BasePreview(spacedBy: Dp = 0.dp, content: @Composable ColumnScope.() -> Unit) {
    GoalReminderTheme {
        Column(verticalArrangement = Arrangement.spacedBy(spacedBy)) {
            content()
        }
    }
}