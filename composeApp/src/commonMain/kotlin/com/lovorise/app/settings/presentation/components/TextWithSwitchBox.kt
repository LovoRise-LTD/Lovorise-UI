package com.lovorise.app.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.DISABLED_LIGHT

@Composable
fun TextWithSwitchBox(
    text:String,
    isChecked:Boolean,
    onCheckChanged:()->Unit,
    isDarkMode:Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontFamily = PoppinsFontFamily()
        )

        Box(Modifier.requiredHeight(20.dp).requiredWidth(36.dp)) {
//            Switch(
//                modifier = Modifier.requiredHeight(20.dp).requiredWidth(36.dp).noRippleClickable(onCheckChanged),
//                checked = isChecked,
//                onCheckedChange = null,
//                thumbContent = {
//                    Box(Modifier.size(16.dp))
//                },
//                colors = SwitchDefaults.colors(
//                    checkedBorderColor = Color(0xffF33358),
//                    checkedThumbColor = Color.White,
//                    uncheckedThumbColor = Color.White,
//                    uncheckedBorderColor =  Color(0xffF2F4F7),
//                    uncheckedTrackColor =  Color(0xffF2F4F7),
//                    checkedTrackColor =  Color(0xffF33358)
//                )
//            )

            CustomSwitch(
                modifier = Modifier.fillMaxSize(),
                isChecked = isChecked,
                onCheckChanged = {
                    onCheckChanged()
                },
                isDarkMode = isDarkMode
            )
        }


    }
}


@Composable
fun CustomSwitch(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckChanged: () -> Unit,
    isDarkMode: Boolean = false
) {
    Box(
        modifier = modifier
            .noRippleClickable {
                onCheckChanged()
            },
        contentAlignment = Alignment.Center
    ) {
        // Track (background)
        Box(
            Modifier
                .height(20.dp)
                .width(36.dp)
                .background(
                    color = if (isChecked) Color(0xffF33358) else { if (isDarkMode) Color(0xff737272) else Color(0xffF2F4F7)},
                    shape = RoundedCornerShape(50)
                )
        )

        // Thumb (moving circle)
        Box(
            Modifier
                .size(16.dp)
                .offset(x = if (isChecked) 8.dp else (-8).dp)
                .background(Color.White, shape = CircleShape)
        )
    }
}