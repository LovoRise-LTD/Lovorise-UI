package com.lovorise.app.settings.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily

@Composable
fun TitleText(
    text:String,
    isDarkMode:Boolean = false
) {

    Text(
        text = text,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = PoppinsFontFamily(),
        letterSpacing = 0.2.sp,
        color = if (isDarkMode) Color.White else  Color(0xff101828)
    )
}