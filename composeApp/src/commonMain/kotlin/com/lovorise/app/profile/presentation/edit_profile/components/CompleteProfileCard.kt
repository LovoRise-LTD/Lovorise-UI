package com.lovorise.app.profile.presentation.edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import org.jetbrains.compose.resources.stringResource


@Composable
fun CompleteProfileCard(
    progress:Int
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(53.dp)
            .background(brush = Brush.linearGradient(listOf(Color(0xffF3335D), Color(0xffF33386)))),
        contentAlignment = Alignment.Center
    ){
        Row(modifier = Modifier.height(21.dp).fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Text(
                text = stringResource(Res.string.complete_your_profile),
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "$progress%",
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold
            )

        }
    }

}