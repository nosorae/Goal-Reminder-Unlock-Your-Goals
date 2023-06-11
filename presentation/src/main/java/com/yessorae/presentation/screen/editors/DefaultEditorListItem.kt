package com.yessorae.presentation.screen.editors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.Margin

@Composable
fun DefaultEditorListItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    content: @Composable RowScope.() -> Unit,

) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = Dimen.MediumDividePadding, horizontal = Dimen.LargeDividePadding)) {
        Icon(imageVector = leadingIcon, contentDescription = null)
        Margin(dp = Dimen.DefaultDividePadding)
        Row(
            modifier = Modifier.weight(1f)
        ) {
            content()
        }
    }
}
