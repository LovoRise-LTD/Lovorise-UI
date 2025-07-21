package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddMorePhotos(photosCount:Int,onAddPhotos:()->Unit) {

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 35.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${photosCount}/3 ${stringResource(Res.string.photos)}",
            fontSize = 18.sp,
            color = Color.White,
            lineHeight = 27.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "${stringResource(Res.string.add)} ${3-photosCount} ${stringResource(Res.string.more_photos_in_your_profile_to_unlock)}",
            fontSize = 16.sp,
            color = Color.White,
            lineHeight = 24.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Box(Modifier.height(40.dp).width(235.dp).background(Color(0xffF33358),
            RoundedCornerShape(40)
        ).noRippleClickable(onAddPhotos), contentAlignment = Alignment.Center){

            Text(
                text = stringResource(Res.string.add_photos),
                fontSize = 16.sp,
                color = Color.White,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium
            )

        }

    }


}