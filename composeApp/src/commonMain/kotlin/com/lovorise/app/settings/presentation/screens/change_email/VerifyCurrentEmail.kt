package com.lovorise.app.settings.presentation.screens.change_email

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.did_not_get_code
import coinui.composeapp.generated.resources.enter_code_sent_to_your_email
import coinui.composeapp.generated.resources.enter_six_digit_code
import coinui.composeapp.generated.resources.resend_code
import coinui.composeapp.generated.resources.send_code
import coinui.composeapp.generated.resources.verification_code_invalid
import coinui.composeapp.generated.resources.verify
import coinui.composeapp.generated.resources.verify_email
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.SFProDisplayFontFamily
import com.lovorise.app.accounts.domain.model.ChangeEmailRequest
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.VerifyEmailScreenModel
import com.lovorise.app.accounts.presentation.signup.email.components.OtpInputField
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource

class VerifyCurrentEmail : Screen {

    @Composable
    override fun Content() {

        val verifyEmailScreenModel = rememberScreenModel { VerifyEmailScreenModel() }

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()

        val accountsState by accountsViewModel.state.collectAsState()
        val ctx = LocalPlatformContext.current

        LaunchedEffect(true){
            accountsViewModel.changeEmail(ChangeEmailRequest(1,null,null,null,null),ctx)
        }

        VerifyCurrentEmailContent(
            email = accountsState.user?.email ?: "",
            isDarkMode = isDarkMode,
            verifyEmailScreenModel = verifyEmailScreenModel,
           // navigateToVerifyPassword = {
             //   navigator.push(VerifyPasswordScreen())
          //  },
            onBack = {
                navigator.pop()
            },
            navigateToAddNewEmail = {
                navigator.push(AddEmailScreen())
            },
            accountsViewModel = accountsViewModel,
            accountsState = accountsState
        )
    }

}


@OptIn(InternalVoyagerApi::class)
@Composable
fun VerifyCurrentEmailContent(email:String,isDarkMode:Boolean,verifyEmailScreenModel: VerifyEmailScreenModel,onBack:()->Unit,navigateToAddNewEmail:()->Unit,accountsViewModel: AccountsViewModel,accountsState:AccountsApiCallState) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val invalidVerificationCode = stringResource(Res.string.verification_code_invalid)

    var otpValue by remember { mutableStateOf("") }

   // var error by remember { mutableStateOf("") }

    var isEnabled by remember { mutableStateOf(false) }

    val state = verifyEmailScreenModel.state.collectAsState().value
    val ctx = LocalPlatformContext.current
    var canNavigate by remember { mutableStateOf(false) }


    BackHandler(true){
        onBack()
    }

    LaunchedEffect(true){
        verifyEmailScreenModel.startCountDown()
    }

    LaunchedEffect(accountsState.success){
        if (canNavigate){
            if (accountsState.success) {
                navigateToAddNewEmail()
                accountsViewModel.resetSuccessState()
            }else{
               // accountsViewModel.updateErrorValue(invalidVerificationCode)
            }
        }else{
            accountsViewModel.resetSuccessState()
        }
    }

    LaunchedEffect(key1 = otpValue){
        if(otpValue.length == 6){
            accountsViewModel.updateErrorValue("")
         //   error = ""
            isEnabled = true
        }else{
            isEnabled = false
        }
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
            HeaderWithTitleAndBack(title = stringResource(Res.string.verify_email), onBack = onBack, isDarkMode = isDarkMode)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = stringResource(Res.string.enter_six_digit_code),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )
                Spacer(Modifier.height(2.dp))

                Text(
                    text = stringResource(Res.string.enter_code_sent_to_your_email,email),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )

                Spacer(Modifier.height(24.dp))

                OtpInputField(
                    otpText = otpValue,
                    otpLength = 6,
                    onOtpModified = { value, _ ->
                        otpValue = value
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))




                if (!accountsState.error.isNullOrBlank()) {
                    Text(
                        text = accountsState.error,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = Color(0xffD92D20),
                        letterSpacing = 0.2.sp
                    )
                    Spacer(Modifier.height(16.dp))
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${stringResource(Res.string.did_not_get_code)} ",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                        letterSpacing = 0.2.sp
                    )

                    Text(
                        modifier = Modifier.noRippleClickable {
                            if (!state.showTimer) {
                                verifyEmailScreenModel.startCountDown()
                                accountsViewModel.changeEmail(ChangeEmailRequest(1,null,null,null,null),ctx)
//                                accountsViewModel.resendEmailOtp(context)
                            }
                        },
                        text = stringResource(Res.string.resend_code),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = Color( if (!state.showTimer) 0xffF33358 else 0xff98A2B3),
                        letterSpacing = 0.2.sp
                    )

                }

                if (state.showTimer){
                    Spacer(Modifier.height(9.dp))
                    Text(
                        text = "${stringResource(Res.string.send_code)} ${state.timerCountDown.collectAsState(59).value}s",
                        fontFamily = SFProDisplayFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                        letterSpacing = 0.2.sp
                    )
                }

//                Spacer(Modifier.height(16.dp))
//
//                Text(
//                    modifier = Modifier.noRippleClickable(navigateToVerifyPassword),
//                    text = stringResource(Res.string.cannot_access_this_email),
//                    fontFamily = PoppinsFontFamily(),
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 14.sp,
//                    lineHeight = 21.sp,
//                    color = Color(0xffF33358),
//                    letterSpacing = 0.2.sp
//                )

                Spacer(Modifier.height(55.dp))

                ButtonWithText(
                    text = stringResource(Res.string.verify),
                    bgColor = if (isEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
                            canNavigate = true
                            accountsViewModel.changeEmail(ChangeEmailRequest(2,accountsState.changeEmailToken!!,otpValue,null,null),ctx)
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

    if (accountsState.isLoading){
        CircularLoader()
    }
}

