package com.yessorae.presentation.screen.editors

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopAppBar(
    title: String? = null,
    onClickBack: () -> Unit = {}
) {
    TopAppBar(
        title = {
            title?.let { text ->
                Text(text = text)
            }
        },
        navigationIcon = {
            IconButton(onClick = { onClickBack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }
    )
}
