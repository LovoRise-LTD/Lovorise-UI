package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import coinui.composeapp.generated.resources.ic_filter
import coinui.composeapp.generated.resources.ic_filter_white
import coinui.composeapp.generated.resources.lovorise
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun TopBarWithFilter(onFilterClick:()->Unit,isDarkMode:Boolean) {

    Box(Modifier.height(35.dp)) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(horizontal = 21.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = stringResource(Res.string.lovorise),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
               // lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                color = Color(0xffF33358)
            )
            Box(Modifier.size(24.dp).noRippleClickable(onFilterClick), contentAlignment = Alignment.Center){
                Image(
                    modifier = Modifier.width(24.dp).height(14.93.dp),
                    imageVector = vectorResource(if(isDarkMode) Res.drawable.ic_filter_white else Res.drawable.ic_filter),
                    contentDescription = "back"
                )
            }
        }


    }

}