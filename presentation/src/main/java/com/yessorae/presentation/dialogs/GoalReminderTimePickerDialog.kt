package com.yessorae.presentation.dialogs

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yessorae.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalReminderTimePickerDialog(
    showDialog: Boolean = false,
    onClickConfirmButton: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {},
    confirmText: String = stringResource(id = R.string.common_confirm),
    cancelText: String = stringResource(id = R.string.common_cancel),
) {
    val state = rememberTimePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { onCancel() },
            confirmButton = {
                TextButton(onClick = {
                    onClickConfirmButton(state.hour, state.minute)
                }) {
                    Text(
                        text = confirmText
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onCancel() }) {
                    Text(
                        text = cancelText
                    )
                }
            },
        ) {
            TimePicker(state = state)
        }
    }
}