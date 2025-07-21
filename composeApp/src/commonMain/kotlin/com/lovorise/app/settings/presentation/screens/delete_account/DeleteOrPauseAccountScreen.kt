package com.lovorise.app.settings.presentation.screens.delete_account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.delete_account
import coinui.composeapp.generated.resources.delete_my_account
import coinui.composeapp.generated.resources.ic_pause_account
import coinui.composeapp.generated.resources.ic_resume_account
import coinui.composeapp.generated.resources.pause_account_msg
import coinui.composeapp.generated.resources.pause_my_account
import coinui.composeapp.generated.resources.pause_my_account_caps
import coinui.composeapp.generated.resources.resume_account_msg
import coinui.composeapp.generated.resources.resume_my_account
import coinui.composeapp.generated.resources.resume_my_account_caps
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class DeleteOrPauseAccountScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountState by accountsViewModel.state.collectAsState()
        val ctx = LocalPlatformContext.current
        var shouldRefreshUser by remember { mutableStateOf(false) }

        LaunchedEffect(true){
            if (accountState.user == null){
                accountsViewModel.getUser(ctx){}
            }
        }

        DeleteOrPauseAccountScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
                if (shouldRefreshUser){
                   // accountsViewModel.getUser(ctx)
                }
            },
            navigateToDeleteFlow = {
                accountsViewModel.resetSuccessState()
                navigator.push(DeleteFlow1Screen(accountsViewModel))
                if (shouldRefreshUser){
                 //   accountsViewModel.getUser(ctx)
                }
            },
            accountsViewModel = accountsViewModel,
            accountsState = accountState,
            shouldRefreshUser = {
                shouldRefreshUser = true
            }
        )
    }
}

@Composable
fun DeleteOrPauseAccountScreenContent(
    isDarkMode:Boolean,
    goBack:()->Unit,
    navigateToDeleteFlow:()->Unit,
    accountsViewModel: AccountsViewModel,
    accountsState: AccountsApiCallState,
    shouldRefreshUser:()->Unit
) {

    var isPaused by remember { mutableStateOf(accountsState.user?.status == "PAUSED") }

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
        ) {

            HeaderWithTitleAndBack(title = stringResource(Res.string.delete_account), onBack = goBack, isDarkMode = isDarkMode)

            Box(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {

                Box(Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

                        if (!isPaused){
                            Image(
                                imageVector = vectorResource(Res.drawable.ic_pause_account),
                                contentDescription = null,
                                modifier = Modifier.size(33.33.dp)
                            )
                        }else{
                            Box(Modifier.size(33.33.dp).background(Color(0xffF33358), CircleShape), contentAlignment = Alignment.Center){
                                Image(
                                    imageVector = vectorResource(Res.drawable.ic_resume_account),
                                    contentDescription = null,
                                    modifier = Modifier.width(14.dp).height(12.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = if (!isPaused) stringResource(Res.string.pause_my_account_caps) else stringResource(Res.string.resume_my_account_caps),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            lineHeight = 27.sp,
                            color = if (isDarkMode) Color.White else Color(0xff101828),
                            letterSpacing = 0.2.sp
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = if (!isPaused) stringResource(Res.string.pause_account_msg) else stringResource(Res.string.resume_account_msg),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                            letterSpacing = 0.2.sp,
                            textAlign = TextAlign.Center
                        )



                    }
                }

                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        ButtonWithText(
                            text = if (!isPaused) stringResource(Res.string.pause_my_account) else stringResource(Res.string.resume_my_account),
                            bgColor = Color(0xffF33358),
                            textColor = Color.White,
                            onClick = {
                                if (!isPaused) accountsViewModel.pauseAccount(context) else accountsViewModel.resumeAccount(context)
                                shouldRefreshUser()
                                isPaused = !isPaused
                            }
                        )

                        Spacer(Modifier.height(25.dp))

                        Text(
                            modifier = Modifier.noRippleClickable(navigateToDeleteFlow),
                            text = stringResource(Res.string.delete_my_account),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                            letterSpacing = 0.2.sp
                        )

                        Spacer(Modifier.height(25.dp))
                    }
                }
            }



        }


        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )

    }

    if (accountsState.isLoading){
        CircularLoader()
    }

}