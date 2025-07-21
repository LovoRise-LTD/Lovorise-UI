package com.lovorise.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily

@Composable
fun Toast(text:String) {

    Box(
        modifier = Modifier.fillMaxWidth().height(36.dp).background(
            Color(0xff414A62).copy(alpha = 0.85f),
            shape = RoundedCornerShape(4.dp)
        ),
        contentAlignment = Alignment.Center
    ){

        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            lineHeight = 21.sp,
            fontFamily = PoppinsFontFamily(),
            letterSpacing = 0.1.sp,
            textAlign = TextAlign.Center
        )
    }

}