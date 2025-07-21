package com.lovorise.app.accounts.presentation.signup.profile_upload.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlertMessageDialog(
    title: String,
    message: String,
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
    isDarkMode:Boolean
) {


    Dialog(
        onDismissRequest = { onNegativeClick() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(if(isDarkMode) CARD_BG_DARK else Color.White, shape = RoundedCornerShape(24.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 44.dp, bottom = 14.dp),
                verticalArrangement = Arrangement.Top,
                //   horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = title,
                    color = if(isDarkMode) Color.White else Color(0xff101828),
                    textAlign = TextAlign.Start,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(8.dp))


                Text(
                    text = message,
                    textAlign = TextAlign.Start,
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 18.sp,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                    fontWeight = FontWeight.Normal,
                    color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Spacer(Modifier.height(22.dp))


                Row(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.dp)

                ) {

                    Spacer(Modifier.weight(1f))

                    Text(
                        modifier = Modifier.noRippleClickable(onNegativeClick),
                        text = stringResource(Res.string.cancel),
                        color = Color(0xffF33358),
                        textAlign = TextAlign.Center,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.2.sp
                    )


                    Text(
                        modifier = Modifier.noRippleClickable(onPositiveClick),
                        text = stringResource(Res.string.settings),
                        color = Color(0xffF33358),
                        textAlign = TextAlign.Center,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.2.sp
                    )





                }


            }


        }


    }


}