package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.gift
import coinui.composeapp.generated.resources.ic_verified
import coinui.composeapp.generated.resources.ic_verified_red
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.domain.model.Conversation
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ConversationListItem(conversation: Conversation, showDivider:Boolean, onClick:()->Unit, isDarkMode:Boolean) {
    Column {
        Spacer(Modifier.height(11.dp))
        Row(modifier = Modifier.fillMaxWidth().noRippleClickable(onClick)) {

            RoundedImageWithStatus(
                isOnline = conversation.isOnline,
                imageUrl = conversation.user.medias?.firstOrNull()?.url ?: "",
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.width(14.dp))

            Column {
                Row(modifier = Modifier.fillMaxWidth().height(20.dp),verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = conversation.user.name ?: "",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff101828)
                    )
                    Spacer(Modifier.width(10.dp))
                    if (conversation.user.isVerified == true) {
                        Image(
                            imageVector = vectorResource(if (conversation.user.name == "Team Lovorise") Res.drawable.ic_verified_red else Res.drawable.ic_verified),
                            contentDescription = null,
                            modifier = Modifier.width(17.12.dp).height(16.67.dp)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = conversation.message?.formattedTimestamp ?: "",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth().height(21.dp),verticalAlignment = Alignment.CenterVertically) {
                    if (conversation.message?.type == Message.Type.GIFT && conversation.message.giftData?.imageRes != null){
                        Text(
                            text = stringResource(Res.string.gift),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                        )

                        Image(
                            imageVector = vectorResource(conversation.message.giftData.imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(Modifier.weight(1f))
                    }else{
                        Text(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            text = conversation.message?.text ?: "",
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                        )
                    }

                    Spacer(Modifier.width(20.dp))


                    if (conversation.unreadCount != 0) {
                        Box(
                            Modifier.size(16.dp).background(Color(0xffF33358), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = conversation.unreadCount.toString(),
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
        if (showDivider){
            ConversationSpacer(isDarkMode)
        }
    }
}

@Composable
fun ConversationSpacer(isDarkMode: Boolean) {
    Spacer(Modifier.height(12.dp))
    CustomDivider(if (isDarkMode) Color(0xff24282B) else DISABLED_LIGHT)
}

@Composable
fun RoundedImageWithStatus(isOnline:Boolean,imageUrl:Any,imageSize:Dp=40.dp,indicatorSize:Dp= 11.dp,isDarkMode: Boolean) {
    Box(modifier = Modifier.size(imageSize)){

        if (imageUrl is DrawableResource){
            Image(
                imageVector = vectorResource(imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        }else{
            AsyncImage(
                modifier = Modifier.fillMaxSize().background(if (isDarkMode) CARD_BG_DARK else DISABLED_LIGHT, CircleShape).clip(CircleShape),
                model = imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }


        if (isOnline) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    Modifier.size(indicatorSize).border(1.dp, Color.White, CircleShape)
                        .background(Color(0xff12B76A), CircleShape)
                )
            }
        }
    }
}

