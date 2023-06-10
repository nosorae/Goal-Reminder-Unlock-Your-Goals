package com.yessorae.presentation.screen.editors.todo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.R
import com.yessorae.presentation.screen.editors.EditorTextField
import com.yessorae.presentation.screen.editors.EditorTopAppBar
import com.yessorae.util.getWeekDisplay
import com.yessorae.util.now
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Composable
fun TodoEditorScreen(
    viewModel: TodoEditorViewModel = hiltViewModel(),
) {
    val model by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    var appBarTitle: String? by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.collectLatest {
            if (it > 0) {
                appBarTitle = model.title
            } else {
                appBarTitle = ""
            }
        }
    }

    BackHandler(enabled = model.enableSaveButton.not()) {
        viewModel.onClickBack()
    }

    Scaffold(
        topBar = {
            EditorTopAppBar(
                title = appBarTitle,
                enableSaveButton = model.enableSaveButton,
                onClickBack = {
                    viewModel.onClickBack()
                },
                onClickSave = {
                    viewModel.onClickSave()
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimen.SidePadding), state = listState
        ) {
            item {
                TitleListItem(
                    title = model.title,
                    onChangeTitle = {

                    }
                )
            }

            item {
                TimeListItem(
                    allDay = model.allDay,
                    startDay = model.startDate,
                    endDay = model.endDate,
                    startTime = model.startTime,
                    endTime = model.endTime,
                    onClickAllDay = {
                        viewModel.onClickAllDay()
                    },
                    onClickStartDay = {
                        viewModel.onClickStartDate()
                    },
                    onClickStartTime = {
                        viewModel.onClickStartTime()
                    },
                    onClickEndDay = {
                        viewModel.onClickEndDate()
                    },
                    onClickEndTime = {
                        viewModel.onClickEndTime()
                    }
                )
            }

            item {

            }
        }
    }
}

@Composable
private fun TitleListItem(
    title: String?,
    onChangeTitle: (String) -> Unit = {}
) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineContent = {
            EditorTextField(
                title = title ?: "",
                placeholderText = stringResource(id = R.string.todo_title_placeholder),
                onChangeTitle = onChangeTitle,
                textStyle = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
private fun TimeListItem(
    allDay: Boolean = false,
    startDay: LocalDate,
    endDay: LocalDate,
    startTime: LocalDateTime? = null,
    endTime: LocalDateTime? = null,
    onClickAllDay: () -> Unit = {},
    onClickStartDay: () -> Unit = {},
    onClickEndDay: () -> Unit = {},
    onClickStartTime: () -> Unit = {},
    onClickEndTime: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClickAllDay() }
                .padding(start = Dimen.DefaultDividePadding, end = Dimen.LargeDividePadding),
        ) {
            Row(modifier = Modifier.weight(weight = 1f, fill = false)) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null
                )
                Margin(dp = Dimen.DefaultDividePadding)
                Text(text = stringResource(id = R.string.todo_all_day))
            }

            Switch(
                checked = allDay,
                onCheckedChange = { },
                enabled = false,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    disabledCheckedTrackColor = MaterialTheme.colorScheme.primary
                )
            )

        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Dimen.DefaultDividePadding + Dimen.LargeDividePadding,
                    end = Dimen.DefaultDividePadding
                )
        ) {
            TextButton(
                onClick = { onClickStartDay() },
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    text = stringResource(id = R.string.common_date_format).format(
                        startDay.year,
                        startDay.monthNumber,
                        startDay.dayOfMonth,
                        startDay.getWeekDisplay()
                    )
                )
            }
            TextButton(onClick = { onClickStartTime() }) {
                startTime?.let {
                    Text(
                        text = "%02d:%02d".format(startTime.time.hour, startTime.time.minute),
                        color = MaterialTheme.colorScheme.primary,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                    )
                } ?: run {
                    Text(
                        text = stringResource(id = R.string.todo_start_time_placeholder),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Dimen.DefaultDividePadding + Dimen.LargeDividePadding,
                    end = Dimen.DefaultDividePadding
                )
        ) {
            TextButton(
                onClick = { onClickEndDay() },
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    text = stringResource(id = R.string.common_date_format).format(
                        endDay.year,
                        endDay.monthNumber,
                        endDay.dayOfMonth,
                        endDay.getWeekDisplay()
                    )
                )
            }
            TextButton(onClick = { onClickEndTime() }) {
                endTime?.let {
                    Text(
                        text = "%02d:%02d".format(endTime.time.hour, endTime.time.minute),
                        color = MaterialTheme.colorScheme.primary,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                    )
                } ?: run {
                    Text(
                        text = stringResource(id = R.string.todo_end_time_placeholder),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ListItemsPreview() {
    BasePreview {
        TitleListItem(title = "아니")
        TitleListItem(title = "아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다")
        TitleListItem(title = null)

        TimeListItem(startDay = LocalDate.now(), endDay = LocalDate.now())
        TimeListItem(
            startDay = LocalDate.now(),
            endDay = LocalDate.now(),
            startTime = LocalDateTime.now(),
            allDay = true
        )
        TimeListItem(
            startDay = LocalDate.now(),
            endDay = LocalDate.now(),
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now()
        )
    }
}

