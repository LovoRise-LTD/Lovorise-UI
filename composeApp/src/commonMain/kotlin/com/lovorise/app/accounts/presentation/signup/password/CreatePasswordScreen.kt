package com.lovorise.app.accounts.presentation.signup.password

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import coinui.composeapp.generated.resources.create_password
import coinui.composeapp.generated.resources.ic_eye
import coinui.composeapp.generated.resources.ic_eye_hide
import coinui.composeapp.generated.resources.ic_lock
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.one_letter_one_number
import coinui.composeapp.generated.resources.one_special_character
import coinui.composeapp.generated.resources.password
import coinui.composeapp.generated.resources.six_characters_max_twenty
import coinui.composeapp.generated.resources.your_password_must_have_at_least
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.name.NameScreen
import com.lovorise.app.accounts.presentation.utils.PasswordValidationResult
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.accounts.presentation.utils.validatePassword
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomLinearProgressIndicator
import com.lovorise.app.components.CustomTextField
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class CreatePasswordScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.setSignupPage(SignupFlowPages.CREATE_PASSWORD_SCREEN,ctx)
        }

        CreatePasswordScreenContent(
            isDarkMode = isDarkMode,
            navigateToNameScreen = {
                navigator.push(NameScreen())
            },
            goBack = {
                navigator.navigateToOnBoarding()
            //    accountsViewModel.resetSignupFlow()
            },
            accountsViewModel = accountsViewModel

        )
    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun CreatePasswordScreenContent(
    isDarkMode:Boolean,
    navigateToNameScreen: ()->Unit,
    goBack:()->Unit,
    accountsViewModel: AccountsViewModel
) {


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var password by remember { mutableStateOf("") }
    var isPassVisible by remember { mutableStateOf(false) }

    var isEnabled by remember { mutableStateOf(false) }

    var errors by remember { mutableStateOf(listOf(PasswordValidationResult.MAX_MIN_CHAR,PasswordValidationResult.ONE_SPECIAL_CHAR,PasswordValidationResult.ONE_LETTER_ONE_DIGIT)) }

    val accountsState by accountsViewModel.state.collectAsState()

    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
//        goBack()
    }

    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            navigateToNameScreen()
        }
    }


    LaunchedEffect(key1 = password){
        errors = validatePassword(password) ?: emptyList()
        if (errors.isEmpty()){
            isEnabled = true
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
                progress = 2f/16f,
                isDarkMode = isDarkMode
            )


            Spacer(Modifier.height(40.dp))

            Icon(
                imageVector = vectorResource(Res.drawable.ic_lock),
                contentDescription = null,
                modifier = Modifier.width(26.dp).height(29.dp),
                tint = if (isDarkMode) Color.White else Color(0xff344054)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(Res.string.create_password),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(40.dp))

            CustomTextField(
                label = stringResource(Res.string.password),
                value = password,
                onChange = {
                    if (it.length <= 20) {
                        password = it
                    }},
                keyboardType = KeyboardType.Unspecified,
                visualTransformation = if (isPassVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '*'),
                trailingRes = if (isPassVisible) Res.drawable.ic_eye else Res.drawable.ic_eye_hide,
                onTrailingIconClick = {
                    isPassVisible = !isPassVisible
                },
                capitalization = KeyboardCapitalization.Words,
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(16.dp))


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

            TextWithCheckBox(isChecked = errors.none { it == PasswordValidationResult.MAX_MIN_CHAR }, text = stringResource(Res.string.six_characters_max_twenty),isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            TextWithCheckBox(isChecked = errors.none { it == PasswordValidationResult.ONE_LETTER_ONE_DIGIT }, text = stringResource(Res.string.one_letter_one_number),isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            TextWithCheckBox(isChecked = errors.none { it == PasswordValidationResult.ONE_SPECIAL_CHAR }, text = stringResource(Res.string.one_special_character),isDarkMode = isDarkMode)

            Spacer(Modifier.height(24.dp))


            ButtonWithText(
                text = stringResource(Res.string.next),
                bgColor = if (isEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (isEnabled) {
                        keyboardController?.hide()
                        accountsViewModel.createPassword(password)
                    }
                }
            )


        }



        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
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

@Composable
fun TextWithCheckBox(
    isChecked:Boolean,
    text:String,
    isDarkMode:Boolean = false
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){

        Checkbox(
            checked = isChecked,
            colors = CheckboxDefaults.colors(
//                checkedColor = Color(0xffF33358),
//                checkmarkColor = Color.White,
                uncheckedColor = Color(0xff98A2B3),
                disabledCheckedColor = Color(0xffF33358),
//                  disabledIndeterminateColor = Color.White,
                disabledUncheckedColor = Color(0xff98A2B3),
                checkmarkColor = if (isDarkMode) BASE_DARK else Color.White
            ),
            onCheckedChange = {},
            modifier = Modifier.size(18.dp),
            enabled = false,

        )

        Text(
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
            letterSpacing = 0.2.sp
        )




    }


}