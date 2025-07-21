package com.lovorise.app.settings.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.enable_all_notifications
import coinui.composeapp.generated.resources.likes
import coinui.composeapp.generated.resources.matches
import coinui.composeapp.generated.resources.message
import coinui.composeapp.generated.resources.new_activity
import coinui.composeapp.generated.resources.notifications
import coinui.composeapp.generated.resources.reels
import coinui.composeapp.generated.resources.secret_crush
import coinui.composeapp.generated.resources.visitors
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.AppSettingsData
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.settings.presentation.components.TextWithSwitchBox
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource

class NotificationSettingsScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()
        val context = LocalPlatformContext.current

        val lifecycleOwner = LocalLifecycleOwner.current


//        DisposableEffect(Unit){
//            onDispose {
//                accountsState.appSettingsData?.notification?.let {
//                    accountsViewModel.updateNotificationSettings(context, data = it,false)
//                }
//            }
//        }

        DisposableEffect(lifecycleOwner) {
            val observer = object : DefaultLifecycleObserver {
                override fun onStop(owner: LifecycleOwner) {
                    accountsState.appSettingsData?.notification?.let {
                        accountsViewModel.updateNotificationSettings(context, data = it, false)
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        NotificationSettingsScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            accountsState = accountsState,
            accountsViewModel = accountsViewModel
        )

    }
}


@Composable
fun NotificationSettingsScreenContent(isDarkMode:Boolean,goBack:()->Unit,accountsState:AccountsApiCallState,accountsViewModel: AccountsViewModel) {

    val state = accountsState.appSettingsData?.notification ?: AppSettingsData.NotificationSettings(messages = true,likes = true,matches = true, visitors = true,secretCrush = true,reels = true,offerAndPromotions = true)
    val context = LocalPlatformContext.current

    Column(modifier = Modifier) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()


        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            HeaderWithTitleAndBack(title = stringResource(Res.string.notifications), onBack = goBack, addShadow = false, isDarkMode = isDarkMode)

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(32.dp))
                TextWithSwitchBox(
                    text = stringResource(Res.string.enable_all_notifications),
                    isChecked = state.allEnabled(),
                    onCheckChanged = {
                        val allEnabled = state.allEnabled()
                        accountsViewModel.updateNotificationSettings(
                            context,
                            AppSettingsData.NotificationSettings(
                                messages = !allEnabled,
                                likes = !allEnabled,
                                matches = !allEnabled,
                                visitors = !allEnabled,
                                secretCrush = !allEnabled,
                                reels = !allEnabled,
                                offerAndPromotions = !allEnabled
                            )
                        )
                    },
                    isDarkMode = isDarkMode
                )
                Spacer(Modifier.height(24.dp))
                CustomDivider(isDarkMode = isDarkMode)

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(Res.string.new_activity),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )
                Spacer(Modifier.height(16.dp))

                TextWithSwitchBox(
                    text = stringResource(Res.string.message),
                    isChecked = state.messages,
                    onCheckChanged = {
                        accountsViewModel.updateNotificationSettings(context,state.copy(messages = !state.messages))
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                TextWithSwitchBox(
                    text = stringResource(Res.string.likes),
                    isChecked = state.likes,
                    onCheckChanged = {
                        accountsViewModel.updateNotificationSettings(context,state.copy(likes = !state.likes))
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                TextWithSwitchBox(
                    text = stringResource(Res.string.matches),
                    isChecked = state.matches,
                    onCheckChanged = {
                        accountsViewModel.updateNotificationSettings(context,state.copy(matches = !state.matches))
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                TextWithSwitchBox(
                    text = stringResource(Res.string.visitors),
                    isChecked = state.visitors,
                    onCheckChanged = {
                        accountsViewModel.updateNotificationSettings(context,state.copy(visitors = !state.visitors))
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                TextWithSwitchBox(
                    text = stringResource(Res.string.secret_crush),
                    isChecked = state.secretCrush,
                    onCheckChanged = {
                        accountsViewModel.updateNotificationSettings(context,state.copy(secretCrush = !state.secretCrush))
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                TextWithSwitchBox(
                    text = stringResource(Res.string.reels),
                    isChecked = state.reels,
                    onCheckChanged = {
                        accountsViewModel.updateNotificationSettings(context,state.copy(reels = !state.reels))
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(24.dp))

//                CustomDivider()
//
//                Spacer(Modifier.height(24.dp))
//
//                Text(
//                    text = stringResource(Res.string.other),
//                    fontFamily = PoppinsFontFamily(),
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 14.sp,
//                    lineHeight = 20.sp,
//                    letterSpacing = 0.2.sp,
//                    color = Color(0xff101828)
//                )
//
//                Spacer(Modifier.height(16.dp))
//
//                TextWithSwitchBox(
//                    text = stringResource(Res.string.offer_and_promotions),
//                    isChecked = state.offerAndPromotions,
//                    onCheckChanged = {
//                        accountsViewModel.updateNotificationSettings(context,state.copy(offerAndPromotions = !state.offerAndPromotions))
//                    }
//                )

            }

        }
        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }

//    if (accountsState.isLoading){
//        CircularLoader(true)
//    }

}

fun AppSettingsData.NotificationSettings.allEnabled(): Boolean {
    return listOf(
        messages, likes, matches, visitors, secretCrush, reels, offerAndPromotions
    ).all { it }
}