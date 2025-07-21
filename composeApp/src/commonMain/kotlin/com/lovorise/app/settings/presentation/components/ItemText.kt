package com.lovorise.app.settings.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ShimmerAnimation

@Composable
fun ItemText(text:String,color: Color = Color(0xffF33358)) {
    Text(
        text = text,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        fontFamily = PoppinsFontFamily(),
        color = color
    )
}

@Composable
fun LocationLoader(modifier: Modifier){
    ShimmerAnimation(modifier.height(24.dp).width(200.dp))
}