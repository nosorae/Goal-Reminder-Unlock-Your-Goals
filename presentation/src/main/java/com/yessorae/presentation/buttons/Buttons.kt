package com.yessorae.presentation.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.yessorae.designsystem.theme.Dimen
import kotlinx.coroutines.delay

@Composable
fun BackgroundTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String
) {
    var show by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        delay(100)
        show = true
    }

    AnimatedVisibility(visible = show, enter = slideInVertically { it }) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = Dimen.SmallDividePadding)
            )
        }
    }
}
