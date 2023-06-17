package com.yessorae.presentation.screen.onboarding.item

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.yessorae.presentation.FreeTextField
import com.yessorae.presentation.ext.keyboardAsState

@Composable
fun FinalGoalTextField(
    modifier: Modifier = Modifier,
    title: String,
    onChangeTitle: (String) -> Unit = {},
    placeholderText: String,
    textStyle: TextStyle,
    singleLine: Boolean = true
) {
    var cursorColor by remember {
        mutableStateOf(Color.Transparent)
    }

    val isKeyboardOpen by keyboardAsState()

    cursorColor = if (isKeyboardOpen) {
        MaterialTheme.colorScheme.onBackground
    } else {
        Color.Transparent
    }

    Column {
        FreeTextField(
            value = title,
            onValueChange = {
                onChangeTitle(it)
            },
            modifier = modifier,
            textStyle = textStyle.copy(fontWeight = FontWeight.Bold),
            placeholder = {
                Text(text = placeholderText, style = textStyle)
            },
            singleLine = singleLine,
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = cursorColor
            ),
            textColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = cursorColor
        )
    }
}
