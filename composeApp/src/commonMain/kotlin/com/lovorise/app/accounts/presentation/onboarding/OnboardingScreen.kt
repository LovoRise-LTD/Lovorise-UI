package com.lovorise.app.accounts.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import coinui.composeapp.generated.resources.by_signing_up_you_agree_to_our
import coinui.composeapp.generated.resources.continue_with_apple
import coinui.composeapp.generated.resources.continue_with_gmail
import coinui.composeapp.generated.resources.ic_apple_logo_red
import coinui.composeapp.generated.resources.ic_google_logo
import coinui.composeapp.generated.resources.ic_logo_with_white_bg
import coinui.composeapp.generated.resources.learn_how_we_use_your_data
import coinui.composeapp.generated.resources.lovorise
import coinui.composeapp.generated.resources.privacy_policy
import coinui.composeapp.generated.resources.terms
import coinui.composeapp.generated.resources.use_email
import coinui.composeapp.generated.resources.you_are_back_online
import coinui.composeapp.generated.resources.you_are_using_lovorise_offline
import coinui.composeapp.generated.resources.you_last_signed_using_email
import coinui.composeapp.generated.resources.you_last_signed_with_apple
import coinui.composeapp.generated.resources.you_last_signed_with_google
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.LastLoginType
import com.lovorise.app.accounts.presentation.signin.SignInScreen
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.EmailScreen
import com.lovorise.app.accounts.presentation.signup.password.CreatePasswordScreen
import com.lovorise.app.closeApp
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.connectivity.ConnectivityViewModel
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject


class OnboardingScreen: Screen {



    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()

        val context =  LocalPlatformContext.current

        LaunchedEffect(true){
            accountsViewModel.resetSuccessState()
            accountsViewModel.clearAll(context)
            accountsViewModel.loadLastLoginType(context)
        }



        val accountsState by accountsViewModel.state.collectAsState()




        OnboardingScreenContent(
            isDarkMode = isDarkMode,
            navigateToSignIn = {
                navigator.popAll()
                navigator.push(SignInScreen())
            },
            navigateToSignUp = {
                navigator.popAll()
                navigator.push(EmailScreen())
            },
            accountsViewModel = accountsViewModel,
            navigateToLastLeftScreen = {
                val screen = accountsViewModel.getSignUpFlowScreen(context)

                if (screen.instanceOf(OnboardingScreen::class) || screen.instanceOf(EmailScreen::class)){
                    navigator.push(CreatePasswordScreen())
                }else {
                    navigator.push(screen)
//                    val currentStoredEmail = accountsViewModel.getCurrentEmail()
//                    if (currentStoredEmail != null && currentStoredEmail == accountsState.currentEmail){
//                        navigator.push(screen)
//                    }else{
//                        navigator.push(CreatePasswordScreen())
//                    }
                }
                // accountsViewModel.reloadState()
            },
            navigateToHomeScreen = {
                tabsScreenModel.updateTab(TabsScreenModel.BottomTab.SWIPE)
                navigator.push(HomeScreen())
            },
            onBack = {
                closeApp(context)
            },
            accountsState = accountsState
        )
    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun OnboardingScreenContent(
    accountsState:AccountsApiCallState,
    isDarkMode: Boolean,
    navigateToSignIn:()->Unit,
    navigateToSignUp:()->Unit,
    accountsViewModel: AccountsViewModel,
    navigateToHomeScreen:()->Unit,
    onBack:()->Unit,
    navigateToLastLeftScreen:()->Unit
) {

    var isOnline by rememberSaveable{ mutableStateOf<Boolean?>(null) }



    LaunchedEffect(true){
        accountsViewModel.reloadState()
    }

    BackHandler(true){
        onBack()
    }

    val context = LocalPlatformContext.current


    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }


    LaunchedEffect(accountsState.oAuthLoginResponse){
        if (accountsState.oAuthLoginResponse != null){
            if (accountsState.oAuthLoginResponse.isRegistrationCompleted){
                navigateToHomeScreen()
            }else {
                navigateToLastLeftScreen()
            }
        }
    }


