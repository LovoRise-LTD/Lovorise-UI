package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.try_now
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.SubscriptionType
import org.jetbrains.compose.resources.stringResource


@Composable
fun TryNowButton(type: SubscriptionType,onClick:()->Unit) {

    Box(modifier = Modifier.width(63.dp).height(22.dp).noRippleClickable(onClick).background(brush = Brush.verticalGradient(listOf(Color(0xffF3335D),Color(0xffF33386))), shape = RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center){
        Text(
            text = stringResource(Res.string.try_now),
            color = Color.White,
            fontFamily = PoppinsFontFamily(),
            letterSpacing = 0.2.sp,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }


}