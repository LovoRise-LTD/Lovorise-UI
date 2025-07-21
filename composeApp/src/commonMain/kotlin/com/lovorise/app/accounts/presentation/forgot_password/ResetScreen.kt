package com.lovorise.app.accounts.presentation.forgot_password

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.email
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.reset
import coinui.composeapp.generated.resources.we_will_email_you_a_code_to_reset_your_password
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.ForgotPasswordData
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.utils.isEmailValid
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomTextField
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource

class ResetScreen(private val buttonText:String? = null,private val startingScreen:Screen? = null) : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.resetSuccessState()
        }

        ResetScreenContent(
            isDarkMode = isDarkMode,
            navigateToVerify = { email,resetToken ->
                navigator.push(VerifyForgotPasswordOtpScreen(email = email, resetToken = resetToken, buttonText = buttonText, startingScreen = startingScreen))
                accountsViewModel.reloadState()
            },
            goBack = {
                navigator.pop()
            },
            accountsViewModel = accountsViewModel
        )

    }
}


@Composable
fun ResetScreenContent(
    isDarkMode:Boolean,
    navigateToVerify:(String,String)->Unit,
    goBack:()->Unit,
    accountsViewModel: AccountsViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }

    var isEnabled by remember { mutableStateOf(false) }


    val accountsState by accountsViewModel.state.collectAsState()

    val ctx = LocalPlatformContext.current


    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            accountsState.forgotPasswordResponse?.resetToken?.let {
                navigateToVerify(email,it)
            }
        }
    }

    LaunchedEffect(key1 = email){
        isEnabled = isEmailValid(email)
    }

    Column(
        modifier = Modifier.noRippleClickable {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
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
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderWithTitleAndBack(title = stringResource(Res.string.reset), onBack = goBack, isDarkMode = isDarkMode)

            Column(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(17.dp))

                CustomTextField(
                    label = stringResource(Res.string.email),
                    value = email,
                    onChange = { email = it },
                    keyboardType = KeyboardType.Email,
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = stringResource(Res.string.we_will_email_you_a_code_to_reset_your_password),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                )


//
//            if (error.isNotBlank()) {
//                Text(
//                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
//                    text = error,
//                    fontFamily = PoppinsFontFamily(),
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 14.sp,
//                    lineHeight = 21.sp,
//                    color = Color(0xffD92D20),
//                )
//                Spacer(Modifier.height(8.dp))
//            }


                Spacer(Modifier.height(176.dp))

                ButtonWithText(
                    text = stringResource(Res.string.next),
                    bgColor = if (isEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
                            keyboardController?.hide()
                            accountsViewModel.forgotPassword(
                                data = ForgotPasswordData(
                                    page = 1,
                                    email = email,
                                    resetToken = null,
                                    code = null,
                                    password = null
                                ),
                                ctx = ctx
                            )
                            //navigateToVerify(email)
                        }
                    }
                )
            }


        }



        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )



    }

    if(accountsState.isLoading){
        CircularLoader()
    }







}