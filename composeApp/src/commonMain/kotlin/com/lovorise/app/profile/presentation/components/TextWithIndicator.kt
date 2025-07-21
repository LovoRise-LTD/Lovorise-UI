package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
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
fun TextWithIndicator(text:String,onClick:() -> Unit,indicatorColor:Color,textColor:Color,modifier: Modifier) {

    Column(
        modifier = modifier
            .height(36.dp)
            .fillMaxWidth(0.5f)
            .noRippleClickable {
                onClick()
            },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            text = text,
            color = textColor,
           // modifier = Modifier.padding(vertical = 5.dp, horizontal = 9.dp),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.2.sp
        )

        Divider(color = indicatorColor, modifier = Modifier.height(1.7.dp))

    }

}