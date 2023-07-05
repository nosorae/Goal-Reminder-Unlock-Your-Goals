package com.yessorae.presentation.screen.home.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.R
import com.yessorae.presentation.ScreenConstants
import com.yessorae.presentation.model.GoalWithUpperGoalModel
import com.yessorae.presentation.model.mockGoalDatumModels

@Composable
fun GoalListItem(
    modifier: Modifier = Modifier,
    goalModel: GoalWithUpperGoalModel,
    onClickGoal: () -> Unit = {},
    onClickMore: () -> Unit = {}
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .clickable { onClickGoal() }
            .alpha(
                if (goalModel.goal.complete) {
                    ScreenConstants.DONE_ALPHA
                } else {
                    1f
                }
            )
    ) {
        ListItem(
            headlineContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = Dimen.ExtraLargeDividePadding)
                ) {
                    Text(
                        text = goalModel.goal.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f, false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (goalModel.goal.complete) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                    Margin(dp = Dimen.InsideDividePadding)
                    goalModel.goal.subtitle?.let { subtitle ->
                        Text(
                            text = subtitle.get(context),
                            style = MaterialTheme.typography.labelSmall,
                            textDecoration = if (goalModel.goal.complete) {
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
                    goalModel.contributionText?.let { stringModel ->
                        Text(
                            text = stringModel.get(context),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(
                                top = Dimen.InsideDividePadding,
                                end = Dimen.ExtraLargeDividePadding
                            ),
                            textDecoration = if (goalModel.goal.complete) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            }
                        )
                    }

                    Margin(dp = Dimen.InsideDividePadding)
                    if (goalModel.goal.showProgress) {
                        Text(
                            text = stringResource(
                                id = R.string.home_goal_progress
                            ).format(goalModel.goal.percent),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                            textDecoration = if (goalModel.goal.complete) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            }
                        )
                        Margin(Dimen.InsideDividePadding)
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            progress = goalModel.goal.progress,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.home_goal_progress_none),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                            textDecoration = if (goalModel.goal.complete) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            }
                        )
                    }
                }
            }
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = Dimen.SmallDividePadding),
            onClick = { onClickMore() }
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun GoalListItemPreview() {
    BasePreview {
        mockGoalDatumModels.chunked(2).forEach {
            GoalListItem(
                goalModel = GoalWithUpperGoalModel(
                    goal = it.first(),
                    upperGoal = it.getOrNull(1)
                )
            )
        }
    }
}