    Column(Modifier.background(brush = Brush.linearGradient(listOf(Color(0xffF3335D),Color(0xffF33386))))) {

        Spacer(
            modifier = Modifier
                .then(if (isOnline != null){ Modifier.background(if (isOnline!!) Color(0xff1BDE83) else Color(0xff000000).copy(alpha = 0.46f)) } else Modifier)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )


        Box(
            modifier = Modifier
                //   .background(Color.White)
                .fillMaxSize()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    //   .background(Color.White)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.weight(1.27f))

                Image(
                    imageVector = vectorResource(Res.drawable.ic_logo_with_white_bg),
                    contentDescription = null,
                    modifier = Modifier.size(52.dp)
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(Res.string.lovorise).uppercase(),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 48.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )


                Spacer(modifier = Modifier.weight(1f))


                if (accountsState.lastLoginType == LastLoginType.EMAIL || accountsState.lastLoginType == LastLoginType.EMAIL) {
                    Text(
                        text = stringResource(if (accountsState.lastLoginType == LastLoginType.EMAIL) Res.string.you_last_signed_using_email else if (accountsState.lastLoginType == LastLoginType.GOOGLE) Res.string.you_last_signed_with_google else Res.string.you_last_signed_with_apple),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.2.sp,
                        //lineHeight = 20.sp,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(44.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .noRippleClickable {
                            if (isAndroid()) accountsViewModel.signInWithGoogle(context) else accountsViewModel.signInWithApple(
                                context
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {


                        Image(
                            imageVector = vectorResource(if (isAndroid()) Res.drawable.ic_google_logo else Res.drawable.ic_apple_logo_red),
                            contentDescription = null,
                            modifier = Modifier.height(20.dp).width(20.29.dp)
                        )

                        Spacer(Modifier.width(8.dp))


                        Text(
                            text = if (isAndroid()) stringResource(Res.string.continue_with_gmail) else stringResource(
                                Res.string.continue_with_apple
                            ),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xffF33358)
                        )


                    }


                }
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(44.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .noRippleClickable { navigateToSignIn() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.use_email),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 21.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xffF33358)
                    )
                }

                Spacer(Modifier.height(24.dp))

                val annotatedString = buildAnnotatedString {
                    append("${stringResource(Res.string.by_signing_up_you_agree_to_our)} ")
                    pushStringAnnotation("terms", "terms")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(Res.string.terms))
                    }
                    pop()
                    append(". ${stringResource(Res.string.learn_how_we_use_your_data)} ")
                    pushStringAnnotation("privacy", "privacy")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(Res.string.privacy_policy))
                    }
                    pop()
                    append(".")
                }
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 1.dp)
                        .pointerInput(Unit) {
                            detectTapGestures { tapOffset ->
                                textLayoutResult?.let { textLayoutResult ->
                                    // Get the character position from the tap offset
                                    val position = textLayoutResult.getOffsetForPosition(tapOffset)

                                    // Check for annotations at the clicked position
                                    val termsAnnotation = annotatedString.getStringAnnotations(
                                        "terms",
                                        position,
                                        position
                                    )
                                    val privacyAnnotation = annotatedString.getStringAnnotations(
                                        "privacy",
                                        position,
                                        position
                                    )

                                    // Handle based on annotation
                                    if (termsAnnotation.isNotEmpty()) {
                                        openUrlInCustomTab(
                                            AppConstants.TERMS_AND_CONDITIONS_URL,
                                            context
                                        )
                                    } else if (privacyAnnotation.isNotEmpty()) {
                                        openUrlInCustomTab(AppConstants.PRIVACY_POLICY_URL, context)
                                    }
                                }
                            }
                        },
                    onTextLayout = { textLayoutResult = it }, // Cache the layout result
                    text = annotatedString,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(Modifier.height(16.dp))

//            Text(
//                modifier = Modifier.noRippleClickable { navigateToSignIn() },
//                text = stringResource(Res.string.sign_in),
//                fontFamily = PoppinsFontFamily(),
//                fontWeight = FontWeight.Medium,
//                fontSize = 14.sp,
//                letterSpacing = 0.2.sp,
//                lineHeight = 21.sp,
//                textAlign = TextAlign.Center,
//                color = Color.White
//            )


            }

            if (isOnline != null) {
                Box(Modifier.fillMaxSize().noRippleClickable { })
            }

            ConnectivityMessage { isOnline = it }
        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
              //  .background(Color(0xffF33386))
        )
    }

    if(accountsState.isLoading){
        CircularLoader()
    }





}

@Composable
fun ConnectivityMessage(isOnline:(Boolean?)->Unit) {
    val connectivityViewModel = koinInject<ConnectivityViewModel>()
    val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()

    var showOnlineMessage by rememberSaveable { mutableStateOf(false) }
    var wasOffline by rememberSaveable { mutableStateOf(false) } // Track previous state

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            isOnline(false)
            //updateColor(disconnectedBGColor)
            wasOffline = true // Set flag when offline
        } else if (wasOffline) {
            // Only show message if we were previously offline
            showOnlineMessage = true
            wasOffline = false // Reset flag
            isOnline(true)
            //updateColor(0xff1BDE83)
            delay(3000) // Show message for 3 seconds
            showOnlineMessage = false
            isOnline(null)
        }
    }

    AnimatedVisibility(
        visible = !isConnected || showOnlineMessage,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Box(Modifier.fillMaxWidth().height(26.dp).background(if (isConnected) Color(0xff1BDE83) else Color(0xff000000).copy(0.46f)), contentAlignment = Alignment.Center){
            Text(
                text = stringResource(if (isConnected) Res.string.you_are_back_online else Res.string.you_are_using_lovorise_offline),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                //  lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )
        }
    }

}





/*
    Spacer(
                modifier = Modifier
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .fillMaxWidth()
                    .background(Color.Red)
            )

            Spacer(
                modifier = Modifier
                    .windowInsetsTopHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(Color.Red)
            )
* */