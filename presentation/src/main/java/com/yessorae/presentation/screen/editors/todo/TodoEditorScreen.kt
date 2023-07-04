package com.yessorae.presentation.screen.editors.todo

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlagCircle
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yessorae.designsystem.common.ScreenLoadingProgressbar
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.presentation.R
import com.yessorae.presentation.buttons.BackgroundTextButton
import com.yessorae.presentation.dialogs.GoalReminderAlertDialog
import com.yessorae.presentation.dialogs.GoalReminderDatePickerDialog
import com.yessorae.presentation.dialogs.GoalReminderTimePickerDialog
import com.yessorae.presentation.dialogs.NotificationPermissionDialog
import com.yessorae.presentation.dialogs.OptionListDialog
import com.yessorae.presentation.ext.BottomNavigationBarHeightDp
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.mockGoalDatumModels
import com.yessorae.presentation.screen.editors.EditorDialogState
import com.yessorae.presentation.screen.editors.EditorTextField
import com.yessorae.presentation.screen.editors.EditorTopAppBar
import com.yessorae.presentation.screen.editors.MultiLineEditorListItem
import com.yessorae.presentation.screen.editors.SelectableEditorListItem
import com.yessorae.util.getWeekDisplay
import com.yessorae.util.now
import com.yessorae.util.showToast
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun TodoEditorScreen(
    viewModel: TodoEditorViewModel = hiltViewModel(),
    onBackEvent: () -> Unit
) {
    val context = LocalContext.current

    val model by viewModel.state.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
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

    BackHandler {
        viewModel.onClickBack()
    }

    Scaffold(
        topBar = {
            EditorTopAppBar(
                title = model.toolbarTitle.get(context),
                onClickBack = {
                    viewModel.onClickBack()
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(
                        WindowInsetsSides.Bottom
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState
            ) {
                item {
                    TitleListItem(
                        title = model.todoTitle,
                        onChangeTitle = { title ->
                            viewModel.onChangeTitle(title)
                        }
                    )
                }

                item {
                    TimeListItem(
                        day = model.paramDate,
                        startTime = model.startTime,
                        endTime = model.endTime,
                        onClickDay = {
                            viewModel.onClickDate()
                        },
                        onClickStartTime = {
                            viewModel.onClickStartTime()
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

                item {
                    MemoListItem(
                        memo = model.memo,
                        onChangeMemo = {
                            viewModel.onChangeMemo(it)
                        }
                    )
                }
            }

            BackgroundTextButton(
                onClick = { viewModel.onClickSave() },
                enabled = model.enableSaveButton,
                modifier = Modifier
                    .padding(horizontal = Dimen.SidePadding)
                    .padding(
                        bottom = (
                                Dimen.BottomPadding - BottomNavigationBarHeightDp
                                )
                            .value
                            .coerceAtLeast(0f)
                            .dp
                    )
                    .imePadding()
                    .fillMaxWidth(),
                text = stringResource(id = R.string.common_save)
            )
        }
    }

    GoalReminderDatePickerDialog(
        showDialog = model.editorDialogState is EditorDialogState.Date,
        onClickConfirmButton = { milliSec ->
            viewModel.onSelectDate(milliSec = milliSec)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderTimePickerDialog(
        showDialog = model.editorDialogState is EditorDialogState.StartTime,
        onClickConfirmButton = { hour, minute ->
            viewModel.onSelectTime(
                hour = hour,
                minute = minute,
                dialogState = EditorDialogState.StartTime
            )
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderTimePickerDialog(
        showDialog = model.editorDialogState is EditorDialogState.EndTime,
        onClickConfirmButton = { hour, minute ->
            viewModel.onSelectTime(
                hour = hour,
                minute = minute,
                dialogState = EditorDialogState.EndTime
            )
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderAlertDialog(
        showDialog = model.editorDialogState is EditorDialogState.ExitConfirm,
        text = stringResource(id = R.string.todo_confirm_dialog_title),
        onClickConfirm = {
            viewModel.onConfirmBack()
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    OptionListDialog(
        showDialog = model.editorDialogState is EditorDialogState.ContributeGoal,
        title = stringResource(
            id = R.string.common_dialog_title_select_weekly_contribution_goal_title
        ),
        onCancel = {
            viewModel.onCancelDialog()
        }
    ) {
        itemsIndexed(
            items = (model.editorDialogState as? EditorDialogState.ContributeGoal)?.goals
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

        item {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.todo_none_goal))
                },
                modifier = Modifier.clickable {
                    viewModel.onSelectNoneGoal()
                }
            )
        }
    }

    NotificationPermissionDialog(
        needRequestPermission =
        model.editorDialogState is EditorDialogState.NotificationPermission,
        onCompleteNotificationPermissionLogic = { result ->
            viewModel.onPermissionLogicCompleted(result)
        }
    )

    ScreenLoadingProgressbar(show = loading)
}

@Composable
private fun TitleListItem(
    title: String?,
    onChangeTitle: (String) -> Unit = {}
) {
    EditorTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimen.SidePadding),
        title = title ?: "",
        placeholderText = stringResource(id = R.string.todo_title_placeholder),
        onChangeTitle = onChangeTitle,
        textStyle = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun TimeListItem(
    day: LocalDate,
    startTime: LocalTime? = null,
    endTime: LocalTime? = null,
    onClickStartTime: () -> Unit = {},
    onClickEndTime: () -> Unit = {},
    onClickDay: () -> Unit = {}
) {
    SelectableEditorListItem(
        titleValue = stringResource(id = R.string.common_date_format).format(
            day.year,
            day.monthNumber,
            day.dayOfMonth,
            day.getWeekDisplay()
        ),
        placeholderText = stringResource(id = R.string.common_select_date),
        content = {
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
        },
        leadingIcon = Icons.Filled.Schedule,
        onClick = {
            onClickDay()
        }
    )
}

@Composable
private fun GoalListItem(
    contributeGoal: GoalModel?,
    contributeScore: Int = 0,
    onChangeContributeGoalScore: (Int) -> Unit = {},
    onClickContributeGoal: () -> Unit = {}

) {
    val hasContributeGoal = contributeGoal != null
    SelectableEditorListItem(
        titleValue = contributeGoal?.title,
        placeholderText = stringResource(id = R.string.todo_goal_placeholder),
        content = {
            AnimatedVisibility(visible = hasContributeGoal) {
                Text(
                    text = stringResource(id = R.string.common_contribution_score_title).format(
                        contributeScore,
                        contributeGoal?.totalScore
                    ),
                    style = MaterialTheme.typography.labelMedium
                )
                contributeGoal?.let { contributeGoal ->
                    Slider(
                        value = contributeScore.toFloat(),
                        onValueChange = { score ->
                            onChangeContributeGoalScore(score.roundToInt())
                        },
                        valueRange = 0f..contributeGoal.totalScore.toFloat(),
                        modifier = Modifier.padding(top = Dimen.InsideDividePadding)
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

@Composable
private fun MemoListItem(
    memo: String?,
    onChangeMemo: (String) -> Unit
) {
    MultiLineEditorListItem(
        leadingIcon = Icons.Filled.Notes
    ) {
        EditorTextField(
            title = memo ?: "",
            placeholderText = stringResource(id = R.string.todo_description_placeholder),
            textStyle = LocalTextStyle.current,
            onChangeTitle = {
                onChangeMemo(it)
            },
            singleLine = false
        )
    }
}

@Preview
@Composable
fun ListItemsPreview() {
    BasePreview {
        TitleListItem(title = "아니")
        TitleListItem(title = "아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다아니길다")
        TitleListItem(title = null)

        GoalListItem(contributeGoal = mockGoalDatumModels[0])
        GoalListItem(contributeGoal = null)
        GoalListItem(contributeGoal = mockGoalDatumModels[1], contributeScore = 20)

        TimeListItem(day = LocalDate.now())
        TimeListItem(
            day = LocalDate.now(),
            startTime = LocalDateTime.now().time
        )
        TimeListItem(
            day = LocalDate.now(),
            startTime = LocalDateTime.now().time,
            endTime = LocalDateTime.now().time
        )
    }
}
