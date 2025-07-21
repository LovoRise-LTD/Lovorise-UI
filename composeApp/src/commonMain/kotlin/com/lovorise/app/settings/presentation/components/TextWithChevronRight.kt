package com.lovorise.app.settings.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_chevron_right_light_color
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TextWithChevronRight(text:String,onClick:()->Unit,isDarkMode:Boolean = false) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .noRippleClickable(onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontFamily = PoppinsFontFamily()
        )

        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center){
            if (isDarkMode){
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                    contentDescription = null,
                    tint = Color.White
                )
            }else {
                Image(
                    modifier = Modifier.size(24.dp),
                    imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                    contentDescription = null
                )
            }
        }

    }
}