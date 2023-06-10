package com.yessorae.presentation.screen.editors.todo

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlagCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.designsystem.util.Margin
import com.yessorae.domain.model.mockGoalModels
import com.yessorae.presentation.R
import com.yessorae.presentation.dialogs.GoalReminderAlertDialog
import com.yessorae.presentation.dialogs.GoalReminderDatePickerDialog
import com.yessorae.presentation.dialogs.GoalReminderTimePickerDialog
import com.yessorae.presentation.dialogs.NotificationPermissionDialog
import com.yessorae.presentation.dialogs.OptionListDialog
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.mockGoalDatumModels
import com.yessorae.presentation.screen.editors.EditorTextField
import com.yessorae.presentation.screen.editors.EditorTopAppBar
import com.yessorae.presentation.screen.editors.TodoEditorListItem
import com.yessorae.util.getWeekDisplay
import com.yessorae.util.now
import com.yessorae.util.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.math.roundToInt

@Composable
fun TodoEditorScreen(
    viewModel: TodoEditorViewModel = hiltViewModel(),
    onBackEvent: () -> Unit
) {
    val context = LocalContext.current

    val model by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    var appBarTitle: String? by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        launch {
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

        launch {
            viewModel.toast.collectLatest {
                context.showToast(it)
            }
        }

        launch {
            viewModel.navigationEvent.collectLatest { route ->
                route?.let {
                    // do nothing
                } ?: run {
                    onBackEvent()
                }
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
                    onChangeTitle = { title ->
                        viewModel.onChangeTitle(title)
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
                GoalListItem(
                    contributeGoal = model.contributeGoal,
                    contributeScore = model.contributionScore,
                    onChangeContributeGoalScore = { score ->
                        viewModel.onChangeContributeGoalScore(score)
                    },
                    onClickContributeGoal = {
                        viewModel.onClickContributeGoal()
                    }
                )
            }
        }
    }

    GoalReminderDatePickerDialog(
        showDialog = model.todoEditorDialogState is TodoEditorDialogState.StartDate,
        onClickConfirmButton = { milliSec ->
            viewModel.onSelectDate(
                milliSec = milliSec,
                dialogState = TodoEditorDialogState.StartDate
            )
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderTimePickerDialog(
        showDialog = model.todoEditorDialogState is TodoEditorDialogState.StartTime,
        onClickConfirmButton = { hour, minute ->
            viewModel.onSelectTime(hour = hour, minute = minute)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderDatePickerDialog(
        showDialog = model.todoEditorDialogState is TodoEditorDialogState.EndDate,
        onClickConfirmButton = { milliSec ->
            viewModel.onSelectDate(milliSec = milliSec, dialogState = TodoEditorDialogState.EndDate)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderTimePickerDialog(
        showDialog = model.todoEditorDialogState is TodoEditorDialogState.EndTime,
        onClickConfirmButton = { hour, minute ->
            viewModel.onSelectTime(hour = hour, minute = minute)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderAlertDialog(
        showDialog = model.todoEditorDialogState is TodoEditorDialogState.ExitConfirm,
        text = stringResource(id = R.string.todo_confirm_dialog_title),
        onClickConfirm = {
            viewModel.onConfirmBack()
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    OptionListDialog(
        showDialog = model.todoEditorDialogState is TodoEditorDialogState.ContributeGoal,
        title = stringResource(id = R.string.todo_goal_placeholder),
        onCancel = {
            viewModel.onCancelDialog()
        }
    ) {
        itemsIndexed(
            items = (model.todoEditorDialogState as? TodoEditorDialogState.ContributeGoal)?.goals
                ?: listOf()
        ) { _, goal ->
            ListItem(
                headlineContent = {
                    Text(text = goal.title)
                },
                modifier = Modifier.clickable {
                    viewModel.onSelectContributeGoal(goal)
                }
            )
        }
    }

    NotificationPermissionDialog(
        needRequestPermission =
        model.todoEditorDialogState is TodoEditorDialogState.NotificationPermission,
        onCompleteNotificationPermissionLogic = { result ->
            viewModel.onPermissionLogicCompleted(result)
        }
    )
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
    startTime: LocalTime? = null,
    endTime: LocalTime? = null,
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
                        text = "%02d:%02d".format(startTime.hour, startTime.minute),
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
                        text = "%02d:%02d".format(endTime.hour, endTime.minute),
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

@Composable
private fun GoalListItem(
    contributeGoal: GoalModel?,
    contributeScore: Int = 0,
    onChangeContributeGoalScore: (Int) -> Unit = {},
    onClickContributeGoal: () -> Unit = {}

) {
    val hasContributeGoal = contributeGoal != null
    TodoEditorListItem(
        titleValue = contributeGoal?.title,
        placeholderText = stringResource(id = R.string.todo_goal_placeholder),
        content = {
            AnimatedVisibility(visible = hasContributeGoal) {
                Text(
                    text = stringResource(id = R.string.todo_contribute_score_title).format(
                        contributeScore,
                        contributeGoal?.totalScore
                    )
                )
                contributeGoal?.let { contributeGoal ->
                    Slider(
                        value = contributeScore.toFloat(),
                        onValueChange = { score ->
                            onChangeContributeGoalScore(score.roundToInt())
                        },
                        valueRange = 0f..contributeGoal.totalScore.toFloat()
                    )
                }

            }
        },
        leadingIcon = if (hasContributeGoal.not()) Icons.Outlined.Flag else Icons.Filled.FlagCircle,
        onClick = {
            onClickContributeGoal()
        }
    )
}

@Preview
@Composable
fun ListItemsPreview() {
    BasePreview {
        TitleListItem(title = "아니")
        TitleListItem(title = "아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다")
        TitleListItem(title = null)

        GoalListItem(contributeGoal = mockGoalDatumModels.get(0))
        GoalListItem(contributeGoal = null)
        GoalListItem(contributeGoal = mockGoalDatumModels.get(1), contributeScore = 20)

        TimeListItem(startDay = LocalDate.now(), endDay = LocalDate.now())
        TimeListItem(
            startDay = LocalDate.now(),
            endDay = LocalDate.now(),
            startTime = LocalDateTime.now().time,
            allDay = true
        )
        TimeListItem(
            startDay = LocalDate.now(),
            endDay = LocalDate.now(),
            startTime = LocalDateTime.now().time,
            endTime = LocalDateTime.now().time
        )
    }
}

