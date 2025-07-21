package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ChipItem(
    icon: DrawableResource? = null,
    text:String,
    isDarkMode:Boolean
) {

    Row(
        modifier = Modifier
            .height(31.25.dp)
            .border(width = 1.dp,color = Color(0xffEAECF0), shape = RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Spacer(Modifier.width(10.dp))

        if (icon != null) {
            Icon(
                modifier = Modifier
                    .size(12.dp),
                imageVector = vectorResource(icon),
                contentDescription = "",
                tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )
            Spacer(Modifier.width(6.dp))
        }



        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            lineHeight = 21.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal
        )


        Spacer(Modifier.width(10.dp))



    }

}