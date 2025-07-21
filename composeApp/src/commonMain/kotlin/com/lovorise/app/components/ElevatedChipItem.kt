package com.lovorise.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT


@Composable
fun ElevatedChipItem(
    text:String,
    isSelected:Boolean,
    onClick:()->Unit,
    isDarkMode:Boolean = false
) {
    Card(
        modifier = Modifier
            .height(36.dp)
            .noRippleClickable(onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color(if (isSelected) 0xffF33358 else if (isDarkMode) 0xff141313 else 0xffffffff)),
        border = if (!isSelected) BorderStroke(1.dp,if (isDarkMode) CARD_BG_DARK else DISABLED_LIGHT) else null,
    ){
        Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = text,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                color = if (!isSelected) Color(0xff667085) else Color.White
            )
        }
    }
}