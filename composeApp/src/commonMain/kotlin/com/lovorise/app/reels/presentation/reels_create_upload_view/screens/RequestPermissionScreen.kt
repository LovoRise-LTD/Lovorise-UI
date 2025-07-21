package com.lovorise.app.reels.presentation.reels_create_upload_view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RequestPermissionScreen(onBackIconClick:()->Unit,requestPermissions:()->Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        Spacer(Modifier.height(20.dp))
        Box(Modifier.height(33.29.dp).width(34.67.dp).noRippleClickable(onBackIconClick), contentAlignment = Alignment.Center) {
            Image(
                imageVector = vectorResource(Res.drawable.ic_back_icon_white),
                contentDescription = null,
                modifier = Modifier.width(18.dp).height(16.dp)
            )
        }

        Box(Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center){
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(horizontal = 35.dp),
                    text = stringResource(Res.string.to_take_photos_and_videos_we_need),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 45.dp),
                    text = stringResource(Res.string.this_will_allow_you_to_take_photos_videos),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp,
                    color = Color(0xff98A2B3)
                )
                Spacer(Modifier.height(16.dp))

                ButtonWithText(
                    modifier = Modifier.width(103.dp),
                    text = stringResource(Res.string.turn_on),
                    textColor = Color.White,
                    bgColor = Color(0xffF33358),
                    onClick = requestPermissions,
                    shape = RoundedCornerShape(8.dp)
                )

            }
        }

    }
}