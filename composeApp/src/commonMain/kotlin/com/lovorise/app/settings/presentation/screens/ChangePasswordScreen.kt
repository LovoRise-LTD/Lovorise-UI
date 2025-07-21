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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.change_password
import coinui.composeapp.generated.resources.current_password
import coinui.composeapp.generated.resources.enter_new_password
import coinui.composeapp.generated.resources.forgot_password
import coinui.composeapp.generated.resources.ic_eye
import coinui.composeapp.generated.resources.ic_eye_hide
import coinui.composeapp.generated.resources.logout
import coinui.composeapp.generated.resources.one_letter_one_number
import coinui.composeapp.generated.resources.one_special_character
import coinui.composeapp.generated.resources.password
import coinui.composeapp.generated.resources.reset
import coinui.composeapp.generated.resources.reset_password
import coinui.composeapp.generated.resources.six_characters_max_twenty
import coinui.composeapp.generated.resources.stay_logged_in
import coinui.composeapp.generated.resources.your_password_has_been_reset
import coinui.composeapp.generated.resources.your_password_must_have_at_least
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.ChangePasswordRequest
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.forgot_password.ResetScreen
import com.lovorise.app.accounts.presentation.signin.SignInScreen
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.password.TextWithCheckBox
import com.lovorise.app.accounts.presentation.utils.PasswordValidationResult
import com.lovorise.app.accounts.presentation.utils.validatePassword
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
import io.ktor.util.reflect.instanceOf
import org.jetbrains.compose.resources.stringResource

class ChangePasswordScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val resetTxt = stringResource(Res.string.reset)

        LaunchedEffect(true){
            accountsViewModel.resetSuccessState()
        }

        ChangePasswordScreenContent(
            isDarkMode = isDarkMode,
            goBack = {navigator.pop()},
            navigateToResetPassword = {navigator.push(ResetScreen(resetTxt,this))},
            navigateToSignIn = {navigator.push(SignInScreen())},
            navigateToManageAccount = {
                navigator.popUntil {
                    it.instanceOf(ManageAccountScreen::class)
                }
            },
            accountsViewModel = accountsViewModel
        )
    }
}


@Composable
fun ChangePasswordScreenContent(
    isDarkMode:Boolean,
    goBack:()->Unit,
    navigateToResetPassword:()->Unit,
    navigateToSignIn:()->Unit,
    navigateToManageAccount:()->Unit,
    accountsViewModel: AccountsViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isCurrentPassVisible by remember { mutableStateOf(false) }
    var isNewPassVisible by remember { mutableStateOf(false) }
    val context = LocalPlatformContext.current

    var isEnabled by remember { mutableStateOf(false) }

    var errors by remember { mutableStateOf(listOf(
        PasswordValidationResult.MAX_MIN_CHAR,
        PasswordValidationResult.ONE_SPECIAL_CHAR,
        PasswordValidationResult.ONE_LETTER_ONE_DIGIT)) }

    var showResetSuccessDialog by remember { mutableStateOf(false) }

    val accountsState by accountsViewModel.state.collectAsState()


    LaunchedEffect(key1 = newPassword){
        errors = validatePassword(newPassword) ?: emptyList()
        if (errors.isEmpty()){
            isEnabled = true
        }
    }

    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            showResetSuccessDialog = true
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


            HeaderWithTitleAndBack(title = stringResource(Res.string.change_password), onBack = goBack, isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                CustomTextField(
                    label = stringResource(Res.string.current_password),
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

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.enter_new_password),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(8.dp))

                CustomTextField(
                    label = stringResource(Res.string.password),
                    value = newPassword,
                    onChange = {
                        if (it.length <= 20) {
                            newPassword = it
                        }},
                    keyboardType = KeyboardType.Unspecified,
                    visualTransformation = if (isNewPassVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '*'),
                    trailingRes = if (isNewPassVisible) Res.drawable.ic_eye else Res.drawable.ic_eye_hide,
                    onTrailingIconClick = {
                        isNewPassVisible = !isNewPassVisible
                    },
                    capitalization = KeyboardCapitalization.Words,
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = stringResource(Res.string.your_password_must_have_at_least),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(16.dp))

                TextWithCheckBox(isChecked = errors.none { it == PasswordValidationResult.MAX_MIN_CHAR }, text = stringResource(Res.string.six_characters_max_twenty), isDarkMode = isDarkMode)

                Spacer(Modifier.height(16.dp))

                TextWithCheckBox(isChecked = errors.none { it == PasswordValidationResult.ONE_LETTER_ONE_DIGIT }, text = stringResource(Res.string.one_letter_one_number), isDarkMode = isDarkMode)

                Spacer(Modifier.height(16.dp))

                TextWithCheckBox(isChecked = errors.none { it == PasswordValidationResult.ONE_SPECIAL_CHAR }, text = stringResource(Res.string.one_special_character), isDarkMode = isDarkMode)

                Spacer(Modifier.height(60.dp))

                ButtonWithText(
                    text = stringResource(Res.string.reset),
                    bgColor = if (isEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
                            keyboardController?.hide()
                            accountsViewModel.changeOrSetPassword(
                                data = ChangePasswordRequest(password = currentPassword, newPassword = newPassword),
                                ctx = context
                            )
                            //showResetSuccessDialog = true
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

    if (showResetSuccessDialog){
        CustomDialogWithTextAndBodyAndActions(
            onCancel = {
                showResetSuccessDialog = false
            },
            actionText2 = stringResource(Res.string.stay_logged_in),
            actionText1 = stringResource(Res.string.logout),
            body = buildAnnotatedString { append(stringResource(Res.string.your_password_has_been_reset))},
            title = stringResource(Res.string.reset_password),
            onAction1 = {
                showResetSuccessDialog = false
                accountsViewModel.logout(context){
                    navigateToSignIn()
                    accountsViewModel.resetSuccessState()
                }
            },
            onAction2 = {
                showResetSuccessDialog = false
//                currentPassword = ""
//                newPassword = ""
//                errors = listOf(
//                    PasswordValidationResult.MAX_MIN_CHAR,
//                    PasswordValidationResult.ONE_SPECIAL_CHAR,
//                    PasswordValidationResult.ONE_LETTER_ONE_DIGIT)
//                isNewPassVisible = false
//                isCurrentPassVisible = false
//                isEnabled = false
                keyboardController?.hide()
                focusManager.clearFocus()
                navigateToManageAccount()
                accountsViewModel.resetSuccessState()
            },
            isDarkMode = isDarkMode
        )
    }



}