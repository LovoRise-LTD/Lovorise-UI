package com.lovorise.app.reels.presentation.prompt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable


@Composable
fun PromptsListItem(text:String,onClick:()->Unit) {

    Column(Modifier.noRippleClickable(onClick)) {
        Spacer(Modifier.height(10.dp))
        Text(
            text = text,
            color = Color(0xff344054),
            letterSpacing = 0.2.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = PoppinsFontFamily(),
            fontSize = 12.sp,
            lineHeight = 24.sp
        )
        Spacer(Modifier.height(10.dp))
        Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xffEAECF0)))
    }
}
