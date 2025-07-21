package com.lovorise.app.accounts.presentation.signup.age.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import org.jetbrains.compose.resources.stringResource

@Composable
fun AgeConfirmationDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    formattedDOB:String,
    age:Int,
    isDarkMode:Boolean,
) {

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp)
                .background(if (isDarkMode) CARD_BG_DARK else Color.White, shape = RoundedCornerShape(24.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .padding(top = 29.dp, bottom = 22.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "${stringResource(Res.string.you_are)} $age",
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                    fontSize = 18.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(10.dp))


                Text(
                    text = buildAnnotatedString {

                        withStyle(SpanStyle(color = if (isDarkMode) Color.White else  Color(0xff344054).copy(alpha = 0.65f), fontWeight = FontWeight.Medium)){
                            append("${stringResource(Res.string.you_entered)} : ")
                        }

                        withStyle(SpanStyle(color = if (isDarkMode) Color.White else  Color(0xff101828), fontWeight = FontWeight.Medium)){
                            append(formattedDOB)
                        }

                    },
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 21.sp,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = stringResource(Res.string.confirm_your_age_is_correct),
                    color = if (isDarkMode) Color.White else  Color(0xff344054),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
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
                            text = stringResource(Res.string.edit),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = if (isDarkMode) Color.White else Color(0xff101828)
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
                            .noRippleClickable(onConfirm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.confirm),
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


            }







        }



    }

}