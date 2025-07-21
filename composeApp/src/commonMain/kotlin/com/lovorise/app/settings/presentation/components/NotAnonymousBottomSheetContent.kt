package com.lovorise.app.settings.presentation.components

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
import coinui.composeapp.generated.resources.continue_enjoying_anonymous_mode_and_more_features
import coinui.composeapp.generated.resources.get_premium
import coinui.composeapp.generated.resources.ic_not_anonymous
import coinui.composeapp.generated.resources.you_are_no_longer_anonymous
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun NotAnonymousBottomSheetContent(isDarkMode:Boolean,onCancel:()->Unit) {
    Column {
        Column(
            modifier = Modifier
                .background(Color.White)
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
                imageVector = vectorResource(Res.drawable.ic_not_anonymous),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
                    .align(Alignment.CenterHorizontally)
            )


            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.you_are_no_longer_anonymous),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff101828)
            )


            Spacer(Modifier.height(10.dp))

            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp).align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.continue_enjoying_anonymous_mode_and_more_features),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff344054)
            )

            Spacer(Modifier.height(16.dp))

            ButtonWithText(
                modifier = Modifier.fillMaxWidth(0.6f),
                text = stringResource(Res.string.get_premium),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = onCancel

            )
            Spacer(Modifier.height(16.dp))

        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )
    }
}