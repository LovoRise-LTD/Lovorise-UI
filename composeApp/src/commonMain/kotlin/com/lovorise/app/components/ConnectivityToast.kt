package com.lovorise.app.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.you_are_back_online
import coinui.composeapp.generated.resources.you_are_using_lovorise_offline
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.libs.connectivity.ConnectivityViewModel
import com.lovorise.app.ui.DISABLED_LIGHT
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ConnectivityToast(updateColor:(Long)->Unit = {}) {
    val connectivityViewModel = koinInject<ConnectivityViewModel>()
    val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()

    var showOnlineMessage by rememberSaveable { mutableStateOf(false) }
    var wasOffline by rememberSaveable { mutableStateOf(false) } // Track previous state

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            updateColor(0xffEAECF0)
            wasOffline = true // Set flag when offline
        } else if (wasOffline) {
            // Only show message if we were previously offline
            showOnlineMessage = true
            wasOffline = false // Reset flag
            updateColor(0xff1BDE83)
            delay(3000) // Show message for 3 seconds
            showOnlineMessage = false
        }
    }

    AnimatedVisibility(
        visible = !isConnected || showOnlineMessage,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Box(Modifier.fillMaxWidth().height(26.dp).background(if (isConnected) Color(0xff1BDE83) else DISABLED_LIGHT), contentAlignment = Alignment.Center){
            Text(
                text = stringResource(if (isConnected) Res.string.you_are_back_online else Res.string.you_are_using_lovorise_offline),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if(isConnected) Color.White else Color(0xff344054),
                //  lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )
        }
    }

}