package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ok
import coinui.composeapp.generated.resources.you_are_in_spotlight
import coinui.composeapp.generated.resources.you_are_in_spotlight_message
import coinui.composeapp.generated.resources.you_are_in_super_spotlight
import coinui.composeapp.generated.resources.you_are_in_super_spotlight_message
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource


enum class SpotlightType{
    SPOTLIGHT,SUPER_SPOTLIGHT
}


@Composable
fun PurchasedSpotlightBottomSheetContent(isDarkMode:Boolean,onCancel:()->Unit,spotlightType:SpotlightType,getRemainingTime:()->String?) {

    var remainingTime by rememberSaveable{ mutableStateOf<String?>("") }

    LaunchedEffect(true){
        while (remainingTime != null){
            remainingTime = getRemainingTime()
            delay(1000L)
        }

    }


    Column {
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
           // verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = stringResource(if (spotlightType == SpotlightType.SPOTLIGHT) Res.string.you_are_in_spotlight else Res.string.you_are_in_super_spotlight),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                color =if (isDarkMode) Color.White else  Color(0xff101828)
            )

            Spacer(Modifier.height(16.dp))


            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = remainingTime ?: "",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 23.sp,
                textAlign = TextAlign.Center,
                color =if (isDarkMode) DISABLED_LIGHT else  Color(0xff344054)
            )

            Spacer(Modifier.height(8.dp))


            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp).align(Alignment.CenterHorizontally),
                text = stringResource(if (spotlightType == SpotlightType.SPOTLIGHT) Res.string.you_are_in_spotlight_message else Res.string.you_are_in_super_spotlight_message),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(16.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(41.dp)
                    .then(
                        if (spotlightType == SpotlightType.SPOTLIGHT) Modifier.background(Color(0xffEAAA08), shape = RoundedCornerShape(16.dp))
                        else Modifier.background(brush = Brush.linearGradient(listOf(Color(0xffAE02FF), Color(0xffFA00FF))), shape = RoundedCornerShape(16.dp))
                    )
                    .noRippleClickable(onCancel),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.ok),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }
}