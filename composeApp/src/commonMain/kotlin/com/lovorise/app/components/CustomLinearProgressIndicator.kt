package com.lovorise.app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import com.lovorise.app.ui.CARD_BG_DARK

@Composable
fun CustomLinearProgressIndicator(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    color: Color = Color(0XFFF33358),
    backgroundColor: Color = Color(0XFFEAECF0),
    strokeWidth: Dp
) {
    Canvas(modifier = modifier.height(strokeWidth)) {
        val width = size.width
        val height = size.height

        // Draw background line
        drawLine(
            color = backgroundColor,
            start = Offset(0f, height / 2),
            end = Offset(width, height / 2),
            strokeWidth = strokeWidth.toPx(),
            cap = StrokeCap.Round
        )

        if (progress > 0) {
            // Draw progress line
            drawLine(
                color = color,
                start = Offset(0f, height / 2),
                end = Offset(width * progress, height / 2),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}


@Composable
fun CustomLinearProgressIndicator(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    strokeWidth: Dp,
) {
    Canvas(modifier = modifier.height(strokeWidth)) {
        val width = size.width
        val height = size.height

        // Draw background line
        drawLine(
            color = if(isDarkMode) CARD_BG_DARK else Color(0XFFEAECF0),
            start = Offset(0f, height / 2),
            end = Offset(width, height / 2),
            strokeWidth = strokeWidth.toPx(),
            cap = StrokeCap.Round
        )

        if (progress > 0) {
            // Draw progress line
            drawLine(
                color = Color(0XFFF33358),
                start = Offset(0f, height / 2),
                end = Offset(width * progress, height / 2),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}