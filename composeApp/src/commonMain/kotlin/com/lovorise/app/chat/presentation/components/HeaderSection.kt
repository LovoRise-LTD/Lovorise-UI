package com.lovorise.app.chat.presentation.components

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.chat_options
import coinui.composeapp.generated.resources.chats
import coinui.composeapp.generated.resources.connecting
import coinui.composeapp.generated.resources.ic_chat_options
import coinui.composeapp.generated.resources.waiting_for_network
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HeaderSection(isDarkMode:Boolean,isChatScreenOptionsVisible:Boolean,onToggleChatScreenOptions:()->Unit,onNewMessage:()->Unit,onSelectChats:()->Unit,onReadAll:()->Unit,isConnected:Boolean) {

    val chatOptions = stringArrayResource(Res.array.chat_options)

    var wasOffline by rememberSaveable { mutableStateOf(false) }

    var connectionMessage by rememberSaveable{ mutableStateOf("") }
    val waiting = stringResource(Res.string.waiting_for_network)
    val connecting = stringResource(Res.string.connecting)

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


    Row(modifier = Modifier.fillMaxWidth().height(43.dp).padding(horizontal = 16.dp), horizontalArrangement =  Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
        Text(
            text = stringResource(Res.string.chats),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
         //   lineHeight = 27.sp,
            letterSpacing = 0.2.sp,
            color = if (isDarkMode) Color.White else Color(0xff101828)
        )

        if (connectionMessage.isNotBlank()) {
            Text(
                text = connectionMessage,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                //   lineHeight = 27.sp,
                letterSpacing = 0.2.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
            )
        }

        Column {
            Icon(
                modifier = Modifier.size(18.dp).noRippleClickable(onToggleChatScreenOptions),
                imageVector = vectorResource(Res.drawable.ic_chat_options),
                tint = if (isDarkMode) Color.White else Color(0xff101828),
                contentDescription = null
            )
            Spacer(Modifier.height(16.5.dp))

            if (isChatScreenOptionsVisible){
                DropdownMenu(
                    expanded = isChatScreenOptionsVisible,
                    onDismissRequest = onToggleChatScreenOptions,
                    containerColor = if (isDarkMode) CARD_BG_DARK else Color.White
                ){
                    Box(Modifier.size(width = 154.dp, height = 123.dp)){
                        Column(Modifier.fillMaxSize().padding(16.dp)) {
                            chatOptions.forEachIndexed {index,item ->
                                Box(modifier = Modifier.fillMaxWidth().height(25.dp).noRippleClickable {
                                    when (index) {
                                        0 -> onNewMessage()
                                        1 -> onSelectChats()
                                        else -> onReadAll()
                                    }
                                }, contentAlignment = Alignment.CenterStart){
                                    Text(
                                        text = item,
                                        fontFamily = PoppinsFontFamily(),
                                        fontWeight = FontWeight.Medium,
                                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                                        letterSpacing = 0.2.sp
                                    )
                                }
                                if(index != chatOptions.lastIndex){
                                    Spacer(Modifier.height(8.dp))
                                }
                            }


                        }
                    }
                }
            }
        }

    }


}