package com.lovorise.app.accounts.presentation.signup.location

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.enable_location
import coinui.composeapp.generated.resources.ic_enable_location
import coinui.composeapp.generated.resources.let_us_know_your_location
import coinui.composeapp.generated.resources.location_permission_required
import coinui.composeapp.generated.resources.lovorise_needs_access_to_your_location
import coinui.composeapp.generated.resources.open_settings
import coinui.composeapp.generated.resources.set_your_location_to_see_who_is_nearby
import coinui.composeapp.generated.resources.turn_on
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LocationPermissionDialog(openSettings:()->Unit) {

    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)).noRippleClickable {  }, contentAlignment = Alignment.Center) {
        Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp).clip(RoundedCornerShape(16.dp))) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier.size(68.44.dp)
                        .background(Color(0xffFF7791).copy(alpha = 0.15f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_enable_location),
                        contentDescription = stringResource(Res.string.enable_location),
                        modifier = Modifier.size(39.82.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))


                Text(
                    text = stringResource(Res.string.let_us_know_your_location),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xff101828)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.set_your_location_to_see_who_is_nearby),
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
                    modifier = Modifier.fillMaxWidth(0.65f),
                    text = stringResource(Res.string.open_settings),
                    bgColor = Color(0xffF33358),
                    textColor = Color(0xffffffff),
                    onClick = {
                        openSettings()
                    }
                )
            }
        }
    }

}


@Composable
fun EnableGpsDialog(onEnable:()->Unit,isDarkMode:Boolean) {

    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)).noRippleClickable {  }, contentAlignment = Alignment.Center) {
        Box(Modifier.fillMaxWidth(0.8f).clip(RoundedCornerShape(16.dp))) {
            Column(
                modifier = Modifier
                    .background(if (isDarkMode) CARD_BG_DARK else Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier.size(48.dp)
                        .background(Color(0xffFF7791).copy(alpha = 0.15f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_enable_location),
                        contentDescription = stringResource(Res.string.enable_location),
                        modifier = Modifier.size(27.93.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))


                Text(
                    text = stringResource(Res.string.location_permission_required),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.lovorise_needs_access_to_your_location),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(36.dp)
                        .background(Color(0xffF33358), shape = RoundedCornerShape(16.dp))
                        .noRippleClickable(onEnable),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.turn_on),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                      //  lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xffffffff)
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }

}