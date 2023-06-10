package com.yessorae.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .sizeIn(minWidth = Dimen.MinDialogWidth, minHeight = Dimen.MinListDialogHeight)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = title)
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
