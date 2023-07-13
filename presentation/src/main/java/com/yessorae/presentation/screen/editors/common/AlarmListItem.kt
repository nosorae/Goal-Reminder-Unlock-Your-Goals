package com.yessorae.presentation.screen.editors.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.presentation.R
import com.yessorae.presentation.model.enums.AlarmType

@Composable
fun AlarmListItem(
    modifier: Modifier = Modifier,
    selectedAlarm: List<AlarmType> = listOf(),
    onClickDelete: (AlarmType) -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(vertical = Dimen.MediumDividePadding)
            .padding(start = Dimen.LargeDividePadding, end = Dimen.LargeDividePadding)
    ) {
        Icon(
            imageVector = Icons.Filled.Alarm,
            contentDescription = null,
            modifier = Modifier.padding(top = Dimen.TextFieldPadding)
        )
        Column(modifier = Modifier.weight(1f)) {
            if (selectedAlarm.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.common_add_alarm),
                    modifier = Modifier.padding(start = Dimen.DefaultDividePadding)
                )
            } else {
                selectedAlarm.forEach {
                    AlarmSelectedListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = Dimen.LargeDividePadding,
                                vertical = Dimen.MediumDividePadding
                            ),
                        alarmType = it,
                        onClickDelete = {
                            onClickDelete(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AlarmSelectedListItem(
    modifier: Modifier = Modifier,
    alarmType: AlarmType,
    onClickDelete: () -> Unit
) {
    val context = LocalContext.current
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = alarmType.display.get(context), modifier = Modifier.weight(1f))
        IconButton(onClick = { onClickDelete() }) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
        }
    }
}

@Composable
fun AlarmAddListItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(start = Dimen.ListItemLeadingIconPadding, end = Dimen.LargeDividePadding)
            .padding(vertical = Dimen.MediumDividePadding)
    ) {
        Text(
            text = stringResource(id = R.string.common_add_alarm),
            modifier = Modifier.padding(start = Dimen.DefaultDividePadding)
        )
    }
}

@Preview
@Composable
fun AlarmListItemPreview() {
    BasePreview {
        AlarmListItem()
        AlarmListItem(
            selectedAlarm = listOf(
                AlarmType.ONE_DAY,
            )
        )
        AlarmListItem(
            selectedAlarm = listOf(
                AlarmType.ONE_MINUTE,
                AlarmType.FIFTEEN_MINUTE
            )
        )
    }
}