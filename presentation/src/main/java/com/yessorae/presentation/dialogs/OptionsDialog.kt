package com.yessorae.presentation.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import com.yessorae.designsystem.theme.Dimen

@Composable
fun OptionsDialog(
    title: String,
    onCancel: () -> Unit = {},
    items: LazyListScope.() -> Unit = {}
) {
    Dialog(onDismissRequest = { onCancel() }) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .sizeIn(
                    minWidth = Dimen.MinDialogWidth,
                    maxHeight = Dimen.MaxListDialogHeight
                ),
            color = MaterialTheme.colorScheme.background,
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = Dimen.DefaultDividePadding,
                                bottom = Dimen.SmallDividePadding,
                                start = Dimen.SidePadding
                            ),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                onClick = { onCancel() },
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            )
                            .padding(
                                top = Dimen.DefaultDividePadding,
                                bottom = Dimen.SmallDividePadding,
                                start = Dimen.DefaultDividePadding,
                                end = Dimen.SidePadding
                            )
                    )
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items()
                }
            }
        }
    }
}
