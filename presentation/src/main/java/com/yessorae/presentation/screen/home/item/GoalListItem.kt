package com.yessorae.presentation.screen.home.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.R
import com.yessorae.presentation.model.Goal
import com.yessorae.presentation.model.goals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalListItem(
    modifier: Modifier = Modifier,
    goal: Goal,
    onClickGoal: () -> Unit = {},
    onClickOption: () -> Unit = {}
) {
    Box(
        modifier = modifier.clickable { onClickGoal() }
    ) {
        ListItem(
            headlineText = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 24.dp)
                ) {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f, false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Margin(dp = Dimen.InsideDividePadding)
                    goal.subtitle?.let { subtitle ->
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
            supportingText = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.home_goal_progress).format(goal.percent),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                    Margin(Dimen.InsideDividePadding)
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = goal.progress,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
        )

        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { onClickOption }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null,
            )
        }
    }


}


@Preview
@Composable
fun GoalListItem() {
    BasePreview {
        goals.forEach { goal ->
            GoalListItem(goal = goal)
        }
        LinearProgressIndicator(progress = 1.5f)
    }
}
