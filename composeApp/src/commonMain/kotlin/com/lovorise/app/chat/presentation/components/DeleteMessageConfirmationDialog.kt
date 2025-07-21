package com.lovorise.app.chat.presentation.components

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
import androidx.compose.material.Icon
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.are_you_sure_want_to_delete_msg
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.delete
import coinui.composeapp.generated.resources.delete_for_user
import coinui.composeapp.generated.resources.delete_message
import coinui.composeapp.generated.resources.ic_xmark
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun DeleteMessageConfirmationDialog(onCancel:()->Unit,onDelete:()->Unit,name:String,isDarkMode:Boolean) {

    var deleteForOtherUser by rememberSaveable { mutableStateOf(false) }

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
                        tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.delete_message),
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
                    text = stringResource(Res.string.are_you_sure_want_to_delete_msg),
                    color = if(isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 21.sp,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp
                )


                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){

                    Box(modifier = Modifier.size(24.dp).noRippleClickable{deleteForOtherUser = !deleteForOtherUser}, contentAlignment = Alignment.Center) {
                        Checkbox(
                            checked = deleteForOtherUser,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xffF33358),
                                checkmarkColor = Color.White,
                                uncheckedColor = Color(0xff344054)
                            ),
                            onCheckedChange = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }


                    val text = stringResource(Res.string.delete_for_user, name)

                    val styledText = buildAnnotatedString {
                        val nameStartIndex = text.indexOf(name)
                        append(text)
                        addStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                              //  color = Color(0xFF475467)
                            ),
                            start = nameStartIndex,
                            end = nameStartIndex + name.length
                        )
                    }

                    Text(
                        modifier = Modifier.weight(1f),
                        text = styledText,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 21.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                    )






                }


                Spacer(Modifier.height(16.dp))

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
                            color = if (isDarkMode) Color.White else Color(0xff101828)
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
                            .noRippleClickable(onDelete),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.delete),
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