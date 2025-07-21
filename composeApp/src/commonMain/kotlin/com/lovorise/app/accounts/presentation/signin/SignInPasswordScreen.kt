package com.lovorise.app.accounts.presentation.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.enter_valid_email_or_password
import coinui.composeapp.generated.resources.forgot_password
import coinui.composeapp.generated.resources.ic_chev_left
import coinui.composeapp.generated.resources.ic_eye
import coinui.composeapp.generated.resources.ic_eye_hide
import coinui.composeapp.generated.resources.ic_logo_with_red_bg
import coinui.composeapp.generated.resources.lovorise
import coinui.composeapp.generated.resources.password
import coinui.composeapp.generated.resources.sign_in
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.forgot_password.ResetScreen
import com.lovorise.app.accounts.presentation.onboarding.OnboardingScreen
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.EmailScreen
import com.lovorise.app.accounts.presentation.signup.password.CreatePasswordScreen
import com.lovorise.app.accounts.presentation.utils.isEmailValid
import com.lovorise.app.accounts.presentation.utils.validatePassword
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomTextField
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import io.ktor.util.reflect.instanceOf
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class SignInPasswordScreen(private val email:String) : Screen{

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()

        val context = LocalPlatformContext.current

        LaunchedEffect(true){
            accountsViewModel.resetSuccessState()
//            accountsViewModel.loadLastLoginType(context)
        }

        val accountsState by accountsViewModel.state.collectAsStateWithLifecycle()

        BoxWithConstraints {
            SignInPasswordScreenContent(
                isDarkMode = isDarkMode,
                email = email,
                screenHeight = maxHeight.value.toInt(),
                navigateToSignup = {
                    navigator.push(EmailScreen())
                },
                navigateToReset = {
                    navigator.push(ResetScreen(startingScreen = this@SignInPasswordScreen))
                },
                navigateToHome = {
                    tabsScreenModel.updateTab(TabsScreenModel.BottomTab.SWIPE)
                    navigator.push(HomeScreen())
                },
//                showAccountDeletedDialog = !accountsState.deletedAccountName.isNullOrBlank(),
                accountsViewModel = accountsViewModel,
                onBack = {
                 //   accountsViewModel.reloadState()
                    navigator.popAll()
                    navigator.push(OnboardingScreen())
                    navigator.push(OnboardingScreen())
                    navigator.pop()
                },
                navigateToLastLeftScreen = {
                    val screen = accountsViewModel.getSignUpFlowScreen(context)

                    if (screen.instanceOf(OnboardingScreen::class) || screen.instanceOf(EmailScreen::class)){
                        navigator.push(CreatePasswordScreen())
                    }else {
                        navigator.push(screen)
//                        val currentStoredEmail = accountsViewModel.getCurrentEmail()
//                        if (currentStoredEmail != null && currentStoredEmail == accountsState.currentEmail){
//                            navigator.push(screen)
//                        }else{
//                            navigator.push(CreatePasswordScreen())
//                        }
                    }
                },
              //  name = accountsState.deletedAccountName
                accountsState = accountsState
            )
        }
    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun SignInPasswordScreenContent(isDarkMode:Boolean,email: String,screenHeight:Int,navigateToSignup:()->Unit,navigateToReset:()->Unit,navigateToHome:()->Unit,accountsViewModel: AccountsViewModel,onBack:()->Unit,navigateToLastLeftScreen:()->Unit,accountsState: AccountsApiCallState) {

   // var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
   // var error by remember { mutableStateOf("") }

//    var showAccountDeletedMessage by remember { mutableStateOf(showAccountDeletedDialog) }


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    var isPassVisible by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(false) }

    val  validEmailPassText = stringResource(Res.string.enter_valid_email_or_password)

    val hasSmallHeight = screenHeight < 720

  //  var isNormalLogin by remember { mutableStateOf() }
    val context = LocalPlatformContext.current

    LaunchedEffect(true){
        println("the name is ${accountsState.deletedAccountName}")
//        accountsViewModel.reloadState()
    }

    LaunchedEffect(accountsState.deletedAccountName){
        println("the name changed to ${accountsState.deletedAccountName}")
       // accountsViewModel.reloadState()
    }

    LaunchedEffect(key1 = email, key2 = password){
        if(email.isNotBlank() && password.isNotBlank()){
            accountsViewModel.updateErrorValue("")
            isEnabled = true
        }else{
            isEnabled = false
        }
    }

 //   val accountsState by accountsViewModel.state.collectAsState()


    LaunchedEffect(accountsState.success){
        if (accountsState.oAuthLoginResponse == null) {
            if (accountsState.success) {
                navigateToHome()
            }
        }else{
            if (accountsState.oAuthLoginResponse.isRegistrationCompleted){
                navigateToHome()
            }else{
                navigateToLastLeftScreen()
            }
        }
    }

    BackHandler(true){
      //  accountsViewModel.reloadState()
        onBack()
    }



    Column(
        modifier = Modifier.noRippleClickable {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    ) {

        Spacer(
            modifier = Modifier
                .background(if(isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()


        Column(
            modifier = Modifier
                .background(if(isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(7.dp))
            Row{
                Box(Modifier.size(24.dp).noRippleClickable(onBack), contentAlignment = Alignment.Center){
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_chev_left),
                        contentDescription = null,
                        tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                    )
                }
                Spacer(Modifier.weight(1f))
            }


            Spacer(Modifier.height(80.dp))

            Image(
                imageVector = vectorResource(Res.drawable.ic_logo_with_red_bg),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.lovorise),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 36.sp,
                textAlign = TextAlign.Center,
                color = if(isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(40.dp))

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

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Start).noRippleClickable(navigateToReset),
                text = stringResource(Res.string.forgot_password),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = PRIMARY,
                letterSpacing = 0.2.sp
            )

            if (!accountsState.error.isNullOrBlank()) {
                Spacer(Modifier.height(if (hasSmallHeight) 5.dp else 8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = accountsState.error,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color(0xffD92D20),
                )
            }

            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(if (isEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT, shape = RoundedCornerShape(16.dp))
                    .noRippleClickable {
                        if (isEnabled) {
                            if (!isEmailValid(email) && validatePassword(password) != null){
                                accountsViewModel.updateErrorValue(validEmailPassText)
                            }else{
                                accountsViewModel.signin(email,password,context)
                            }

                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.sign_in),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    color = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT
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





