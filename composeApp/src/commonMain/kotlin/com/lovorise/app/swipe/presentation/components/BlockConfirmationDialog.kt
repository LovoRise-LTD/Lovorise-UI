package com.lovorise.app.swipe.presentation.components

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
import coinui.composeapp.generated.resources.block
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.do_you_want_to_block
import coinui.composeapp.generated.resources.ic_xmark
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BlockConfirmationDialog(name:String,onBlock:()->Unit,onCancel:()->Unit,isDarkMode:Boolean) {

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
                    text = stringResource(Res.string.block),
                    color = if (isDarkMode) Color(0xff101828) else Color.White,
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

                        withStyle(SpanStyle(fontWeight = FontWeight.Normal)){
                            append("${stringResource(Res.string.do_you_want_to_block)} ")
                        }

                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)){
                            append("$name?")
                        }

                    },
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
                                shape = RoundedCornerShape(50)
                            )
                            .noRippleClickable(onCancel),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = if (isDarkMode) Color(0xff101828) else Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                            .background(
                                color = Color((0xffF33358)),
                                shape = RoundedCornerShape(50)
                            )
                            .noRippleClickable(onBlock),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.block),
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