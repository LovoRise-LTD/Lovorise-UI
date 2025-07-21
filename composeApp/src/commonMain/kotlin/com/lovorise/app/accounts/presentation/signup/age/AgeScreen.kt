package com.lovorise.app.accounts.presentation.signup.age

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import coinui.composeapp.generated.resources.choose_your_dob
import coinui.composeapp.generated.resources.how_old_are_you
import coinui.composeapp.generated.resources.ic_age
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.months
import coinui.composeapp.generated.resources.never_too_early_to_count_down
import coinui.composeapp.generated.resources.next
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.restrictions.Over18OnlyScreen
import com.lovorise.app.accounts.presentation.signup.age.components.AgeConfirmationDialog
import com.lovorise.app.accounts.presentation.signup.age.components.DobInputField
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.gender.GenderScreen
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomLinearProgressIndicator
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


class AgeScreen : Screen{

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.setSignupPage(SignupFlowPages.AGE_SCREEN,ctx)
        }

        AgeScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.navigateToOnBoarding()
              //  accountsViewModel.resetSignupFlow()
            },
            accountsViewModel = accountsViewModel,
            navigateToGenderScreen = {
                navigator.push(GenderScreen())
            },
            navigateToAgeRequirementScreen = {
                navigator.push(Over18OnlyScreen())
            }
        )
    }
}




@OptIn(InternalVoyagerApi::class)
@Composable
fun AgeScreenContent(
    isDarkMode:Boolean,
    navigateToGenderScreen:()->Unit,
    goBack:()->Unit,
    navigateToAgeRequirementScreen:()->Unit,
    accountsViewModel: AccountsViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isEnabled by remember { mutableStateOf(false) }

    var dobText by remember { mutableStateOf("") }

    var validateAgeResult by remember { mutableStateOf(ValidateAgeResult()) }

    var showConfirmationDialog by remember { mutableStateOf(false) }

    val monthsName = stringArrayResource(Res.array.months)

    LaunchedEffect(key1 = dobText){
        if (dobText.length == 8){
            validateAgeResult = validateAge(dobText,monthsName) ?: ValidateAgeResult()
            isEnabled = validateAgeResult.formatted.isNotBlank()
        }else{
            isEnabled = false
        }
    }

    val accountsState by accountsViewModel.state.collectAsState()


    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            navigateToGenderScreen()
        }
    }

    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
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
                progress = 4f/16f,
                isDarkMode = isDarkMode
            )


            Spacer(Modifier.height(40.dp))

            Icon(
                imageVector = vectorResource(Res.drawable.ic_age),
                contentDescription = null,
                modifier = Modifier.width(21.33.dp).height(26.67.dp),
                tint = if (isDarkMode) Color.White else Color(0xff344054)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(Res.string.how_old_are_you),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = Color(if(isDarkMode) 0xffFFFFFF else 0xff344054)
            )

            Spacer(Modifier.height(40.dp))

            DobInputField(
                onDobModified = { newDobText, result ->
                    if (result) {
                        dobText = newDobText
                    }
                },
                dobText = dobText,
                isDarkMode = isDarkMode,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                text = stringResource(Res.string.never_too_early_to_count_down),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Color(if(isDarkMode) 0xffEAECF0 else 0xff344054),
                letterSpacing = 0.2.sp
            )




            Spacer(Modifier.height(50.dp))



            ButtonWithText(
                text = stringResource(Res.string.next),
                bgColor = if (isEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (isEnabled) {
                        showConfirmationDialog = true
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

    if (accountsState.showExitConfirmationDialog){
        SignupConfirmExitDialog(
            onCancel = accountsViewModel::hideExitConfirmationDialog,
            onConfirm = {
                accountsViewModel.hideExitConfirmationDialog()
                goBack()
            },
            isDarkMode = isDarkMode,
        )
    }

    if (accountsState.isLoading){
        CircularLoader()
    }



    if (showConfirmationDialog){
        AgeConfirmationDialog(
            onConfirm = {
                showConfirmationDialog = false
                if (validateAgeResult.age < 18){
                    accountsViewModel.addAge(validateAgeResult.age, birthDate = validateAgeResult.yyyyMMDD)
                    keyboardController?.hide()
                    navigateToAgeRequirementScreen()
                }else {
                    keyboardController?.hide()
                    accountsViewModel.addAge(validateAgeResult.age, birthDate = validateAgeResult.yyyyMMDD)
                    //navigateToGenderScreen("${validateAgeResult.year}-${validateAgeResult.month}-${validateAgeResult.day}")
                }
            },
            onCancel = {
                showConfirmationDialog = false
            },
            formattedDOB = validateAgeResult.formatted,
            age = validateAgeResult.age,
            isDarkMode = isDarkMode
        )
    }




}