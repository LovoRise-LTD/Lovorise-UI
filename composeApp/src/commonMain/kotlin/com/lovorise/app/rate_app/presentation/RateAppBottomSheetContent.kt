package com.lovorise.app.rate_app.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
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
import coinui.composeapp.generated.resources.ic_rate_app_logo
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RateAppBottomSheetContent(isDarkMode:Boolean,navigateToRateAppScreen:()->Unit,onCancel:()->Unit) {
    Column {
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .background(Color(0xff667085))
                )
            }


            Spacer(Modifier.height(10.dp))

            Image(
                imageVector = vectorResource(Res.drawable.ic_rate_app_logo),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
                    .align(Alignment.CenterHorizontally)
            )


            Spacer(Modifier.height(10.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.do_you_enjoy_using_lovorise),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 36.5.dp).align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.we_appreciate_your_feedback),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(16.dp))

            ButtonWithText(
                modifier = Modifier.fillMaxWidth(0.6f),
                text = stringResource(Res.string.yes),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = navigateToRateAppScreen

            )

            Spacer(Modifier.height(10.dp))



            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).noRippleClickable(onCancel),
                text = stringResource(Res.string.no),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff98A2B3)
            )

            Spacer(Modifier.height(16.dp))

        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }
}