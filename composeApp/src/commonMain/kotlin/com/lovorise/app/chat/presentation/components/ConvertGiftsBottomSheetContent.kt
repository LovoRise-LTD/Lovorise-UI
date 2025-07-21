package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.convert
import coinui.composeapp.generated.resources.convert_gift_to_hearts
import coinui.composeapp.generated.resources.this_action_cannot_be_undone
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ConvertGiftsBottomSheetContent(
    message: Message,
    isDarkMode:Boolean,
    onConvert:(Message) -> Unit,
    onCancel:()->Unit,
) {



    BoxWithConstraints(
        modifier = Modifier
            .background(if (isDarkMode) BASE_DARK else Color.White)
            .fillMaxWidth()
    ) {

        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.convert_gift_to_hearts),
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) Color.White else Color(0xff121213),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = buildAnnotatedString {
                        append("Do you want to convert this gift from ")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)){
                            append(message.giftData?.senderName)
                        }
                        append(" to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)){
                            append("${(message.giftData?.totalHearts ?: 0) -20} Hearts ?")
                        }

                    },
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Image(
                    imageVector = vectorResource(message.giftData?.imageRes!!),
                    contentDescription = null,
                    modifier = Modifier.width(65.53.dp).height(59.86.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.this_action_cannot_be_undone),
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(24.dp))

                ButtonWithText(
                    text = stringResource(Res.string.convert),
                    onClick = {
                        onConvert(message)
                    },
                    bgColor = Color(0xffF33358),
                    textColor = Color.White,
                )


                Spacer(Modifier.height(8.dp))
                Text(
                    modifier = Modifier.noRippleClickable(onCancel),
                    text = stringResource(Res.string.cancel),
                    fontFamily = PoppinsFontFamily(),
                    color = Color(0xffB7B6BF),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(10.dp))


            }

            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(if (isDarkMode) BASE_DARK else Color.White)
            )
        }


    }

}

