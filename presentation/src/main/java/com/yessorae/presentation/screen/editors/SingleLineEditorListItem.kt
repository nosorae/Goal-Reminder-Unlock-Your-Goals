package com.yessorae.presentation.screen.editors

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.Margin

@Composable
fun SingleLineEditorListItem(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    insidePadding: Dp = 0.dp,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = Dimen.MediumDividePadding,
                horizontal = Dimen.LargeDividePadding
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = leadingIcon, contentDescription = null)
        Margin(dp = insidePadding)
        Row(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
        ) {
            content()
        }
    }
}
