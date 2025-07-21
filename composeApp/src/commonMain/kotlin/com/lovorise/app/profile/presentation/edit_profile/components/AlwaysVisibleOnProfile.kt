package com.lovorise.app.profile.presentation.edit_profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.always_visible_on_profile
import coinui.composeapp.generated.resources.ic_eye_visible
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AlwaysVisibleOnProfile(
    text:String = stringResource(Res.string.always_visible_on_profile),
    res:DrawableResource = Res.drawable.ic_eye_visible,
    isDarkMode:Boolean = false
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.37.dp)
    ){


        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            Image(
                imageVector = vectorResource(res),
                contentDescription = null,
                modifier = Modifier.width(22.12.dp).height(14.62.dp)
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 21.sp,
            //  textAlign = TextAlign.Center,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )







    }
}