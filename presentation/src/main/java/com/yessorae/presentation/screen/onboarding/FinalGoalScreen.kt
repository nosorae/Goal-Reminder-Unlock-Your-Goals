package com.yessorae.presentation.screen.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.R
import com.yessorae.presentation.buttons.BackgroundTextButton
import com.yessorae.presentation.dialogs.GoalReminderAlertDialog
import com.yessorae.presentation.ext.BottomNavigationBarHeightDp
import com.yessorae.presentation.screen.onboarding.item.FinalGoalTextField
import com.yessorae.presentation.screen.onboarding.item.FinalGoalTopAppBar
import com.yessorae.presentation.screen.onboarding.item.YearPickerDialog
import com.yessorae.util.showToast
import com.yessorae.util.toLocalString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun FinalGoalScreen(
    viewModel: FinalGoalViewModel = hiltViewModel(),
    onBackEvent: () -> Unit
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
            FinalGoalTopAppBar(
                title = stringResource(id = R.string.final_goal_top_app_bar_title),
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
            Margin(dp = Dimen.ExtraLargeDividePadding)

            val contentTextStyle = MaterialTheme.typography.headlineLarge
            val partTextStyle = MaterialTheme.typography.headlineMedium
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(Dimen.DefaultDividePadding)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimen.InsideDividePadding)
                ) {
                    Text(
                        text = model.finalGoalYear.toLocalString() + ",",
                        style = contentTextStyle.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .alignByBaseline()
                            .clip(CircleShape)
                            .clickable { viewModel.onClickYear() }
                            .padding(vertical = Dimen.SmallDividePadding)
                            .padding(
                                start = Dimen.DefaultDividePadding,
                                end = Dimen.SmallDividePadding
                            )
                    )

                    Text(
                        text = stringResource(id = R.string.final_goal_year_word_part),
                        style = partTextStyle,
                        modifier = Modifier.alignByBaseline()
                    )
                }

                Margin(dp = Dimen.DefaultDividePadding)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    FinalGoalTextField(
                        title = model.finalGoalText,
                        onChangeTitle = {
                            viewModel.onChangeYearText(it)
                        },
                        placeholderText = stringResource(id = R.string.final_goal_text_placeholder),
                        textStyle = contentTextStyle,
                        modifier = Modifier.weight(weight = 1f, fill = false)
                    )

                    Text(
                        text = stringResource(id = R.string.final_goal_text_word_part),
                        style = partTextStyle,
                        modifier = Modifier.padding(bottom = Dimen.DefaultDividePadding).wrapContentWidth(),
                        maxLines = 1
                    )
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

    YearPickerDialog(
        showDialog = model.dialogState is FinalGoalDialogState.YearPickerDialog,
        currentYear = (model.dialogState as? FinalGoalDialogState.YearPickerDialog)?.year
            ?: model.finalGoalYear,
        onSelectYear = {
            viewModel.onSelectYear(it)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderAlertDialog(
        showDialog = model.dialogState is FinalGoalDialogState.ExitConfirm,
        text = stringResource(id = R.string.todo_confirm_dialog_title),
        onClickConfirm = {
            viewModel.onConfirmBack()
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )
}