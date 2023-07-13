package com.yessorae.presentation.screen.editors.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.presentation.R
import com.yessorae.presentation.model.enum.AlarmType

@Composable
private fun AlarmListItem(
    modifier: Modifier = Modifier,
    selectedAlarm: List<AlarmType>,
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
                // todo placeholder 알림 추가
            } else {
                // todo AlarmSelectedListItem
            }
        }
    }
}

@Composable
fun AlarmSelectedListItem(
    modifier: Modifier = Modifier,
    onClickDelete: () -> Unit
) {
    Row(modifier = modifier) {
        // todo text
        // todo icon x버튼
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
            .padding(vertical = Dimen.MediumDividePadding,)
    ) {
        Text(
            text = stringResource(id = R.string.common_add_alarm),
            modifier = Modifier.padding(start = Dimen.DefaultDividePadding)
        )
    }
}