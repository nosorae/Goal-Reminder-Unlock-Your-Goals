package com.yessorae.presentation.screen.editors.goal

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlagCircle
import androidx.compose.material.icons.filled.Numbers
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.presentation.R
import com.yessorae.presentation.buttons.BackgroundTextButton
import com.yessorae.presentation.dialogs.GoalReminderAlertDialog
import com.yessorae.presentation.dialogs.GoalReminderDatePickerDialog
import com.yessorae.presentation.dialogs.OptionListDialog
import com.yessorae.presentation.ext.BottomNavigationBarHeightDp
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.screen.editors.EditTextEditorListItem
import com.yessorae.presentation.screen.editors.EditorDialogState
import com.yessorae.presentation.screen.editors.EditorNumberField
import com.yessorae.presentation.screen.editors.EditorTextField
import com.yessorae.presentation.screen.editors.EditorTopAppBar
import com.yessorae.presentation.screen.editors.SelectableEditorListItem
import com.yessorae.util.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.math.roundToInt

@Composable
fun GoalEditorScreen(
    viewModel: GoalEditorViewModel = hiltViewModel(),
    onBackEvent: () -> Unit = {}
) {
    val model by viewModel.state.collectAsState()
    val context = LocalContext.current

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
                title = if (model.isUpdate) {
                    stringResource(id = R.string.goal_edit_toolbar_title)
                } else {
                    stringResource(id = R.string.goal_add_toolbar_title)
                },
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
                        onClickStartDay = {
                            viewModel.onClickStartDate()
                        },
                        onClickEndDay = {
                            viewModel.onClickEndDate()
                        },
                    )
                }

                item {
                    GoalListItem(
                        contributeGoal = model.contributionGoal,
                        contributeScore = model.contributionScore ?: 0,
                        onClickContributeGoal = {
                            viewModel.onClickContributeGoal()
                        },
                        onChangeContributeGoalScore = { score ->
                            viewModel.onChangeContributionScore(score)
                        }
                    )
                }
            }

            BackgroundTextButton(
                onClick = { viewModel.onClickSave() },
                enabled = model.enableSaveButton,
                modifier = Modifier
                    .padding(horizontal = Dimen.SidePadding)
                    .padding(bottom = (Dimen.BottomPadding - BottomNavigationBarHeightDp).value.coerceAtLeast(0f).dp)
                    .imePadding()
                    .fillMaxWidth(),
                text = stringResource(id = R.string.common_save)
            )
        }


    }

    GoalReminderDatePickerDialog(
        showDialog = model.editorDialogState is EditorDialogState.StartDate,
        onClickConfirmButton = { milliSec ->
            viewModel.onSelectDate(milliSec = milliSec, dialogState = EditorDialogState.StartDate)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderDatePickerDialog(
        showDialog = model.editorDialogState is EditorDialogState.EndDate,
        onClickConfirmButton = { milliSec ->
            viewModel.onSelectDate(milliSec = milliSec, dialogState = EditorDialogState.EndDate)
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
        title = stringResource(id = R.string.todo_goal_placeholder),
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
    EditTextEditorListItem(
        leadingIcon = Icons.Filled.Numbers
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
        clickEnabled = false
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
        placeholderText = stringResource(id = R.string.goal_contribution_goal_placeholder),
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
