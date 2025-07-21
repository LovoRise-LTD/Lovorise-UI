package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_notification
import coinui.composeapp.generated.resources.notifications
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationSection(body:String,unreadCount:Int,time:String, onClick:()->Unit,isDarkMode:Boolean) {
    Column {
        Spacer(Modifier.height(11.dp))
        Row(modifier = Modifier.fillMaxWidth().noRippleClickable(onClick)) {

            RoundedImageWithStatus(
                isOnline = false,
                imageUrl = Res.drawable.ic_notification,
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.width(14.dp))

            Column {
                Row(modifier = Modifier.fillMaxWidth().height(20.dp),verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.notifications),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828)
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = time,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth().height(21.dp),verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        text = body,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                    )

                    Spacer(Modifier.width(20.dp))


                    if (unreadCount != 0) {
                        Box(
                            Modifier.size(16.dp).background(Color(0xffF33358), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = unreadCount.toString(),
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp,
                                //   lineHeight = 13.sp,
                                letterSpacing = 0.2.sp,
                                color = Color.White
                            )
                        }
                    }
                }

            }
        }
        ConversationSpacer(isDarkMode)
    }
}
