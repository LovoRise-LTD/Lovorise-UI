package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.connecting
import coinui.composeapp.generated.resources.ic_call_red
import coinui.composeapp.generated.resources.ic_inbox_more_options
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_verified
import coinui.composeapp.generated.resources.ic_verified_red
import coinui.composeapp.generated.resources.waiting_for_network
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.chat.domain.model.User
import com.lovorise.app.components.DropShadow
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.DISABLED_LIGHT
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ChatInboxHeader(onBack:()->Unit, user: UserResponse, onMoreClick:()->Unit, onCall:()->Unit, isLovorise:Boolean, onDeleteConversation:()->Unit, isDarkMode:Boolean, isConnected:Boolean) {

    var showMenuItem by remember{ mutableStateOf(false) }

    var connectionMessage by rememberSaveable{ mutableStateOf("") }

    val waiting = stringResource(Res.string.waiting_for_network)
    val connecting = stringResource(Res.string.connecting)
    var wasOffline by rememberSaveable{ mutableStateOf(false) }

    LaunchedEffect(isConnected){
        if (!isConnected) {
            connectionMessage = waiting
            wasOffline = true // Set flag when offline
        } else if (wasOffline) {
            // Only show message if we were previously offline
            connectionMessage = connecting
            wasOffline = false // Reset flag
            delay(3000) // Show message for 3 seconds
            connectionMessage = ""
        }
    }


    Row(
        modifier = Modifier.fillMaxWidth().height(55.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.fillMaxHeight().size(24.dp)
                .noRippleClickable(onBack),
            contentAlignment = Alignment.CenterStart
        ) {
            if (isDarkMode) {
                Icon(
                    modifier = Modifier.width(17.dp).height(13.dp),
                    imageVector = vectorResource(Res.drawable.ic_left),
                    contentDescription = "back",
                    tint = Color.White
                )
            }else{
                Image(
                    modifier = Modifier.width(17.dp).height(13.dp),
                    imageVector = vectorResource(Res.drawable.ic_left),
                    contentDescription = "back"
                )
            }
        }

        Spacer(Modifier.width(16.dp))


        Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {

            RoundedImageWithStatus(
                isOnline = true,
                imageUrl = user.medias?.firstOrNull()?.url ?: "",
                imageSize = 24.dp,
                indicatorSize = 8.dp,
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.width(7.dp))

            Column(modifier = Modifier.height(38.dp),verticalArrangement = if (!user.isActive) Arrangement.Center else Arrangement.SpaceBetween) {
                Text(
                    text = user.name ?: "",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
//                    lineHeight = 16.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )

                if (user.isActive && isConnected && connectionMessage.isBlank()) {
                    Text(
                        text = "Active",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
//                        lineHeight = 18.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                    )
                }

                if (connectionMessage.isNotBlank()){
                    Text(
                        text = connectionMessage,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
//                        lineHeight = 18.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                    )
                }

            }

            if (user.isVerified == true && isLovorise) {
                Spacer(Modifier.width(7.dp))
                Image(
                    modifier = Modifier.size(16.67.dp),
                    imageVector = vectorResource(if (isLovorise) Res.drawable.ic_verified_red else Res.drawable.ic_verified),
                    contentDescription = "call"
                )
            }

        }



        Spacer(Modifier.weight(1f))

        if (!isLovorise) {
            Box(
                modifier = Modifier.fillMaxHeight().size(20.dp)
                    .noRippleClickable(onCall),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(16.67.dp),
                    imageVector = vectorResource(Res.drawable.ic_call_red),
                    contentDescription = "call"
                )
            }

            Spacer(Modifier.width(6.dp))

        }


        // Dot Menu
        Column(
            modifier = Modifier.wrapContentSize(),
            //   horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().size(24.dp)
                    .noRippleClickable{
                        if (!isLovorise) {
                            onMoreClick()
                        }else{
                            showMenuItem = !showMenuItem
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.height(16.dp).width(24.dp),
                    imageVector = vectorResource(Res.drawable.ic_inbox_more_options),
                    contentDescription = "more"
                )
            }
            if (isLovorise && showMenuItem){
                DeleteConversationDropDownMenu(
                    expanded = showMenuItem,
                    onDismissRequest = {
                        showMenuItem = false
                    },
                    onDelete = {
                        showMenuItem = false
                        onDeleteConversation()
                    }
                )
            }
        }



    }
    DropShadow()


}