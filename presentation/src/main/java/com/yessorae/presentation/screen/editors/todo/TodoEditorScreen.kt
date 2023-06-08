package com.yessorae.presentation.screen.editors.todo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TodoEditorScreen(
    viewModel: TodoEditorViewModel = hiltViewModel()
) {
    val model = viewModel.state.collectAsState()
    // todo UI
}