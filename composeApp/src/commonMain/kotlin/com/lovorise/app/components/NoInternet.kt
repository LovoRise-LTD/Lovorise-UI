package com.lovorise.app.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.connect_to_internet
import coinui.composeapp.generated.resources.tap_to_retry
import coinui.composeapp.generated.resources.you_are_offline_check_connection
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoInternet(isDarkMode:Boolean,onRetry:()->Unit){

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.connect_to_internet),
            fontFamily = PoppinsFontFamily(),
            color = if (isDarkMode) Color.White else Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = stringResource(Res.string.you_are_offline_check_connection),
            fontFamily = PoppinsFontFamily(),
            color = if (isDarkMode) Color(0xffEBEBEB).copy(alpha = 0.94f) else Color(0xff344054),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .height(32.dp)
                .background(PRIMARY, shape = RoundedCornerShape(12.dp))
                .noRippleClickable(onRetry),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(Res.string.tap_to_retry),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }




}