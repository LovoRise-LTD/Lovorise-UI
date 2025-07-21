package com.lovorise.app.chat.presentation.components

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
import coinui.composeapp.generated.resources.ok
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoInternetDialog(
    title: String,
    message: String,
    onPositiveClick: () -> Unit = {},
    isDarkMode:Boolean
) {


    Dialog(
        onDismissRequest = { onPositiveClick() },
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
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(6.dp))


                Text(
                    text = message,
                    textAlign = TextAlign.Start,
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp,
                    fontWeight = FontWeight.Normal,
                    color = if(isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )

                Spacer(Modifier.height(8.dp))


                Row(
                    modifier = Modifier.fillMaxWidth().height(27.dp),
                    verticalAlignment = Alignment.CenterVertically,
                  //  horizontalArrangement = Arrangement.spacedBy(32.dp)

                ) {

                    Spacer(Modifier.weight(1f))



                    Text(
                        modifier = Modifier.noRippleClickable(onPositiveClick),
                        text = stringResource(Res.string.ok),
                        color = Color(0xffF33358),
                        textAlign = TextAlign.Center,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        lineHeight = 21.sp,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp
                    )





                }


            }


        }


    }


}