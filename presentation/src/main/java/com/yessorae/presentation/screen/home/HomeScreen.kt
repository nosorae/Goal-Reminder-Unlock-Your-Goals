package com.yessorae.presentation.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.presentation.R
import com.yessorae.presentation.dialogs.GoalReminderDatePickerDialog
import com.yessorae.presentation.model.GoalModel
import com.yessorae.presentation.model.TitleListItemModel
import com.yessorae.presentation.model.TodoModel
import com.yessorae.presentation.screen.home.item.GoalListItem
import com.yessorae.presentation.screen.home.item.HomeTopAppBar
import com.yessorae.presentation.screen.home.item.OverlayPermissionDialog
import com.yessorae.presentation.screen.home.item.TitleListItem
import com.yessorae.presentation.screen.home.item.TodoListItem
import com.yessorae.util.getWeekScopeDisplay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class HomeTabPage(val index: Int, val titleResId: Int) {
    DAILY_TODO(0, R.string.home_tab_daily_todo),
    WEEKLY_GOAL(1, R.string.home_tab_weekly_goal),
    MONTHLY_GOAL(2, R.string.home_tab_monthly_goal),
    YEARLY_GOAL(3, R.string.home_tab_yearly_goal)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavOutEvent: (String) -> Unit = {}
) {
    val model by viewModel.state.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val pagerState = rememberPagerState()

    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.scrollToPageEvent.collectLatest { page ->
                pagerState.animateScrollToPage(page)
            }
        }

        launch {
            viewModel.navigationEvent.collectLatest { route ->
                route?.let {
                    onNavOutEvent(route)
                }
            }
        }
    }

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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeTabRow(
                pagerState = pagerState,
                onClickTab = { tab ->
                    viewModel.onClickTab(tab)
                }
            )

            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                pageCount = HomeTabPage.values().size,
                state = pagerState
            ) { page ->
                when (page) {
                    HomeTabPage.YEARLY_GOAL.index -> {
                        GoalPage(
                            title = TitleListItemModel(
                                stringResource(
                                    id = R.string.common_yearly_goal
                                ).format(model.now.year)
                            ),
                            goals = model.yearlyGoalModels,
                            onClickMore = { goal ->
                                viewModel.onClickGoalMore(goal = goal)
                            },
                            onClickGoal = { goal ->
                                viewModel.onClickGoal(goal = goal)
                            }
                        )
                    }

                    HomeTabPage.MONTHLY_GOAL.index -> {
                        GoalPage(
                            title = TitleListItemModel(
                                stringResource(
                                    id = R.string.common_monthly_goal
                                ).format(model.now.monthNumber)
                            ),
                            goals = model.monthlyGoalModels,
                            onClickMore = { goal ->
                                viewModel.onClickGoalMore(goal = goal)
                            },
                            onClickGoal = { goal ->
                                viewModel.onClickGoal(goal = goal)
                            }
                        )
                    }

                    HomeTabPage.WEEKLY_GOAL.index -> {
                        GoalPage(
                            title = TitleListItemModel(
                                stringResource(
                                    id = R.string.common_weekly_goal
                                ).format(model.now.getWeekScopeDisplay())
                            ),
                            goals = model.weeklyGoalModels,
                            onClickMore = { goal ->
                                viewModel.onClickGoalMore(goal = goal)
                            },
                            onClickGoal = { goal ->
                                viewModel.onClickGoal(goal = goal)
                            }
                        )
                    }

                    HomeTabPage.DAILY_TODO.index -> {
                        TodoPage(
                            title = TitleListItemModel(
                                stringResource(id = R.string.common_day_todo).format(
                                    model.now.dayOfMonth
                                )
                            ),
                            todos = model.daylyTodoModels,
                            onClickMore = { todo ->
                                viewModel.onClickTodoMore(todo = todo)
                            },
                            onClickCheckBox = { todo ->
                                viewModel.onClickTodoCheckBox(todo = todo)
                            },
                            onClickTodo = { todo ->
                                viewModel.onClickTodo(todo = todo)
                            }
                        )
                    }
                }
            }
        }
    }

    OverlayPermissionDialog(
        showDialog = model.showOverlayConfirmDialog,
        onOverlayConfirmed = { confirmed ->
            viewModel.onOverlayConfirmed(confirmed)
        },
        onCancelDialog = {
            viewModel.onCancelDialog()
        }
    )

    GoalReminderDatePickerDialog(
        showDialog = model.showDatePickerDialog,
        onClickConfirmButton = { timestamp ->
            viewModel.onSelectDate(timestamp)
        },
        onCancel = {
            viewModel.onCancelDialog()
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeTabRow(
    pagerState: PagerState,
    onClickTab: (HomeTabPage) -> Unit
) {
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.dp
            )
        },
        divider = {
            Divider(
                thickness = 1.dp
            )
        }
    ) {
        HomeTabPage.values().forEach {
            LabelTab(
                title = stringResource(id = it.titleResId),
                selected = it.index == pagerState.currentPage,
                onClick = {
                    onClickTab(it)
                }
            )
        }
    }
}

@Composable
fun LabelTab(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Text(
            title,
            style = if (selected) {
                MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            } else {
                MaterialTheme.typography.titleMedium
            },
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground
            },
            modifier = Modifier.padding(
                top = Dimen.DefaultDividePadding,
                bottom = Dimen.DefaultDividePadding
            )
        )
    }
}

@Composable
private fun GoalPage(
    modifier: Modifier = Modifier,
    title: TitleListItemModel,
    goals: List<GoalModel>,
    onClickGoal: (GoalModel) -> Unit = {},
    onClickMore: (GoalModel) -> Unit = {}
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        item(
            contentType = TitleListItemModel::class
        ) {
            TitleListItem(
                model = title
            )
        }
        itemsIndexed(
            items = goals,
            contentType = { _, _ ->
                GoalModel::class
            }
        ) { _, item ->
            GoalListItem(
                goalModel = item,
                onClickGoal = {
                    onClickGoal(item)
                },
                onClickMore = {
                    onClickMore(item)
                }
            )
        }
    }
}

@Composable
private fun TodoPage(
    modifier: Modifier = Modifier,
    title: TitleListItemModel,
    todos: List<TodoModel>,
    onClickTodo: (TodoModel) -> Unit = {},
    onClickMore: (TodoModel) -> Unit = {},
    onClickCheckBox: (TodoModel) -> Unit = {}
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        item(
            contentType = TitleListItemModel::class
        ) {
            TitleListItem(
                model = title
            )
        }
        itemsIndexed(
            items = todos,
            contentType = { _, _ ->
                GoalModel::class
            }
        ) { _, item ->
            TodoListItem(
                todoModel = item,
                onClickTodo = {
                    onClickTodo(item)
                },
                onClickMore = {
                    onClickMore(item)
                },
                onClickCheckBox = {
                    onClickCheckBox(item)
                }
            )
        }
    }
}
