package com.yessorae.presentation.dialogs

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yessorae.common.Logger
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.presentation.R
import com.yessorae.util.now
import com.yessorae.util.toLocalDateTime
import com.yessorae.util.toMilliSecond
import com.yessorae.util.toStartLocalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinTimeZone
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalReminderDatePickerDialog(
    showDialog: Boolean = false,
    initDate: LocalDate,
    onClickConfirmButton: (timestamp: Long) -> Unit = {},
    onCancel: () -> Unit = {},
    confirmText: String = stringResource(id = R.string.common_confirm),
    cancelText: String = stringResource(id = R.string.common_cancel)
) {
    if (showDialog) {
        val initMillis = initDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        val datePickerState = rememberDatePickerState(
            initialDisplayedMonthMillis = initMillis,
            initialSelectedDateMillis = initMillis
        )
        DatePickerDialog(
            onDismissRequest = {
                onCancel()
            },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { timestamp ->
                        onClickConfirmButton(timestamp)
                    }
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
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview
@Composable
fun DatePickerDialogPreview() {
    BasePreview {
        GoalReminderDatePickerDialog(initDate = LocalDate.now())
    }
}
