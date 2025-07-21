package com.lovorise.app.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_splash_logo
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.onboarding.OnboardingScreen
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.connectivity.ConnectivityViewModel
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.vectorResource

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val connectivityViewModel = navigator.koinNavigatorScreenModel<ConnectivityViewModel>()
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()
        val ctx = LocalPlatformContext.current
        var isSplashTimeout by rememberSaveable { mutableStateOf(false) }

        val screen = remember { accountsViewModel.getStartingScreen(ctx)  }

        LaunchedEffect(true){

            themeViewModel.init(ctx)

            delay(if(screen !is HomeScreen) 500 else  3000)
            if (screen !is HomeScreen){
                navigator.push(screen)
            }else{
                if (isConnected){
                    navigator.push(screen)
                }else{
                    isSplashTimeout = true
                }
            }
        }

        LaunchedEffect(true){
            if (screen is HomeScreen) {
                accountsViewModel.getUser(ctx, navigateToSignIn = {
                    navigator.push(OnboardingScreen())
                })
                profileScreenModel.getPurchaseInfo(ctx)
            }
        }

        LaunchedEffect(isConnected,isSplashTimeout){
            if (isConnected && isSplashTimeout){
                delay(if(screen !is HomeScreen) 500 else  3000)
                navigator.push(screen)
            }
        }

        SplashScreenContent(isConnected)
    }
}

@Composable
fun SplashScreenContent(isConnected:Boolean){

    var color by rememberSaveable{ mutableStateOf<Long?>(null) }

    Box {
        Column(
            Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(
                    listOf(Color(0xffF3335D),Color(0xffF33386))
                )),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Image(
                imageVector = vectorResource(Res.drawable.ic_splash_logo),
                modifier = Modifier.size(144.dp).clip(CircleShape),
                contentDescription = null
            )


        }

        Column {
            Spacer(
                modifier = Modifier
                    .then(if (color != null) Modifier.background(Color(color!!)) else Modifier)
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .fillMaxWidth()
            )
            ConnectivityToast(updateColor = {
                if (!isAndroid()){
                    color = it
                }
            })
        }
    }


}