package com.lovorise.app.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_xmark
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.vectorResource

@Composable
fun CustomDialogWithTextAndBodyAndActions(onCancel:()->Unit,onAction1:()->Unit,onAction2:()->Unit,actionText1:String,actionText2:String,body:AnnotatedString,title:String,isDarkMode:Boolean = false) {

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
                .background(if (isDarkMode) CARD_BG_DARK else Color.White, shape = RoundedCornerShape(15.dp))
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
                        imageVector = vectorResource(Res.drawable.ic_xmark),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff667085)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = title,
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
                    text = body,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 21.sp,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp
                )





                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)

                ) {


                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = Color((0xffD0D5DD)),
                                shape = RoundedCornerShape(40)
                            )
                            .noRippleClickable(onAction1),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = actionText1,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff101828)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                            .background(
                                color = Color((0xffF33358)),
                                shape = RoundedCornerShape(40)
                            )
                            .noRippleClickable(onAction2),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = actionText2,
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

                Spacer(Modifier.height(16.dp))




            }







        }



    }
}