package com.yessorae.presentation.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalReminderTimePickerDialog(
    showDialog: Boolean = false,
    onClickConfirmButton: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {},
    confirmText: String = stringResource(id = R.string.common_confirm),
    cancelText: String = stringResource(id = R.string.common_cancel)
) {
    val state = rememberTimePickerState()

    if (showDialog) {
        Dialog(
            onDismissRequest = { onCancel() }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = Dimen.LargeDividePadding,
                        start = Dimen.LargeDividePadding,
                        end = Dimen.LargeDividePadding,
                        bottom = Dimen.DefaultDividePadding
                    )
                ) {
                    TimePicker(state = state)

                    Row(
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextButton(
                            onClick = { onCancel() }
                        ) {
                            Text(cancelText)
                        }
                        TextButton(
                            onClick = {
                                onClickConfirmButton(
                                    state.hour,
                                    state.minute
                                )
                            }
                        ) {
                            Text(confirmText)
                        }
                    }
                }
            }
        }
    }
}
