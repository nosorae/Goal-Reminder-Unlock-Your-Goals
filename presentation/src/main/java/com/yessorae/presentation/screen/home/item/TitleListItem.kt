package com.yessorae.presentation.screen.home.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.presentation.model.TitleListItemModel
import com.yessorae.presentation.model.mockTitleListItemModels

@Composable
fun TitleListItem(
    model: TitleListItemModel
) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(
                top = Dimen.MediumDividePadding,
                bottom = Dimen.SmallDividePadding,
                start = Dimen.SidePadding,
                end = Dimen.SidePadding
            )
    ) {
        Text(
            text = model.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun TitleListItem() {
    BasePreview {
        mockTitleListItemModels.forEach {
            TitleListItem(it)
        }
    }
}
