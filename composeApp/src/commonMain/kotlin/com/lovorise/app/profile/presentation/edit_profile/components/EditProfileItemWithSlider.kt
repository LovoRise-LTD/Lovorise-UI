package com.lovorise.app.profile.presentation.edit_profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun EditProfileItemWithSlider(
//    modifier: Modifier = Modifier,
    title: String,
    titleColor:Color = Color(0xff344054),
    sliderValue: Float = 0f,
    description: String,
    isSingleThumbSlider: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    onValueChangeSingleThumb: (Float) -> Unit = {},
    onValueChangeDoubleThumb: (Float, Float) -> Unit = { _, _ -> },
    doubleSliderValue:Pair<Float,Float> = Pair(30f,50f),
    addDividerAtTop:Boolean = true,
    isLocked:Boolean = false,
    icon:DrawableResource? = null,
    isDarkMode:Boolean = false
) {

    val value by remember(sliderValue) { mutableStateOf(sliderValue) }
    val rangeValue by remember(doubleSliderValue){ mutableStateOf(doubleSliderValue) }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (addDividerAtTop) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = if (isDarkMode) Color(0xff737272) else Color(0xffEAECF0)
            )
        }

        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Row(modifier = Modifier.height(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = if (isDarkMode) Color.White else titleColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                )
                if (icon != null){
                    Spacer(Modifier.width(8.dp))
                    Image(
                        imageVector = vectorResource(icon),
                        modifier = Modifier.size(20.dp),
                        contentDescription = null
                    )
                }

            }

            Text(
                modifier = Modifier
                    .align(Alignment.End),
                //.padding(end = 12.dp),
                text = description,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff5E697A),
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontFamily = PoppinsFontFamily()
            )

//            Spacer(Modifier.height(8.dp))

            if (isSingleThumbSlider) {
//
//                Slider(
//                    valueRange = 0f..100f,
//                    value = value,
//                    onValueChangeFinished = {
//                        onValueChangeSingleThumb(sliderValue)
//                    },
//                    onValueChange = {
//                        value = it
//                        onValueChangeSingleThumb(sliderValue)
//                    },
//                    thumb = {
//                        SliderThumb()
//                    },
//                    colors = SliderDefaults.colors(
//                        activeTrackColor = Color(0xffF33358),
//                        inactiveTrackColor = Color(0xffEAECF0)
//                    ),
//                    modifier = Modifier.fillMaxWidth().requiredHeight(4.dp)
//                )

                SingleThumbSlider(
//                    modifier = Modifier
//                        .background(color = Color.Green),
                    value = value,
                    valueRange = valueRange,
                    onValueChange = {
                        onValueChangeSingleThumb(it)
                    },
                    isDarkMode = isDarkMode
                )
            }
            else {

//                RangeSlider(
//                    value = rangeValue,
//                    onValueChangeFinished = {
//                        onValueChangeDoubleThumb(doubleSliderValue.first, doubleSliderValue.second)
//                    },
//                    onValueChange = {
//                        rangeValue = it
//                        onValueChangeDoubleThumb(doubleSliderValue.first, doubleSliderValue.second)
//                    },
//                    startThumb = {
//                        SliderThumb()
//                    },
//                    endThumb = {
//                        SliderThumb()
//                    },
//                    colors = SliderDefaults.colors(
//                        disabledActiveTickColor = Color.Transparent,
////                        disabledInactiveTickColor = Color.Transparent,
//                        inactiveTickColor = Color.Transparent,
//                        activeTrackColor = Color(0xffF33358),
//                        inactiveTrackColor = Color(0xffEAECF0)
//                    ),
//                    valueRange = 0f..100f,
//                    modifier = Modifier.fillMaxWidth().height(4.dp)
//
//                )

                DoubleThumbSlider(
//                    modifier = Modifier
//                        .background(color = Color.Green),
                    valueRange = valueRange,
                    lowerValue = rangeValue.first,
                    upperValue = rangeValue.second,
                    onValueChange = { lower, upper ->
                        onValueChangeDoubleThumb(lower, upper)
                    },
                    isLocked = isLocked,
                    isDarkMode = isDarkMode

                )
            }
        }

        Spacer(Modifier.height(16.dp))
//        HorizontalDivider(
//            modifier = Modifier
//                .fillMaxWidth(),
//            thickness = 1.dp,
//            color = Color(0xffEAECF0)
//        )
    }

}


@Composable
fun SliderTrack() {
    Box(Modifier.fillMaxWidth().height(4.dp))
}


@Composable
fun SliderThumb() {
    Box(Modifier.size(20.dp).background(Color.White, CircleShape).border(1.5.dp,Color(0xffF33358), CircleShape))
}