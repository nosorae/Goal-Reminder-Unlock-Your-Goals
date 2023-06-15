package com.yessorae.presentation.screen.editors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.yessorae.designsystem.theme.Dimen

@Composable
fun MultiLineEditorListItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(vertical = Dimen.MediumDividePadding)
            .padding(start = Dimen.LargeDividePadding, end = Dimen.LargeDividePadding)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier.padding(top = Dimen.TextFieldPadding)
        )
        Column(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}