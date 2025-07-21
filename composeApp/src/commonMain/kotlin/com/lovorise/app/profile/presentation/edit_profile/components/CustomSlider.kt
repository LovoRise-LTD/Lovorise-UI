package com.lovorise.app.profile.presentation.edit_profile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.lovorise.app.ui.CARD_BG_DARK
import kotlin.math.absoluteValue


@Composable
fun SingleThumbSlider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    value: Float,
    onValueChange: (Float) -> Unit,
    isDarkMode:Boolean
) {
    var thumbValue by remember(value) { mutableStateOf(value) }
    var trackWidth by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    val newValue = (change.position.x / trackWidth) *
                            (valueRange.endInclusive - valueRange.start) + valueRange.start
                    thumbValue = newValue.coerceIn(valueRange.start, valueRange.endInclusive)
                    onValueChange(thumbValue)
                }
            }
            .onSizeChanged { size ->
                trackWidth = size.width.toFloat()
            }
        ) {
            val thumbX = (thumbValue - valueRange.start) /
                    (valueRange.endInclusive - valueRange.start) * trackWidth

            // Calculate thumb center Y
            val thumbCenterY = size.height / 2f

            // Draw track
            drawRoundRect(
                color = if(isDarkMode) CARD_BG_DARK else Color(0xffEAECF0),
                topLeft = Offset(0f, thumbCenterY - 2.dp.toPx()),
                size = size.copy(width = size.width, height = 4.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Draw active range
            drawRoundRect(
                color = Color(0xffF33358),
                topLeft = Offset(0f, thumbCenterY - 2.dp.toPx()),
                size = size.copy(width = thumbX, height = 4.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Draw thumb
            drawCircle(
                color = Color.White,
                radius = 10.dp.toPx(),
                center = Offset(thumbX, thumbCenterY)
            )
            drawCircle(
                color = Color(0xffF33358),
                radius = 10.dp.toPx(),
                center = Offset(thumbX, thumbCenterY),
                style = Stroke(1.5.dp.toPx())  // Adjust border thickness
            )
        }
    }
}


@Composable
fun DoubleThumbSlider(
    isLocked:Boolean,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    lowerValue: Float,
    upperValue: Float,
    onValueChange: (Float, Float) -> Unit,
    isDarkMode: Boolean
) {
    var lowerThumbValue by remember(lowerValue) { mutableStateOf(lowerValue) }
    var upperThumbValue by remember(upperValue) { mutableStateOf(upperValue) }
    var trackWidth by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(Color.Transparent)
                .then(if (!isLocked) Modifier.pointerInput(Unit) {
                    detectHorizontalDragGestures { change, _ ->
                        val newValue =
                            (change.position.x / trackWidth) * (valueRange.endInclusive - valueRange.start) + valueRange.start

                        // Determine which thumb to move based on proximity to the drag point
                        val distanceToLowerThumb = (newValue - lowerThumbValue).absoluteValue
                        val distanceToUpperThumb = (newValue - upperThumbValue).absoluteValue

                        if (distanceToLowerThumb < distanceToUpperThumb) {
                            // Moving the lower thumb
                            lowerThumbValue = newValue.coerceIn(valueRange.start, upperThumbValue)
                        } else {
                            // Moving the upper thumb
                            upperThumbValue = newValue.coerceIn(lowerThumbValue, valueRange.endInclusive)
                        }

                        onValueChange(lowerThumbValue, upperThumbValue)
                    }
                } else Modifier)
                .onSizeChanged { size ->
                    trackWidth = size.width.toFloat()
                }
        ) {
            val lowerThumbX =
                (lowerThumbValue - valueRange.start) / (valueRange.endInclusive - valueRange.start) * trackWidth
            val upperThumbX =
                (upperThumbValue - valueRange.start) / (valueRange.endInclusive - valueRange.start) * trackWidth

            val thumbCenterY = size.height / 2f

            // Draw track
            drawRoundRect(
                color = if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0),
                topLeft = Offset(0f, thumbCenterY - 2.dp.toPx()),
                size = size.copy(width = size.width, height = 4.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Draw active range
            drawRoundRect(
                color = Color(0xffF33358),
//                color = if (!isLocked) Color(0xffF33358) else { if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0) },
                topLeft = Offset(lowerThumbX, thumbCenterY - 2.dp.toPx()),
                size = size.copy(width = upperThumbX - lowerThumbX, height = 4.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Draw lower thumb
            drawCircle(
                color = Color.White,
                radius = 10.dp.toPx(),
                center = Offset(lowerThumbX, thumbCenterY)
            )
            drawCircle(
                color = Color(0xffF33358) ,
//                color = if (!isLocked) Color(0xffF33358) else { if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0) },
                radius = 10.dp.toPx(),
                center = Offset(lowerThumbX, thumbCenterY),
                style = Stroke(1.5.dp.toPx())
            )
            // Draw upper thumb
            drawCircle(
                color = Color.White,
                radius = 10.dp.toPx(),
                center = Offset(upperThumbX, thumbCenterY)
            )
            drawCircle(
                color = Color(0xffF33358),
//                color = if (!isLocked) Color(0xffF33358) else { if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0) },
                radius = 10.dp.toPx(),
                center = Offset(upperThumbX, thumbCenterY),
                style = Stroke(1.5.dp.toPx())
            )
        }
    }
}

