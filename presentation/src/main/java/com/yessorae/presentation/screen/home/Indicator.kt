package com.yessorae.presentation.screen.home

// @Composable
// fun LinearProgressIndicator(
//    progress: Float,
//    modifier: Modifier = Modifier,
//    color: Color = ProgressIndicatorDefaults.linearColor,
//    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
// ) {
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
// }
//
// fun DrawScope.drawLinearIndicatorTrack(
//    color: Color,
//    strokeWidth: Float
// ) = drawLinearIndicator(0f, 1f, color, strokeWidth)
//
// fun DrawScope.drawLinearIndicator(
//    startFraction: Float,
//    endFraction: Float,
//    color: Color,
//    strokeWidth: Float
// ) {
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
// }
