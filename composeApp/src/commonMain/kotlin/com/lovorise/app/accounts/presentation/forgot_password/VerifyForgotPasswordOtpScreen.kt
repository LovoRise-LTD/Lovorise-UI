package com.lovorise.app.accounts.presentation.forgot_password

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
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.check_your_email
import coinui.composeapp.generated.resources.did_not_get_code
import coinui.composeapp.generated.resources.if_we_found_an_account_email_has_been_sent
import coinui.composeapp.generated.resources.please_check_your_email_in_a_moment
import coinui.composeapp.generated.resources.resend_code
import coinui.composeapp.generated.resources.send_code
import coinui.composeapp.generated.resources.verify
import coinui.composeapp.generated.resources.verify_email
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.SFProDisplayFontFamily
import com.lovorise.app.accounts.domain.AccountsRepo
import com.lovorise.app.accounts.domain.model.ForgotPasswordData
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.components.OtpInputField
import com.lovorise.app.accounts.presentation.signup.email.maskEmail
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
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class VerifyForgotPasswordOtpScreen(private val email:String,private val buttonText:String? = null,private val startingScreen:Screen?=null,private val resetToken:String? = null)  : Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = rememberScreenModel { ForgotPasswordScreenModel() }

        val accountsRepo = koinInject<AccountsRepo>()
        val accountsViewModel = rememberScreenModel { AccountsViewModel(accountsRepo) }


        VerifyForgotPasswordOtpScreenContent(
            isDarkMode = isDarkMode,
            email = maskEmail(email),
            onBack = { navigator.pop() },
            onVerify = {
                navigator.push(CreatePasswordAndSignInScreen(buttonText,startingScreen, resetToken = resetToken))
                accountsViewModel.reloadState()
            },
            screenModel = screenModel,
            accountsViewModel = accountsViewModel,
            resetToken = resetToken,
            navigateToPasswordScreen = {
                navigator.push(CreatePasswordScreen())
            },
            navigateTo = {
                navigator.push(it)
            }
        )

    }
}

@Composable
fun VerifyForgotPasswordOtpScreenContent(
    isDarkMode:Boolean,
    email:String,
    onBack:()->Unit,
    onVerify: ()->Unit,
    screenModel: ForgotPasswordScreenModel,
    accountsViewModel: AccountsViewModel,
    resetToken: String?,
    navigateToPasswordScreen:()->Unit,
    navigateTo:(Screen)->Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var otpValue by remember { mutableStateOf("") }

 //   var error by remember { mutableStateOf("") }

    var isEnabled by remember { mutableStateOf(false) }

    val state = screenModel.state.collectAsState().value

    LaunchedEffect(true){
        screenModel.startCountDown()
    }

    val accountsState by accountsViewModel.state.collectAsState()

    val ctx = LocalPlatformContext.current


    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            if (accountsState.forgotPasswordResponse?.redirectToRegistration == true){
                if (!accountsState.forgotPasswordResponse?.nextStatus.isNullOrBlank()){
                    navigateTo(accountsViewModel.getScreenFromString(accountsState.forgotPasswordResponse?.nextStatus!!))
                }else {
                    navigateToPasswordScreen()
                }
            }else {
                onVerify()
            }
        }
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
                    text = stringResource(Res.string.check_your_email),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )
                Spacer(Modifier.height(2.dp))

                Text(
                    text = stringResource(Res.string.if_we_found_an_account_email_has_been_sent,email),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = stringResource(Res.string.please_check_your_email_in_a_moment),
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




                if (accountsState.error?.isNotBlank() == true) {
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
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                        letterSpacing = 0.2.sp
                    )

                    Text(
                        modifier = Modifier.noRippleClickable {
                            if (!state.showTimer){
                                accountsViewModel.resendEmailOtp(ctx)
                                screenModel.startCountDown()
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



                Spacer(Modifier.height(68.dp))

                ButtonWithText(
                    text = stringResource(Res.string.verify),
                    bgColor = if (isEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
//                            if (otpValue != "123456") {
//                                accountsViewModel.updateErrorValue("Verification code is invalid")
//                            } else {
//                                onVerify(otpValue)

                                accountsViewModel.forgotPassword(
                                    data = ForgotPasswordData(
                                        page = 2,
                                        email = null,
                                        resetToken = resetToken,
                                        code = otpValue,
                                        password = null
                                    ),
                                    ctx = ctx
                                )
                          //  }
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