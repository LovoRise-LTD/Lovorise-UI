package com.lovorise.app.settings.presentation.screens.delete_account

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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.delete
import coinui.composeapp.generated.resources.forgot_password
import coinui.composeapp.generated.resources.ic_eye
import coinui.composeapp.generated.resources.ic_eye_hide
import coinui.composeapp.generated.resources.password
import coinui.composeapp.generated.resources.to_delete_account_please_provide_your_account_login_password
import coinui.composeapp.generated.resources.verify
import coinui.composeapp.generated.resources.verify_password
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.forgot_password.ResetScreen
import com.lovorise.app.accounts.presentation.signin.SignInScreen
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomTextField
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.components.CustomDialogWithTextAndBodyAndActions
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource

class DeleteFlow3Screen :Screen{


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()

        val name by remember { mutableStateOf(accountsViewModel.state.value.user?.name) }
        //val verifyEmailScreenModel = rememberScreenModel { VerifyEmailScreenModel() }

        LaunchedEffect(true){
            accountsViewModel.resetSuccessState()
        }

        DeleteFlow3ScreenContent(
            isDarkMode = isDarkMode,
            goBack = {navigator.pop()},
            navigateToResetPassword = {navigator.push(ResetScreen("Reset",this))},
            navigateToSignInScreen = {
               // accountsViewModel.setDeletedAccountName(name)
                navigator.popAll()
                navigator.push(SignInScreen())
            },
            name = name,
            //verifyEmailScreenModel = verifyEmailScreenModel,
            accountsViewModel = accountsViewModel
        )
    }
}

@Composable
fun DeleteFlow3ScreenContent(isDarkMode:Boolean,goBack:()->Unit,navigateToSignInScreen:()->Unit,name:String?,accountsViewModel: AccountsViewModel,navigateToResetPassword:()->Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

//    val state by verifyEmailScreenModel.state.collectAsState()


    var showDialog by remember{ mutableStateOf(false) }


  //  var error by remember { mutableStateOf("") }

    val accountState by accountsViewModel.state.collectAsState()

    val ctx = LocalPlatformContext.current

    var password by remember { mutableStateOf("") }
    var isPassVisible by remember { mutableStateOf(false) }



//    LaunchedEffect(accountState.success){
//        if (accountState.success){
//            navigateToSignInScreen()
//        }
//    }


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
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = stringResource(Res.string.to_delete_account_please_provide_your_account_login_password),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )
                Spacer(Modifier.height(16.dp))


                CustomTextField(
                    label = stringResource(Res.string.password),
                    value = password,
                    onChange = {password = it},
                    keyboardType = KeyboardType.Unspecified,
                    visualTransformation = if (isPassVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '*'),
                    trailingRes = if (isPassVisible) Res.drawable.ic_eye else Res.drawable.ic_eye_hide,
                    onTrailingIconClick = {
                        isPassVisible = !isPassVisible
                    },
                    capitalization = KeyboardCapitalization.Words,
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(8.dp))



                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start).noRippleClickable(navigateToResetPassword),
                    text = stringResource(Res.string.forgot_password),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = Color(0xffF33358),
                    letterSpacing = 0.2.sp
                )

                if (!accountState.error.isNullOrBlank()) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = accountState.error!!,
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
                    bgColor = if (password.isNotBlank()) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (password.isNotBlank()) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (password.isNotBlank()) {
                            keyboardController?.hide()
                            showDialog = true
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

    if (showDialog){
        CustomDialogWithTextAndBodyAndActions(
            onCancel = {
                showDialog = false
            },
            actionText2 = stringResource(Res.string.delete),
            actionText1 = stringResource(Res.string.cancel),
            body = buildAnnotatedString { append("Are you sure you want to delete your account, $name?")},
            title = "Delete account",
            onAction1 = {
                showDialog = false
            },
            onAction2 = {
                showDialog = false
                if (password.isNotBlank() && !name.isNullOrBlank()) {
                    accountsViewModel.confirmPasswordForDelete(password = password,ctx, name = name, onSuccess = navigateToSignInScreen)
                }
            },
            isDarkMode = isDarkMode
        )
    }

    if (accountState.isLoading){
        CircularLoader()
    }


}