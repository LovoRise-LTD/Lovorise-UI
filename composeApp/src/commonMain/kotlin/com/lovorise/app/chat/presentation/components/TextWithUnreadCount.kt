package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily

@Composable
fun TextWithUnreadCount(text:String,unreadCount:Int,isDarkMode:Boolean) {


    Row(modifier = Modifier.height(21.dp),verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            letterSpacing = 0.2.sp,
            color = if (isDarkMode) Color.White else Color(0xff101828)
        )

        Spacer(Modifier.width(8.dp))

        if (unreadCount != 0) {
            Box(
                Modifier.height(16.dp).width(17.dp).background(Color(0xffF33358), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = unreadCount.toString(),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                   // lineHeight = 13.sp,
                    letterSpacing = 0.2.sp,
                    color = Color.White
                )
            }
        }
    }


}