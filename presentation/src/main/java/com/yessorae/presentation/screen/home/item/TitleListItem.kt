package com.yessorae.presentation.screen.home.item

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

@Composable
private fun TitleListItem(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = Dimen.LargeDividePadding,
                bottom = Dimen.SmallDividePadding,
                start = Dimen.SidePadding,
                end = Dimen.SidePadding
            )
    ) {
        Text(
            text = title,
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
        TitleListItem(title = "2023년 목표")
        TitleListItem(title = "12월 목표")
        TitleListItem(title = "1월 목표")
    }
}
