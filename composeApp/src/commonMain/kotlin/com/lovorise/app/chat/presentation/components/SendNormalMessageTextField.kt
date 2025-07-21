package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_attachment
import coinui.composeapp.generated.resources.ic_gift
import coinui.composeapp.generated.resources.ic_send_normal_message_disabled
import coinui.composeapp.generated.resources.ic_send_normal_message_enabled
import coinui.composeapp.generated.resources.message
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun SendNormalMessageTextField(horizontalPadding:Dp = 16.dp, label:String = stringResource(Res.string.message), value:String, onValueChange:(String)->Unit, roundedCornerShape: Dp = 8.dp, onSend:()->Unit, focusRequester: FocusRequester, replyData: Message.ReplyData?, onCancelReply:()->Unit, onEmojiClick:()->Unit, onFocus:()->Unit, isDarkMode:Boolean, showSend:Boolean, selectedMediaCount:Int, onShowMediaPicker:()->Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .padding(vertical = 4.dp)
            .background(if (isDarkMode) CARD_BG_DARK else Color.White,shape = RoundedCornerShape(roundedCornerShape))
//            .focusRequester(focusRequester)
//            .focusable()
            .wrapContentHeight()
          //  .imePadding()
          //  .height(if (replyData == null) 40.97.dp else 84.dp)
            .border(width = 1.dp,if (isDarkMode) CARD_BG_DARK else Color(0xFFEAECF0), shape = RoundedCornerShape(roundedCornerShape))

    ) {

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.24.dp), verticalArrangement = Arrangement.Center){

            if (replyData != null){
                ReplyBox(
                    replyData = replyData,
                    forTextField = true,
                    isDarkMode = isDarkMode,
                    onCancel = onCancelReply
                )
                Spacer(Modifier.height(8.53.dp))
            }


            Row {
                Box(Modifier.size(24.dp), contentAlignment = Alignment.CenterStart){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_gift),
                        contentDescription = "emoji Icon",
                        modifier = Modifier.size(20.dp).noRippleClickable(onEmojiClick)
                    )
                }

                Spacer(Modifier.width(4.dp))

                CompositionLocalProvider(LocalTextSelectionColors provides TextSelectionColors(Color(0xffF33358),Color(0xffF33358))) {
                    BasicTextField(
                        value = TextFieldValue(value, selection = TextRange(value.length)).text,
                        maxLines = 5,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth().weight(1f).noRippleClickable(onFocus),
                        decorationBox = { padding ->
                            if (value.isBlank()) {
                                Text(
                                    text = label,
                                    color = Color(0xff98A2B3),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = 0.2.sp,
                                    lineHeight = 24.sp,
                                    fontFamily = PoppinsFontFamily()
                                )
                            }
                            padding()
//                    innerTextField() // The actual text field
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.17.sp,
                            lineHeight = 20.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = if (isDarkMode) Color.White else Color.Black
                        ),

                        cursorBrush = SolidColor(Color(0xffF33358))
                    )
                }
                Spacer(Modifier.width(8.dp))
                Box(Modifier.size(24.dp).noRippleClickable( if (showSend || value.isNotBlank()) onSend else onShowMediaPicker), contentAlignment = Alignment.Center){
                    if (showSend || value.isNotBlank()){
                        Image(
                            imageVector = vectorResource(if (selectedMediaCount > 0 || value.isNotBlank()) Res.drawable.ic_send_normal_message_enabled else Res.drawable.ic_send_normal_message_disabled),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp).clip(CircleShape).noRippleClickable(onSend),
                            contentScale = ContentScale.FillBounds
                        )
                        if (selectedMediaCount > 0) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                                Box(
                                    Modifier.size(10.dp).border(
                                        width = 1.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    ).background(PRIMARY, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = selectedMediaCount.toString(),
                                        fontFamily = PoppinsFontFamily(),
                                        fontSize = 7.11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                    }else{
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_attachment),
                            contentDescription = null,
                            modifier = Modifier.size(17.76.dp),
                            tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                        )
                    }

                }
            }
        }

    }

}