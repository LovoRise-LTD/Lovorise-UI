package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable

@Composable
fun RoundedTextButton(modifier: Modifier=Modifier,text:String,onClick:() -> Unit, horizontalPadding:Dp = 16.dp,isDarkMode:Boolean) {
        Box(
            modifier = modifier.height(32.dp).wrapContentWidth().background(Color(if (isDarkMode) 0xff24282B else 0XffEAECF0),RoundedCornerShape(12.dp)).noRippleClickable{
                onClick()
            },
            contentAlignment = Alignment.Center
        ) {


        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color(if (isDarkMode) 0xffFFFFFF else 0xff101828),

          //  lineHeight = 16.sp,
            letterSpacing = 0.2.sp
        )



    }

}