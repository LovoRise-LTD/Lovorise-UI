package com.lovorise.app.reels.presentation.prompt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily

@Composable
fun AnsweredTextPromptCard(title:String,text:String) {

    Box(Modifier.fillMaxWidth().padding(horizontal = 6.dp).clip(RoundedCornerShape(16.dp)).background(Color.White)){
        Column(Modifier.padding(vertical = 16.dp)) {
            Spacer(Modifier.height(9.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = title,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = Color(0xff344054),
                fontSize = 12.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(19.dp))

            Text(
                modifier = Modifier.padding(horizontal = 32.5.dp),
                text = text,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = Color(0xff101828),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(10.dp))

        }

    }

}