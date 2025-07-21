package com.lovorise.app.accounts.presentation.signup.email

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.email
import coinui.composeapp.generated.resources.exit
import coinui.composeapp.generated.resources.exit_signup_confirmation_message
import coinui.composeapp.generated.resources.ic_email
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.please_enter_a_valid_email
import coinui.composeapp.generated.resources.provide_a_valid_email
import coinui.composeapp.generated.resources.receive_marketing_emails
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.utils.isEmailValid
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomLinearProgressIndicator
import com.lovorise.app.components.CustomTextField
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import io.ktor.util.reflect.instanceOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class EmailScreen : Screen{

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

//        LaunchedEffect(true){
//            accountsViewModel.setSignupPage(SignupFlowPages.EMAIL_SCREEN,ctx)
//        }


        EmailScreenContent(
            isDarkMode = isDarkMode,
            navigateToVerify = {
                val screen = accountsViewModel.getSignUpFlowScreen(ctx)
                println("the screen is $screen")
                if (screen.instanceOf(EmailScreen::class)){
                   // accountsViewModel.setSignupPage(SignupFlowPages.EMAIL_SCREEN,ctx)
                    navigator.push(VerifyEmailScreen(email = it))
                }else if (screen.instanceOf(VerifyEmailScreen::class)) {
                    navigator.push(VerifyEmailScreen(email = it))
                } else{
                    navigator.push(screen)
//                    val currentEmail = accountsViewModel.getCurrentEmail()
//                    if (currentEmail == it){
//                        navigator.push(screen)
//                    }else{
//                        navigator.push(VerifyEmailScreen(email = it))
//                    }
                }
                accountsViewModel.reloadState()
            },
            goBack = {
                navigator.navigateToOnBoarding()
               // accountsViewModel.resetSignupFlow()
                //closeApp(ctx)
            },
            accountsViewModel = accountsViewModel
        )

    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun EmailScreenContent(
    isDarkMode:Boolean,
    navigateToVerify:(String)->Unit,
    goBack:()->Unit,
    accountsViewModel: AccountsViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val errorMessage = stringResource(Res.string.please_enter_a_valid_email)

    var isEnabled by remember { mutableStateOf(false) }

    val accountsState by accountsViewModel.state.collectAsState()

    val context = LocalPlatformContext.current


    var isEmailMarketingChecked by remember { mutableStateOf(false) }

    BackHandler(true){
        goBack()
    }

    LaunchedEffect(key1 = email){
        if(email.isNotBlank()){
            error = ""
            isEnabled = true
        }else{
            isEnabled = false
        }
    }

    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            println("the success is called")
            navigateToVerify(email)
            println("the email from success is $email")
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
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(3.dp))

            Box(
                modifier = Modifier.size(24.dp).align(Alignment.Start).noRippleClickable(goBack),
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
                progress = 1f/16f,
                isDarkMode = isDarkMode
            )


            Spacer(Modifier.height(40.dp))

            Icon(
                imageVector = vectorResource(Res.drawable.ic_email),
                contentDescription = null,
                modifier = Modifier.width(30.dp).height(24.dp),
                tint = if (isDarkMode) Color.White else Color(0xff344054)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(Res.string.provide_a_valid_email),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff344054)
            )

            Spacer(Modifier.height(40.dp))

            CustomTextField(
                label = stringResource(Res.string.email),
                value = email,
                onChange = {email = it},
                keyboardType = KeyboardType.Email,
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(8.dp))

            if (error.isNotBlank()) {
                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = error,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = Color(0xffD92D20),
                )
                Spacer(Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 1.5.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.Top
            ){
                Box(modifier = Modifier.size(24.dp).noRippleClickable {
                    isEmailMarketingChecked = !isEmailMarketingChecked
                }, contentAlignment = Alignment.Center) {
                    Checkbox(
                        checked = isEmailMarketingChecked,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xffF33358),
                            checkmarkColor = if(isDarkMode) BASE_DARK else Color.White,
                            uncheckedColor = Color(0xff98A2B3),
//                        disabledCheckedColor = Color.White,
//                        disabledIndeterminateColor = Color.White,
//                        disabledUncheckedColor = Color.White
                        ),
                        onCheckedChange = null,
                        modifier = Modifier.size(18.dp),


                    )
                }

                Text(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    text = stringResource(Res.string.receive_marketing_emails),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = if(isDarkMode) Color.White else Color(0xff344054),
                    letterSpacing = 0.2.sp
                )

            }

            Spacer(Modifier.height(40.dp))

            ButtonWithText(
                text = stringResource(Res.string.next),
                bgColor = if (isEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (isEnabled) {
                        if (!isEmailValid(email)){
                            error = errorMessage
                        }else{
                              accountsViewModel.signup(email,context)
//                            keyboardController?.hide()
//                            navigateToVerify(email)
                        }
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

    if (accountsState.isLoading){
        CircularLoader()
    }

}


@Composable
fun SignupConfirmExitDialog(onCancel:()->Unit,onConfirm:()->Unit,isDarkMode: Boolean) {
    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp)
                .background(if(isDarkMode) BASE_DARK else Color.White, shape = RoundedCornerShape(24.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .padding(top = 29.dp, bottom = 22.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(Res.string.exit_signup_confirmation_message),
                    color = if(isDarkMode) Color.White else Color(0xff101828),
                    //textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp
                )




                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)

                ) {


                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = Color((0xffD0D5DD)),
                                shape = RoundedCornerShape(50)
                            )
                            .noRippleClickable(onCancel),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = if(isDarkMode) Color.White else Color(0xff101828)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                            .background(
                                color = Color((0xffF33358)),
                                shape = RoundedCornerShape(50)
                            )
                            .noRippleClickable(onConfirm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.exit),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }






                }


            }

        }



    }
    
}

