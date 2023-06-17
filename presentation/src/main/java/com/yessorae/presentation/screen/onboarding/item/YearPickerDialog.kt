package com.yessorae.presentation.screen.onboarding.item

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.presentation.R
import com.yessorae.util.toLocalString
import java.text.NumberFormat

const val YearsInRow = 3

@Composable
fun YearPickerDialog(
    showDialog: Boolean,
    currentYear: Int,
    onSelectYear: (year: Int) -> Unit,
    onCancel: () -> Unit
) {

    var currentSelectedYear: Int by remember {
        mutableStateOf(currentYear)
    }

    if (showDialog) {
        Dialog(onDismissRequest = { onCancel() }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .widthIn(min = Dimen.MinDialogWidth),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = Dimen.LargeDividePadding,
                        bottom = Dimen.DefaultDividePadding
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.common_select_year),
                        modifier = Modifier.padding(horizontal = Dimen.LargeDividePadding),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(Dimen.DefaultDividePadding))

                    YearPickerScreen(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimen.MaxListDialogHeight),
                        currentYear = currentSelectedYear,
                        onYearSelected = {
                            currentSelectedYear = it
                        }
                    )


                    Spacer(modifier = Modifier.height(Dimen.LargeDividePadding))

                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = Dimen.LargeDividePadding)
                    ) {
                        TextButton(
                            onClick = {
                                onCancel()
                            }
                        ) {
                            Text(text = stringResource(id = R.string.common_cancel))
                        }
                        TextButton(
                            onClick = {
                                onSelectYear(currentSelectedYear)
                            }
                        ) {
                            Text(text = stringResource(id = R.string.common_confirm))
                        }
                    }
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearPickerScreen(
    modifier: Modifier = Modifier,
    currentYear: Int,
    onYearSelected: (year: Int) -> Unit
) {
    val yearRange = DatePickerDefaults.YearRange

    val lazyGridState =
        rememberLazyGridState(
            initialFirstVisibleItemIndex = Integer.max(
                0, currentYear - yearRange.first - YearsInRow
            )
        )
    LazyVerticalGrid(
        columns = GridCells.Fixed(YearsInRow),
        modifier = modifier.background(
            color = Color.Transparent
        ),
        state = lazyGridState,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(Dimen.DefaultDividePadding)
    ) {
        items(yearRange.count()) { index ->
            val selectedYear = index + yearRange.first
            val localizedYear = selectedYear.toLocalString()
            Year(
                modifier = Modifier.requiredSize(
                    width = Dimen.SelectionYearContainerWidth,
                    height = Dimen.SelectionYearContainerHeight
                ),
                yearText = localizedYear,
                selected = currentYear == selectedYear,
                onClick = {
                    onYearSelected(selectedYear)
                }
            )
        }
    }
}


@Composable
private fun Year(
    modifier: Modifier = Modifier,
    yearText: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = CircleShape
    Box(
        modifier = modifier
            .background(color = yearContainerColor(selected).value, shape = shape)
            .clip(shape = shape)
            .clickable { onClick() }
            .padding(
                vertical = Dimen.ExtraSmallPadding,
                horizontal = Dimen.InsideDividePadding
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = yearText,
            style = LocalTextStyle.current.copy(fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}


@Composable
fun yearContainerColor(selected: Boolean): State<Color> {
    val target = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
    return animateColorAsState(
        target,
        tween(durationMillis = 100)
    )
}
