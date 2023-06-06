package com.yessorae.presentation.screen.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.yessorae.presentation.R
import com.yessorae.presentation.screen.home.item.HomeTopAppBar
import com.yessorae.presentation.screen.home.item.OverlayPermissionDialog
import com.yessorae.presentation.screen.home.item.TitleListItem
import com.yessorae.util.getMonthDisplay
import com.yessorae.util.getWeekDisplay
import com.yessorae.util.getWeekScopeDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val model by viewModel.state.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(
                title = stringResource(id = R.string.common_final_goal).format(
                    model.finalGoalYear,
                    model.finalGoal
                ),
                onClickEditCalendar = {
                    viewModel.onClickEditCalendar()
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
            ) {
                item {
                    TitleListItem(
                        title = stringResource(
                            id = R.string.common_yearly_goal
                        ).format(model.now.year)
                    )
                }

                item {
                    TitleListItem(
                        title = stringResource(
                            id = R.string.common_monthly_goal
                        ).format(model.now.monthNumber)
                    )
                }

                item {
                    TitleListItem(
                        title = stringResource(
                            id = R.string.common_weekly_goal
                        ).format(model.now.getWeekScopeDisplay())

                    )
                }

                item {
                    TitleListItem(
                        title = stringResource(id = R.string.common_day_todo).format(model.now.dayOfMonth)
                    )
                }


            }
        }
    )

    OverlayPermissionDialog(
        showDialog = model.showOverlayConfirmDialog,
        onOverlayConfirmed = { confirmed ->
            viewModel.onOverlayConfirmed(confirmed)
        },
        onCancelDialog = {
            viewModel.onCancelDialog()
        }
    )
}



