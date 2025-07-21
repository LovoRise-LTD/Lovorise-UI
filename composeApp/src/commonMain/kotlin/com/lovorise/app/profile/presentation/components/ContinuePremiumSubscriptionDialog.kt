package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.isAndroid
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource

@Composable
fun ContinuePremiumSubscriptionDialog(
    onPositive:()->Unit,
    onCancel:()->Unit,
    isDarkMode:Boolean
) {

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .background(if (isDarkMode) BASE_DARK else Color.White, shape = RoundedCornerShape(16.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = buildAnnotatedString {
                        append(stringResource(Res.string.by_continuing_to_lovorise_premium))
                        withStyle(SpanStyle(color = Color(0xffF33358))){
                            append(" ${stringResource(Res.string.subscription_terms)} ")
                        }
                        append(if (isAndroid()) stringResource(Res.string.and_auto_renew_unless_google_play) else stringResource(Res.string.and_auto_renew_unless_app_store_settings))
                    },
                    fontSize = 14.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )

                Spacer(Modifier.height(16.dp))

                ButtonWithText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.continue_txt),
                    onClick = onPositive,
                    textColor = Color.White,
                    bgColor = Color(0xffF33358),
                    height = 41.dp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    modifier = Modifier.noRippleClickable(onCancel),
                    text = stringResource(Res.string.maybe_later),
                    fontSize = 14.sp,
                    color = Color(0xff98A2B3),
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                )



            }




        }

    }

}