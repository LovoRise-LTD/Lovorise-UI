package com.lovorise.app.accounts.presentation.signup.email

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
import cafe.adriel.voyager.koin.koinScreenModel
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
import coinui.composeapp.generated.resources.verify
import coinui.composeapp.generated.resources.verify_email
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.SFProDisplayFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.components.OtpInputField
import com.lovorise.app.accounts.presentation.signup.password.CreatePasswordScreen
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import io.ktor.util.reflect.instanceOf
import org.jetbrains.compose.resources.stringResource

class VerifyEmailScreen(private val email:String) : Screen{

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val verifyEmailScreenModel = rememberScreenModel { VerifyEmailScreenModel() }

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        val ctx = LocalPlatformContext.current

        LaunchedEffect(true){
            println("the email is $email")
            accountsViewModel.setSignupPage(SignupFlowPages.VERIFY_EMAIL_SCREEN,ctx)
            accountsViewModel.setCurrentEmail(email)
        }

        VerifyEmailScreenContent(
            isDarkMode = isDarkMode,
            email = maskEmail(email),
            onBack = {
                accountsViewModel.setCurrentEmail("")
                if (navigator.items.contains(EmailScreen())){
                    navigator.popUntil { it.instanceOf(EmailScreen::class) }
                }else{
                    navigator.push(EmailScreen())
                    navigator.push(EmailScreen())
                    navigator.pop()
                }
              //  accountsViewModel.resetSignupFlow()
            },
            navigateToCreatePassword = {
                navigator.push(CreatePasswordScreen())
            },
            verifyEmailScreenModel = verifyEmailScreenModel,
            accountsViewModel = accountsViewModel
        )
    }
}

fun maskEmail(email: String): String {
    return email
//    val atIndex = email.indexOf("@")
//    return if (atIndex > 1) {
//        val firstChar = email.first()
//        val remainingChars = atIndex - 1
//        val asterisks = "*".repeat(remainingChars)
//        val domain = email.substring(atIndex)
//        "$firstChar$asterisks$domain"
//    } else {
//        email // In case the email is too short to mask properly
//    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun VerifyEmailScreenContent(
    isDarkMode:Boolean,
    email:String,
    onBack:()->Unit,
    navigateToCreatePassword: (String)->Unit,
    verifyEmailScreenModel: VerifyEmailScreenModel,
    accountsViewModel: AccountsViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var otpValue by remember { mutableStateOf("") }


    var isEnabled by remember { mutableStateOf(false) }

    val state = verifyEmailScreenModel.state.collectAsState().value

    BackHandler(true){
//        accountsViewModel.showExitConfirmationDialog()
        onBack()
    }

    val context = LocalPlatformContext.current

    val accountsState by accountsViewModel.state.collectAsState()

    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            navigateToCreatePassword(otpValue)
        }
    }

    LaunchedEffect(accountsState.error){
        if (!accountsState.error.isNullOrBlank()){
            accountsViewModel.updateErrorValue(accountsState.error!!)
        }
    }


    LaunchedEffect(true){
        verifyEmailScreenModel.startCountDown()
    }

    LaunchedEffect(key1 = otpValue){
        if(otpValue.length == 6){
            accountsViewModel.updateErrorValue("")
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) Color.White else Color(0xff344054)
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
                        text = accountsState.error!!,
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
                        color = if(isDarkMode) Color.White else Color(0xff475467),
                        letterSpacing = 0.2.sp
                    )

                    Text(
                        modifier = Modifier.noRippleClickable {
                            if (!state.showTimer){
                                println("called the resend func")
                                accountsViewModel.updateErrorValue("")
                                accountsViewModel.resendEmailOtp(context)
                                verifyEmailScreenModel.startCountDown()
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
                        color = if(isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                        letterSpacing = 0.2.sp
                    )
                }



                Spacer(Modifier.height(68.dp))

                ButtonWithText(
                    text = stringResource(Res.string.verify),
                    bgColor = if (isEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
                            accountsViewModel.verifyEmail(email,otpValue)
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

    if (accountsState.showExitConfirmationDialog){
        SignupConfirmExitDialog(
            onCancel = accountsViewModel::hideExitConfirmationDialog,
            onConfirm = {
                accountsViewModel.hideExitConfirmationDialog()
                onBack()
            },
            isDarkMode = isDarkMode
        )
    }


    if (accountsState.isLoading){
        CircularLoader()
    }

}