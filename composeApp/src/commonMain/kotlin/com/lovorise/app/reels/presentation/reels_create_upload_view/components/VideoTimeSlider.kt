package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily

@Composable
fun VideoTimeSlider(start:Int,end:Int,currentPos:Float,onSliderValueChange:(Float)->Unit) {

    @Composable
    fun CustomSlider(
        modifier: Modifier = Modifier,
        valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
        value:Float,
        onValueChange: (Float) -> Unit,
    ) {
        var trackWidth by remember { mutableFloatStateOf(0f) }

        var thumbValue by remember(value) { mutableStateOf(value) }

        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
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
                    color = Color(0xffF2F4F7),
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
                    color = Color.Red,
                    radius = 4.dp.toPx(),
                    center = Offset(thumbX, thumbCenterY)
                )
                drawCircle(
                    color = Color(0xffF33358),
                    radius = 4.dp.toPx(),
                    center = Offset(thumbX, thumbCenterY),
                    style = Stroke(1.5.dp.toPx())  // Adjust border thickness
                )
            }
        }
    }


    Column{
        CustomSlider (
            value = currentPos,
            onValueChange = onSliderValueChange
        )
        Spacer(Modifier.height(3.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Text(
                color = Color(0xffF2F4F7),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                text = "00:${if(start < 10) "0" else ""}$start"
            )

            Text(
                text = "00:${if(end < 10) "0" else ""}$end",
                color = Color(0xffF2F4F7),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
            )

        }
        Spacer(Modifier.height(3.dp))
    }

}