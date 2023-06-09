package com.yessorae.presentation.screen.editors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import com.yessorae.designsystem.util.BasePreview

@Composable
fun EditorTextField(
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
        modifier = modifier.fillMaxWidth(),
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
            unfocusedContainerColor = Color.Transparent
        )
    )
}

@Preview
@Composable
fun EditorTextFieldPreview() {
    BasePreview {
        EditorTextField(title = null, placeholderText = "할 일 작성")
        EditorTextField(title = "아침 일기 작성", placeholderText = "할 일 작성")
        EditorTextField(title = "아침 일기 작성아침 일기 작성아침 일기 작성아침 일기 작성아침 일기 작성", placeholderText = "할 일 작성", singleLine = false)
    }

}