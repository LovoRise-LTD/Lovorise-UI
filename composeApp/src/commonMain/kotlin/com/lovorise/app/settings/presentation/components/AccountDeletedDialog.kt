package com.lovorise.app.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.ok
import coinui.composeapp.generated.resources.permanently_deleted_from_lovorise
import coinui.composeapp.generated.resources.your_account
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AccountDeletedDialog(onCancel:()->Unit,name:String,isDarkMode:Boolean) {

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
                .padding(horizontal = 17.dp)
                .background(if(isDarkMode) CARD_BG_DARK else Color.White, shape = RoundedCornerShape(15.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                // .padding(top = 29.dp, bottom = 22.dp),
                verticalArrangement = Arrangement.Top,
                //  horizontalAlignment = Alignment.CenterStart
            ) {

                Spacer(Modifier.height(16.dp))
                Box(Modifier.size(24.dp).align(Alignment.End).noRippleClickable(onCancel), contentAlignment = Alignment.Center) {
                    Icon(
                        tint = if (isDarkMode) Color.White else Color.Black,
                        imageVector = vectorResource(Res.drawable.ic_xmark),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Account deleted",
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(2.dp))


                Text(
                    text = buildAnnotatedString {
                        append(stringResource(Res.string.your_account))
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)){
                            append(" $name ")
                        }
                        append(stringResource(Res.string.permanently_deleted_from_lovorise))
                    },
                    color = if(isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 21.sp,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    fontWeight = FontWeight.Normal
                )


                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(Modifier.weight(1f))

                    Text(
                        modifier = Modifier.noRippleClickable(onCancel),
                        text = stringResource(Res.string.ok),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xffF33358)
                    )

                }

                Spacer(Modifier.height(16.dp))




            }







        }



    }

}