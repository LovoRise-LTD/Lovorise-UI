package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun TextTabRowItem(
    text:String,
    showIndicator:Boolean,
    onClick:()->Unit
) {

    Column(
        modifier = Modifier
            .height(50.dp)
            .noRippleClickable(onClick)
    ) {

        Text(
            text = text,
            modifier = Modifier.padding(10.dp),
            lineHeight = 18.sp,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = PoppinsFontFamily(),
            color = Color.White
        )

        if (showIndicator){
            Box(Modifier.width(29.25.dp).height(3.dp).background(Color.White,RoundedCornerShape(12.dp)).align(Alignment.CenterHorizontally))
        }



    }

}