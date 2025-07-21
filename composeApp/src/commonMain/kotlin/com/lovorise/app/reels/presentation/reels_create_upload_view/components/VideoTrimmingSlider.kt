package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lovorise.app.libs.camera.IosImageView
import io.ktor.util.reflect.instanceOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTrimmingSlider(
    height: Dp = 32.dp,
    frameList: List<Any>, // List of video frames as ImageBitmaps
//    onTrimChanged: (start: Float, end: Float) -> Unit
    // totalDuration:Long
    progress: Float,
    currentRange:ClosedFloatingPointRange<Float> = 0f..100f,
    onVideoClipRange: (ClosedFloatingPointRange<Float>) ->Unit
) {

    var sliderPosition by remember { mutableStateOf(currentRange) }

    LaunchedEffect(true){
        onVideoClipRange(sliderPosition)
    }

    BoxWithConstraints(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(2.dp))) {

        val containerWidth = maxWidth
        // Display video frames in a horizontal LazyRow


        RangeSlider(
            //    modifier = Modifier.fillMaxWidth().height(100.dp),
            value = sliderPosition,
            // steps = 5,
            onValueChange = { range ->
                //     println("the diff value is ${range.endInclusive - range.start}")
//                if((range.endInclusive - range.start )*  > 0.2){
                sliderPosition = range
//                }

            },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                onVideoClipRange(sliderPosition)
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
            startThumb = {
                Box(
                    modifier = Modifier
                        .size(11.dp, height)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Box(
                        Modifier
                            .width(2.dp)
                            .height(12.dp)
                            .background(
                                Color(0xff344054),
                                RoundedCornerShape(2.dp)
                            ))
                }
            },
            endThumb = {
                Box(
                    modifier = Modifier
                        .size(11.dp, height)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Box(
                        Modifier
                            .width(2.dp)
                            .height(12.dp)
                            .background(
                                Color(0xff344054),
                                RoundedCornerShape(2.dp)
                            ))
                }
            },
            colors = SliderDefaults.colors(
                disabledActiveTickColor = Color.Transparent,
                disabledInactiveTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                activeTrackColor = Color.Green,
                inactiveTrackColor = Color.DarkGray

            ),
            track = {
                Box(
                    modifier = Modifier
                        // .align(Alignment.CenterEnd)
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(Color.Transparent, shape = RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ){
                    val itemWidth = containerWidth/9
                    Row(Modifier.fillMaxWidth().height(height).clip(RoundedCornerShape(4.dp)), horizontalArrangement = Arrangement.spacedBy(0.dp), verticalAlignment = Alignment.CenterVertically) {
                        frameList.forEach {
                            if (it.instanceOf(ImageBitmap::class)) {
                                Image(
                                    contentScale = ContentScale.FillBounds,
                                    bitmap = it as ImageBitmap,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxHeight().requiredWidth(itemWidth)
                                )
                            }else{

                                IosImageView(
                                    imgData = it,
                                    modifier = Modifier.fillMaxHeight().requiredWidth(itemWidth)
                                )
                            }
                        }
                    }
                    PlayerProgressIndicator(progress)
                }
            }
        )
    }
}