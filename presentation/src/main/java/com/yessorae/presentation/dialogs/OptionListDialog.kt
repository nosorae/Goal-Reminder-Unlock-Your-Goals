package com.yessorae.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.IconButton
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
fun OptionListDialog(
    showDialog: Boolean,
    title: String,
    onCancel: () -> Unit = {},
    items: LazyListScope.() -> Unit = {}
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onCancel() }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .sizeIn(
                        minWidth = Dimen.MinDialogWidth,
                        minHeight = Dimen.MinListDialogHeight,
                        maxHeight = Dimen.MaxListDialogHeight
                    ),
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            ) {
                Column(Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = Dimen.LargeDividePadding,
                                bottom = Dimen.SmallDividePadding
                            )
                            .padding(horizontal = Dimen.SidePadding),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.weight(1f),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        IconButton(onClick = { onCancel() }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                        }
                    }
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items()
                    }
                }
            }
        }
    }
}
