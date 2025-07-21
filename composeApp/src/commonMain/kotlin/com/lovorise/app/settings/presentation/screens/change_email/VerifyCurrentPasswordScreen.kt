package com.lovorise.app.settings.presentation.screens.change_email

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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.forgot_password
import coinui.composeapp.generated.resources.ic_eye
import coinui.composeapp.generated.resources.ic_eye_hide
import coinui.composeapp.generated.resources.password
import coinui.composeapp.generated.resources.reset
import coinui.composeapp.generated.resources.to_change_email
import coinui.composeapp.generated.resources.verify
import coinui.composeapp.generated.resources.verify_password
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.ChangeEmailRequest
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.forgot_password.ResetScreen
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
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
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource

class VerifyCurrentPasswordScreen : Screen {


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val resetText = stringResource(Res.string.reset)

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()

        val accountsState by accountsViewModel.state.collectAsState()

        LaunchedEffect(true){
            accountsViewModel.updateErrorValue("")
        }

        val ctx = LocalPlatformContext.current

        LaunchedEffect(true){
            accountsViewModel.changeEmail(ChangeEmailRequest(1,null,null,null,null),ctx)
        }


        VerifyCurrentPasswordScreenContent(
            isDarkMode = isDarkMode,
            goBack = {navigator.pop()},
            navigateToResetPassword = {navigator.push(ResetScreen(resetText,this))},
            navigateToAddEmail = {navigator.push(AddEmailScreen())},
            accountsViewModel = accountsViewModel,
            accountsState = accountsState
        )
    }
}

@Composable
fun VerifyCurrentPasswordScreenContent(isDarkMode:Boolean,goBack:()->Unit,navigateToResetPassword:()->Unit,navigateToAddEmail:()->Unit,accountsState:AccountsApiCallState,accountsViewModel: AccountsViewModel) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var currentPassword by remember { mutableStateOf("") }
    var isCurrentPassVisible by remember { mutableStateOf(false) }

    var isEnabled by remember { mutableStateOf(false) }
    val ctx = LocalPlatformContext.current

    var canNavigate by remember { mutableStateOf(false) }


    LaunchedEffect(currentPassword){
        isEnabled = currentPassword.length >= 2
    }

   // val failed = stringResource(Res.string.)

    LaunchedEffect(accountsState.success){
        if (canNavigate) {
            if (accountsState.success) {
                navigateToAddEmail()
                accountsViewModel.resetSuccessState()
            } else {
              //  accountsViewModel.updateErrorValue("Incorrect password")
            }
        }else{
            accountsViewModel.resetSuccessState()
        }
    }

    Column(modifier = Modifier
        .noRippleClickable {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    ){

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
            //  horizontalAlignment = Alignment.CenterHorizontally
        ) {


            HeaderWithTitleAndBack(title = stringResource(Res.string.verify_password), onBack = goBack, isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = stringResource(Res.string.to_change_email),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(16.dp))

                CustomTextField(
                    label = stringResource(Res.string.password),
                    value = currentPassword,
                    onChange = {
                        if (it.length <= 20) {
                            currentPassword = it
                        }},
                    keyboardType = KeyboardType.Unspecified,
                    visualTransformation = if (isCurrentPassVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '*'),
                    trailingRes = if (isCurrentPassVisible) Res.drawable.ic_eye else Res.drawable.ic_eye_hide,
                    onTrailingIconClick = {
                        isCurrentPassVisible = !isCurrentPassVisible
                    },
                    capitalization = KeyboardCapitalization.Words,
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    modifier = Modifier.noRippleClickable(navigateToResetPassword),
                    text = stringResource(Res.string.forgot_password),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = Color(0xffF33358),
                    letterSpacing = 0.2.sp
                )

                if (!accountsState.error.isNullOrBlank()) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = accountsState.error,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = Color(0xffD92D20),
                        letterSpacing = 0.2.sp
                    )
                }


                Spacer(Modifier.height(68.dp))

                ButtonWithText(
                    text = stringResource(Res.string.verify),
                    bgColor = if (isEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
                            canNavigate = true
                            keyboardController?.hide()
                            accountsViewModel.changeEmail(ChangeEmailRequest(2,accountsState.changeEmailToken!!,currentPassword,null,null),ctx)
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