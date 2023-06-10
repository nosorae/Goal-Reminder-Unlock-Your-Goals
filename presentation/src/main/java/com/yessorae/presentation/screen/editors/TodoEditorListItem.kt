package com.yessorae.presentation.screen.editors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.R

@Composable
fun DialogTodoEditorListItem(
    modifier: Modifier = Modifier,
    valueText: String?,
    placeholderText: String,
    leadingIcon: ImageVector?,
    onClick: () -> Unit = {}

) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = Dimen.MediumDividePadding),
        verticalAlignment = Alignment.Top,
    ) {
        Margin(dp = Dimen.DefaultDividePadding)
        leadingIcon?.let {
            Icon(imageVector = leadingIcon, contentDescription = null)
            Margin(dp = Dimen.DefaultDividePadding)
        }

        Column(modifier = Modifier.weight(1f)) {
            valueText?.let {
                Text(
                    text = valueText,
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                )
            } ?: run {
                Text(text = placeholderText)
            }
        }
    }
}


@Composable
fun TodoEditorListItem(
    modifier: Modifier = Modifier,
    titleValue: String?,
    placeholderText: String,
    content: @Composable ColumnScope.() -> Unit,
    leadingIcon: ImageVector?,
    onClick: () -> Unit = {}

) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(vertical = Dimen.MediumDividePadding),
            verticalAlignment = Alignment.Top
        ) {
            Margin(dp = Dimen.DefaultDividePadding)
            leadingIcon?.let {
                Icon(imageVector = leadingIcon, contentDescription = null)
                Margin(dp = Dimen.DefaultDividePadding)
            }
            Column(modifier = Modifier.weight(1f)) {
                titleValue?.let {
                    Text(
                        text = titleValue,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                    )
                } ?: run {
                    Text(text = placeholderText)
                }
                Margin(dp = Dimen.SmallDividePadding)
                content()
            }
            Margin(dp = Dimen.LargeDividePadding)
        }

    }
}

