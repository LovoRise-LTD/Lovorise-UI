package com.lovorise.app.accounts.presentation.signup.drink

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
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
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.drink_preferences
import coinui.composeapp.generated.resources.how_often_do_you_drink
import coinui.composeapp.generated.resources.ic_drink
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.skip
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.smoke.SmokeScreen
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomLinearProgressIndicator
import com.lovorise.app.components.ElevatedChipItem
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class DrinkScreen : Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.setSignupPage(SignupFlowPages.DRINK_SCREEN,ctx)
        }


        DrinkScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.navigateToOnBoarding()
//                accountsViewModel.resetSignupFlow()
            },
            onSkip = {
                navigator.push(SmokeScreen())
            },
            onNext = {
                navigator.push(SmokeScreen())
            },
            accountsViewModel = accountsViewModel
        )


    }
}

@OptIn(ExperimentalLayoutApi::class, InternalVoyagerApi::class)
@Composable
fun DrinkScreenContent(
    isDarkMode:Boolean,
    goBack:()->Unit,
    onSkip:()->Unit,
    onNext:()->Unit,
    accountsViewModel: AccountsViewModel
) {

    val res = stringArrayResource(Res.array.drink_preferences)
    var isEnabled by remember { mutableStateOf(false) }
    val items by remember { mutableStateOf(res) }
    var selectedIndex by remember { mutableStateOf(-1) }

    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
       // goBack()
    }

    val accountsState by accountsViewModel.state.collectAsState()

    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            onNext()
        }
    }

    LaunchedEffect(selectedIndex){
        isEnabled = selectedIndex != -1
    }

    Column(
        modifier = Modifier
    ) {

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
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(3.dp))

            Box(
                modifier = Modifier.size(24.dp).align(Alignment.Start).noRippleClickable(accountsViewModel::showExitConfirmationDialog),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_xmark),
                    contentDescription = null,
                    modifier = Modifier.size(10.dp),
                    tint = if (isDarkMode) Color.White else Color(0xff475467)
                )
            }
            Spacer(Modifier.height(8.dp))

            CustomLinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                strokeWidth = 6.dp,
                progress = 12f/16f,
                isDarkMode = isDarkMode
            )


            Spacer(Modifier.height(40.dp))

            Icon(
                imageVector = vectorResource(Res.drawable.ic_drink),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isDarkMode) Color.White else Color(0xff344054)
            )


            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(Res.string.how_often_do_you_drink),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff344054)
            )

            Spacer(Modifier.height(40.dp))



            FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
                items.forEachIndexed { index, value ->
                    ElevatedChipItem(text = value, isSelected = selectedIndex == index, onClick = { selectedIndex = if (selectedIndex == index){
                        -1
                    }else {
                        index
                    }}, isDarkMode = isDarkMode)
                }
            }


            Spacer(Modifier.height(40.dp))

            Text(
                modifier = Modifier.noRippleClickable(onSkip),
                text = stringResource(Res.string.skip),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = if (isDarkMode) Color.White else Color(0xff344054),
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )


            Spacer(Modifier.height(16.dp))



            ButtonWithText(
                text = stringResource(Res.string.next),
                bgColor = if (isEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (isEnabled && selectedIndex >= 0) {
                        accountsViewModel.addDrink(items[selectedIndex])
                    }
                }
            )


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

    if (accountsState.showExitConfirmationDialog){
        SignupConfirmExitDialog(
            onCancel = accountsViewModel::hideExitConfirmationDialog,
            onConfirm = {
                accountsViewModel.hideExitConfirmationDialog()
                goBack()
            },
            isDarkMode = isDarkMode
        )
    }



}
