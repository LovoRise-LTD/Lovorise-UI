package com.lovorise.app.lovorise_hearts.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.by_tapping_ok_you_agree_to_the
import coinui.composeapp.generated.resources.ic_heart_medium
import coinui.composeapp.generated.resources.maybe_later
import coinui.composeapp.generated.resources.ok
import coinui.composeapp.generated.resources.terms_and_conditions
import coinui.composeapp.generated.resources.try_power_ups_using_coins
import coinui.composeapp.generated.resources.welcome_to_lovorise_hearts_shop
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LovoriseHeartScreenWelcomeDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onMaybeLater: () -> Unit,
    onTermsAndCondition:()->Unit,
    isDarkMode:Boolean
) {

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.784f)
                .background(if(isDarkMode) BASE_DARK else Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp, bottom = 17.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon at the top
                Image(
                    modifier = Modifier.size(65.dp),
                    imageVector = vectorResource(Res.drawable.ic_heart_medium),
                    contentDescription = "hearts"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title text
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 57.5.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(Res.string.welcome_to_lovorise_hearts_shop),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = if(isDarkMode) Color.White else Color(0xff101828),
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Description text
                Box(Modifier.height(37.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(Res.string.try_power_ups_using_coins),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                        lineHeight = 16.52.sp,
                        letterSpacing = 0.14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Terms and Conditions text

                Text(
                    text = stringResource(Res.string.by_tapping_ok_you_agree_to_the),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    lineHeight = 16.52.sp,
                    letterSpacing = 0.14.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier.noRippleClickable(onTermsAndCondition),
                    text = stringResource(Res.string.terms_and_conditions),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = Color(0xffF33358),
                    lineHeight = 16.52.sp,
                    letterSpacing = 0.14.sp,
                    textAlign = TextAlign.Center
                )


                Spacer(modifier = Modifier.height(18.dp))

                // OK Button
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .width(177.dp)
                        .background(
                            color = Color(0xffF33358),
                            shape = RoundedCornerShape(50)
                        )
                        .noRippleClickable {
                            onConfirm()
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(Res.string.ok),
                        textAlign = TextAlign.Center,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.White,
                        lineHeight = 20.sp,
                        letterSpacing = 0.2.sp
                    )


                }


                Spacer(modifier = Modifier.height(10.dp))

                // Maybe Later Text

                Text(
                    modifier = Modifier.noRippleClickable { onMaybeLater() },
                    text = stringResource(Res.string.maybe_later),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xff98A2B3),
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )
            }
        }
    }


}