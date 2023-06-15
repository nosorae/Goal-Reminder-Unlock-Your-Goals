package com.yessorae.presentation.screen.editors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.yessorae.designsystem.theme.Dimen
import kotlinx.coroutines.selects.selectUnbiased



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectableEditorListItem(
    modifier: Modifier = Modifier,
    titleValue: String?,
    placeholderText: String,
    content: @Composable ColumnScope.() -> Unit,
    leadingIcon: ImageVector,
    onClick: () -> Unit = {},
    clickEnabled: Boolean = true

) {
    val icon = ConstrainedLayoutReference("icon")
    val title = ConstrainedLayoutReference(id = "title")
    val contentColumn = ConstrainedLayoutReference(id = "contentColumn")

    ConstraintLayout(
        modifier = modifier
            .clickable(enabled = clickEnabled) { onClick() }
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(vertical = Dimen.MediumDividePadding)
            .padding(start = Dimen.LargeDividePadding, end = Dimen.LargeDividePadding)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier.constrainAs(icon) {
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom,
                    bias = 0f
                )
            }
        )

        val textModifier = Modifier.constrainAs(title) {
            linkTo(top = icon.top, bottom = icon.bottom)
            linkTo(start = icon.end, end = parent.end, startMargin = Dimen.SidePadding)
            width = Dimension.fillToConstraints
        }
        titleValue?.let {
            Text(
                text = titleValue,
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
                modifier = textModifier,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } ?: run {
            Text(text = placeholderText, modifier = textModifier)
        }

        Column(
            modifier = Modifier
                .constrainAs(contentColumn) {
                    linkTo(start = title.start, end = title.end)
                    linkTo(
                        top = title.bottom,
                        bottom = parent.bottom,
                        topMargin = Dimen.MediumDividePadding
                    )
                    width = Dimension.fillToConstraints
                }
        ) {
            content()
        }
    }
}

