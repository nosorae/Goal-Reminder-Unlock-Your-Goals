package com.yessorae.presentation.screen.editors.goal

import android.content.SharedPreferences.Editor
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlagCircle
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdView
import com.yessorae.designsystem.common.AdmobBanner
import com.yessorae.designsystem.common.ScreenLoadingProgressbar
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.ComposableLifecycle
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.BuildConfig
import com.yessorae.presentation.R
import com.yessorae.presentation.ScreenConstants
import com.yessorae.presentation.buttons.BackgroundTextButton
import com.yessorae.presentation.dialogs.GoalReminderAlertDialog
import com.yessorae.presentation.dialogs.GoalReminderDatePickerDialog
import com.yessorae.presentation.dialogs.OptionListDialog
import com.yessorae.presentation.ext.BottomNavigationBarHeightDp
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.TodoModel
import com.yessorae.presentation.screen.editors.EditorDialogState
import com.yessorae.presentation.screen.editors.EditorNumberField
import com.yessorae.presentation.screen.editors.EditorTextField
import com.yessorae.presentation.screen.editors.EditorTopAppBar
import com.yessorae.presentation.screen.editors.MultiLineEditorListItem
import com.yessorae.presentation.screen.editors.SelectableEditorListItem
import com.yessorae.presentation.screen.editors.SingleLineEditorListItem
import com.yessorae.util.StringModel
import com.yessorae.util.showToast
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
fun GoalEditorScreen(
    viewModel: GoalEditorViewModel = hiltViewModel(),
    onBackEvent: () -> Unit = {}
) {
    val model by viewModel.state.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val context = LocalContext.current

    val adView: MutableState<AdView?> = remember {
        mutableStateOf(null)
    }

    ComposableLifecycle { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            adView.value?.destroy()
            adView.value = null
        }
    }

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
        },
        modifier = Modifier.fillMaxSize()
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
                modifier = Modifier.weight(1f)
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
                    TotalScoreListItem(
                        title = model.totalScore?.toString(),
                        onChangeNumber = { scoreNumber ->
                            viewModel.onChangeTotalScore(scoreNumber)
                        }
                    )
                }

                item {
                    TimeListItem(
                        title = model.dayEditorTitle?.get(context = context),
                        startDay = model.startDate,
                        endDay = model.endDate,
                        onClickDay = {
                            viewModel.onClickDate()
                        },
                        onClickStartDay = {
                            viewModel.onClickStartDate()
                        },
                        onClickEndDay = {
                            viewModel.onClickEndDate()
                        }
                    )
                }

                if (model.showGoalListItem) {
                    item {
                        ContributionGoalListItem(
                            contributeGoal = model.upperGoal,
                            contributeScore = model.upperGoalContributionScore ?: 0,
                            placeholderText = model.upperGoalPlaceholderText,
                            onClickContributeGoal = {
                                viewModel.onClickContributeGoal()
                            },
                            onChangeContributeGoalScore = { score ->
                                viewModel.onChangeUpperGoalContributionScore(score)
                            }
                        )
                    }
                }

                item {
                    MemoListItem(
                        memo = model.memo,
                        onChangeMemo = {
                            viewModel.onChangeMemo(it)
                        }
                    )
                }

                model.lowerItemsTitle?.let { lowerItemsTitle ->
                    item {
                        AdmobBanner(
                            adView = adView.value,
                            onAdViewLoad = {
                                adView.value = it
                            },
                            adId = BuildConfig.ADMOB_EDITOR_BANNER_ID,
                            modifier = Modifier.padding(bottom = Dimen.SmallDividePadding)
                        )
                    }

                    item {
                        SingleLineEditorListItem(
                            leadingIcon = Icons.Filled.SubdirectoryArrowRight,
                            insidePadding = Dimen.DefaultDividePadding
                        ) {
                            Text(text = lowerItemsTitle.get(context))
                        }
                    }

                    if (model.showLowerGoals) {
                        model.lowerGoals?.let { goals ->
                            items(
                                goals,
                                contentType = { GoalModel::class }
                            ) { goal ->
                                GoalEditorListItem(
                                    goalModel = goal,
                                    modifier = Modifier.padding(
                                        start = Dimen.ListItemLeadingIconPadding
                                    )
                                )
                            }
                        }
                    }

                    if (model.showLowerTodos) {
                        model.lowerTodos?.let { todos ->
                            items(
                                items = todos,
                                contentType = { TodoModel::class }
                            ) { todo ->
                                TodoEditorListItem(
                                    todoModel = todo,
                                    modifier = Modifier.padding(
                                        start = Dimen.ListItemLeadingIconPadding
                                    )
                                )
                            }
                        }
                    }
                }
            }

            BackgroundTextButton(
                onClick = { viewModel.onClickSave() },
                enabled = model.enableSaveButton,
                modifier = Modifier
                    .padding(horizontal = Dimen.SidePadding)
                    .padding(
                        bottom = (Dimen.BottomPadding - BottomNavigationBarHeightDp)
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
        initDate = model.paramDate,
        onClickConfirmButton = { milliSec ->
            viewModel.onSelectDate(milliSec = milliSec)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderDatePickerDialog(
        showDialog = model.editorDialogState is EditorDialogState.StartDate,
        initDate = model.startDate ?: model.paramDate,
        onClickConfirmButton = { milliSec ->
            viewModel.onSelectRangeDate(milliSec = milliSec, dialogState = EditorDialogState.StartDate)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderDatePickerDialog(
        showDialog = model.editorDialogState is EditorDialogState.EndDate,
        initDate = model.endDate ?: model.paramDate,
        onClickConfirmButton = { milliSec ->
            viewModel.onSelectRangeDate(milliSec = milliSec, dialogState = EditorDialogState.EndDate)
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
        title =(model.editorDialogState as? EditorDialogState.ContributeGoal)?.title?.get(context)
            ?: stringResource(id = R.string.common_dialog_title_select_default_contribution_title),
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
        placeholderText = stringResource(id = R.string.goal_title_placeholder),
        onChangeTitle = onChangeTitle,
        textStyle = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun TotalScoreListItem(
    title: String?,
    onChangeNumber: (String) -> Unit
) {
    SingleLineEditorListItem(
        leadingIcon = Icons.Filled.TableChart
    ) {
        EditorNumberField(
            title = title ?: "",
            placeholderText = stringResource(id = R.string.goal_total_score_placeholder),
            onChangeTitle = onChangeNumber,
            textStyle = LocalTextStyle.current
        )
    }
}

@Composable
private fun TimeListItem(
    title: String? = null,
    startDay: LocalDate? = null,
    endDay: LocalDate? = null,
    onClickDay: () -> Unit = {},
    onClickStartDay: () -> Unit = {},
    onClickEndDay: () -> Unit = {}
) {
    SelectableEditorListItem(
        titleValue = title,
        placeholderText = stringResource(id = R.string.common_select_date),
        content = {
            TextButton(onClick = { onClickStartDay() }) {
                startDay?.let {
                    Text(
                        text = stringResource(
                            id = R.string.goal_start_date_value
                        ).format(
                            startDay.monthNumber,
                            startDay.dayOfMonth
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                    )
                } ?: run {
                    Text(
                        text = stringResource(id = R.string.goal_start_date_placeholder),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            TextButton(onClick = { onClickEndDay() }) {
                endDay?.let {
                    Text(
                        text = stringResource(
                            id = R.string.goal_end_date_value
                        ).format(
                            endDay.monthNumber,
                            endDay.dayOfMonth
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                    )
                } ?: run {
                    Text(
                        text = stringResource(id = R.string.goal_end_date_placeholder),
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
private fun ContributionGoalListItem(
    contributeGoal: GoalModel?,
    contributeScore: Int = 0,
    placeholderText: StringModel,
    onChangeContributeGoalScore: (Int) -> Unit = {},
    onClickContributeGoal: () -> Unit = {}

) {
    val hasContributeGoal = contributeGoal != null
    val context = LocalContext.current

    SelectableEditorListItem(
        titleValue = contributeGoal?.title,
        placeholderText = placeholderText.get(context),
        content = {
            AnimatedVisibility(visible = hasContributeGoal) {
                Text(
                    text = stringResource(id = R.string.common_contribution_score_title).format(
                        contributeScore,
                        contributeGoal?.totalScore ?: 0
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
fun MemoListItem(
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

@Composable
fun GoalEditorListItem(
    modifier: Modifier = Modifier,
    goalModel: GoalModel
) {
    val context = LocalContext.current
    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = Dimen.ExtraLargeDividePadding)
            ) {
                Text(
                    text = goalModel.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f, false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (goalModel.complete) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
                Margin(dp = Dimen.InsideDividePadding)
                goalModel.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle.get(context),
                        style = MaterialTheme.typography.labelSmall,
                        textDecoration = if (goalModel.complete) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                }
            }
        },
        supportingContent = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Margin(dp = Dimen.InsideDividePadding)
                Text(
                    text = stringResource(
                        id = R.string.home_goal_progress
                    ).format(goalModel.percent),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    textDecoration = if (goalModel.complete) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
                Margin(Dimen.InsideDividePadding)
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = goalModel.progress,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        modifier = modifier
            .alpha(
                if (goalModel.complete) {
                    ScreenConstants.DONE_ALPHA
                } else {
                    1f
                }
            )
    )
}

@Composable
fun TodoEditorListItem(
    modifier: Modifier = Modifier,
    todoModel: TodoModel
) {
    val context = LocalContext.current
    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = Dimen.ExtraLargeDividePadding)
            ) {
                Text(
                    text = todoModel.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f, false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (todoModel.done) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
                Margin(dp = Dimen.InsideDividePadding)
                todoModel.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle.get(context),
                        style = MaterialTheme.typography.labelSmall,
                        textDecoration = if (todoModel.done) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                }
            }
        },
        leadingContent = {
            Checkbox(
                checked = todoModel.done,
                onCheckedChange = null,
                enabled = false
            )
        },
        modifier = modifier
            .alpha(
                if (todoModel.done) {
                    ScreenConstants.DONE_ALPHA
                } else {
                    1f
                }
            )
    )
}
