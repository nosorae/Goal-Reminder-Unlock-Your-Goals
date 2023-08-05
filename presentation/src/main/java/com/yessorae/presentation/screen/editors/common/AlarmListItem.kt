package com.yessorae.presentation.screen.editors.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Doorbell
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.presentation.R
import com.yessorae.presentation.model.enums.AlarmType

@Composable
fun AlarmListItem(
    modifier: Modifier = Modifier,
    selectedAlarm: Set<AlarmType> = setOf(),
    onClickAdd: () -> Unit = {},
    onClickDelete: (AlarmType) -> Unit = {}
) {
    val context = LocalContext.current

    val isSelectedAlarmEmpty = selectedAlarm.isEmpty()
    val clickableModifier = if (isSelectedAlarmEmpty) {
        modifier.clickable {
            onClickAdd()
        }
    } else {
        modifier
    }

    Column {
        Row(
            modifier = clickableModifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(
                    vertical = Dimen.MediumDividePadding,
                    horizontal = Dimen.LargeDividePadding
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AddAlarm,
                contentDescription = null
            )

                Text(
                    text = stringResource(id = R.string.common_add_alarm),
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = Dimen.DefaultDividePadding
                        )
                )
//            if (isSelectedAlarmEmpty) {
//            } else {
//                selectedAlarm.firstOrNull()?.let { alarm ->
//                    Row(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(
//                                start = Dimen.DefaultDividePadding
//                            ),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = alarm.display.get(context),
//                            modifier = Modifier.weight(1f)
//                        )
//                        Icon(
//                            imageVector = Icons.Filled.Close,
//                            contentDescription = null,
//                            modifier = Modifier
//                                .clickable(
//                                    onClick = { onClickDelete(alarm) },
//                                    indication = null,
//                                    interactionSource = MutableInteractionSource()
//                                )
//                                .padding(Dimen.SmallDividePadding)
//                        )
//                    }
//                }
//            }
        }

        selectedAlarm.forEachIndexed { index, alarm ->
//            if (index != 0) {
                AlarmSelectedListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimen.ListItemLeadingIconPadding,
                            end = Dimen.MediumDividePadding
                        ),
                    alarmType = alarm,
                    onClickDelete = {
                        onClickDelete(alarm)
                    }
                )
//            }
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
    Row(
        modifier = modifier.padding(
            horizontal = Dimen.DefaultDividePadding
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = alarmType.display.get(context),
            modifier = Modifier
                .weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            modifier = Modifier
                .clickable(
                    onClick = { onClickDelete() },
                    indication = null,
                    interactionSource = MutableInteractionSource()
                )
                .padding(Dimen.InsideDividePadding)
        )
    }
}

@Composable
fun AlarmAddListItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(start = Dimen.ListItemLeadingIconPadding, end = Dimen.LargeDividePadding)
            .padding(vertical = 20.dp)
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
            selectedAlarm = setOf(
                AlarmType.ONE_DAY,
            )
        )
        AlarmListItem(
            selectedAlarm = setOf(
                AlarmType.ONE_MINUTE,
                AlarmType.FIFTEEN_MINUTE
            )
        )
    }
}