@Composable
fun CircularLoader(center:Boolean = false) {
//    Dialog(
//        properties = DialogProperties(
//            dismissOnClickOutside = false,
//            dismissOnBackPress = false,
//            usePlatformDefaultWidth = false
//        ),
//        onDismissRequest = {}
//    ) {
        Column(Modifier.fillMaxSize().noRippleClickable {  }) {
            Box(
                Modifier.fillMaxWidth().background(Color.Transparent).fillMaxHeight(if (center) 1f else 0.6f),
                contentAlignment = Alignment.Center
            ) {
                // Infinite animation for rotating the loader
                val infiniteTransition = rememberInfiniteTransition()
                val angleOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1100, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )

//        CircularProgressIndicator(
//            color = Color(0xffA8A8A9),
//            modifier = Modifier.size(25.dp)
//        )

                // Drawing the circular loader
                Canvas(modifier = Modifier.size(30.dp)) {
                    // Static arc (background arc)
                    drawArc(
                        color = Color(0xffA8A8A9), // Static background color
                        startAngle = 0f,
                        sweepAngle = 360f, // Full circle for the background
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx())
                    )

                    // Moving arc (foreground)
                    drawArc(
                        color = Color(0xffFFFFFF), // Moving color
                        startAngle = angleOffset,
                        sweepAngle = 90f, // Adjust for how much of the arc moves
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
            }
            if (!center) {
                Spacer(Modifier.weight(0.4f))
            }
        }
//    }
}

@Composable
fun CircularLoader1() {
    Box(
        Modifier.fillMaxWidth().background(Color.Transparent).fillMaxHeight(0.6f),
        contentAlignment = Alignment.Center
    ) {
        // Infinite animation for rotating the loader
        val infiniteTransition = rememberInfiniteTransition()
        val angleOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1100, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

//        CircularProgressIndicator(
//            color = Color(0xffA8A8A9),
//            modifier = Modifier.size(25.dp)
//        )

        // Drawing the circular loader
        Canvas(modifier = Modifier.size(30.dp)) {
            // Static arc (background arc)
            drawArc(
                color = Color(0xffA8A8A9), // Static background color
                startAngle = 0f,
                sweepAngle = 360f, // Full circle for the background
                useCenter = false,
                style = Stroke(width = 4.dp.toPx())
            )

            // Moving arc (foreground)
            drawArc(
                color = Color(0xffFFFFFF), // Moving color
                startAngle = angleOffset,
                sweepAngle = 90f, // Adjust for how much of the arc moves
                useCenter = false,
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }

}