package com.yessorae.presentation.screen.home.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.R
import com.yessorae.presentation.model.Todo
import com.yessorae.presentation.model.mockTodoData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoListItem(
    modifier: Modifier = Modifier,
    todo: Todo,
    onClickCheckBox: () -> Unit = {},
    onClickTodo: () -> Unit = {},
    onClickMore: () -> Unit = {}
) {
    Box(modifier = modifier.clickable {
        onClickTodo()
    }
    ) {
        ListItem(
            headlineText = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 24.dp)
                ) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f, false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Margin(dp = Dimen.InsideDividePadding)
                    todo.subtitle?.let { subtitle ->
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
            supportingText = {
                val goalTitle = todo.goalTitle
                val score = todo.goalContributionScore
                if (goalTitle != null && score != null) {
                    Text(
                        text = stringResource(id = R.string.home_goal_contribution_).format(
                            goalTitle,
                            score
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            leadingContent = {
                Checkbox(
                    checked = todo.completed,
                    onCheckedChange = {
                        onClickCheckBox()
                    }
                )
            }
        )

        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { onClickMore() }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun TodoListItemPreview() {
    BasePreview {
        mockTodoData.forEach { todo ->
            TodoListItem(todo = todo)
        }
    }
}

