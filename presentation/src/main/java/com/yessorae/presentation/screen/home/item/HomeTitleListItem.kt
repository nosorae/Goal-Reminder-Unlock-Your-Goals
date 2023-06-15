package com.yessorae.presentation.screen.home.item

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.presentation.model.TitleListItemModel
import com.yessorae.presentation.model.mockTitleListItemModels

@Composable
fun HomeTitleListItem(
    model: TitleListItemModel,
    onClickAdd: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(
                top = Dimen.MediumDividePadding,
                start = Dimen.SidePadding,
                end = Dimen.SmallDividePadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = model.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        IconButton(onClick = { onClickAdd() }) {
            Icon(
                imageVector = Icons.Filled.AddTask,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun HomeTitleListItem() {
    BasePreview {
        mockTitleListItemModels.forEach {
            HomeTitleListItem(it)
        }
    }
}
