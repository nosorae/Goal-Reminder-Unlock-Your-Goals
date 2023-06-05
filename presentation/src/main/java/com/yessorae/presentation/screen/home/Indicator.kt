package com.yessorae.presentation.screen.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

//@Composable
//fun LinearProgressIndicator(
//    progress: Float,
//    modifier: Modifier = Modifier,
//    color: Color = ProgressIndicatorDefaults.linearColor,
//    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
//) {
//    Canvas(
//        modifier
//            .progressSemantics(progress)
//            .fillMaxWidth()
//            .height(4.dp)
//    ) {
//        val strokeWidth = size.height
//        drawLinearIndicatorTrack(trackColor, strokeWidth)
//        drawLinearIndicator(0f, progress, color, strokeWidth)
//    }
//}
//
//fun DrawScope.drawLinearIndicatorTrack(
//    color: Color,
//    strokeWidth: Float
//) = drawLinearIndicator(0f, 1f, color, strokeWidth)
//
//fun DrawScope.drawLinearIndicator(
//    startFraction: Float,
//    endFraction: Float,
//    color: Color,
//    strokeWidth: Float
//) {
//    val width = size.width
//    val height = size.height
//    // Start drawing from the vertical center of the stroke
//    val yOffset = height / 2
//
//    val isLtr = layoutDirection == LayoutDirection.Ltr
//    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
//    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width
//
//    // Progress line
//    drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
//}