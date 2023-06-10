package com.yessorae.presentation.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalReminderAlertDialog(
    showDialog: Boolean,
    text: String,
    cancelText: String = stringResource(id = R.string.common_confirm),
    confirmText: String = stringResource(id = R.string.common_confirm),
    onClickConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onCancel()
            }
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
                        top = 24.dp,
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 16.dp
                    )
                ) {
                    Text(text = text)
                    Margin(Dimen.LargeDividePadding)
                    Row(
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextButton(onClick = { onCancel() }) { Text(cancelText) }
                        TextButton(onClick = { onClickConfirm() }) { Text(confirmText) }
                    }
                }
            }
        }
    }

}