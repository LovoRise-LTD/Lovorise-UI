package com.lovorise.app.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily

@Composable
fun TextWithBackground(text:String,verticalPadding:Dp=8.dp,bgColor: Color = Color(0xffF3F5F9),textColor: Color? = null) {

    Box(
        modifier = Modifier.fillMaxWidth().background(bgColor)
    ){
        Text(
            modifier = Modifier.padding(vertical = verticalPadding, horizontal = 16.dp),
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            fontFamily = PoppinsFontFamily(),
            letterSpacing = 0.2.sp,
            color = textColor ?: Color(0xff475467)
        )
    }

}