package com.yessorae.presentation.screen.editors

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EditorNumberField(
    modifier: Modifier = Modifier,
    title: String?,
    placeholderText: String,
    textStyle: TextStyle = LocalTextStyle.current,
    onChangeTitle: (String) -> Unit = {},
    singleLine: Boolean = true
) {
    TextField(
        value = title ?: "",
        onValueChange = {
            onChangeTitle(it)
        },
        modifier = modifier,
        textStyle = textStyle.copy(fontWeight = FontWeight.Bold),
        placeholder = {
            Text(text = placeholderText, style = textStyle)
        },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}

