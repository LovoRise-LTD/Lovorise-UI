package com.lovorise.app.accounts.presentation.signup.privacy

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
import coinui.composeapp.generated.resources.accept
import coinui.composeapp.generated.resources.ic_logo_with_red_bg
import coinui.composeapp.generated.resources.your_privacy_comes_first
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.guidelines.GuidelinesScreen
import com.lovorise.app.accounts.presentation.signup.notification.NotificationPermissionScreen
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.showPermissionScreen
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class PrivacyScreen : Screen{


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.setSignupPage(SignupFlowPages.PRIVACY_SCREEN,ctx)
        }

        PrivacyScreenContent(
            isDarkMode = isDarkMode,
            onNext = {
                if (showPermissionScreen(ctx)){
                    navigator.push(NotificationPermissionScreen())
                }else{
                    navigator.push(GuidelinesScreen())
                }
            },
            goBack = {
                navigator.navigateToOnBoarding()
             //   accountsViewModel.resetSignupFlow()
            },
            accountsViewModel = accountsViewModel

        )
    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun PrivacyScreenContent(
    isDarkMode:Boolean,
    onNext:()->Unit,
    goBack:()->Unit,
    accountsViewModel: AccountsViewModel
) {

    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
        //goBack()
    }

    val context = LocalPlatformContext.current

    val accountsState by accountsViewModel.state.collectAsState()

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }


    Column{

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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(Modifier.height(75.dp))

            Image(
                imageVector = vectorResource(Res.drawable.ic_logo_with_red_bg),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.your_privacy_comes_first),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 36.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text = buildAnnotatedString {
                    append("We and our ")
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)){
                        append("2 partners ")
                    }
                    append("use tracking tools to store and process your data, including your IP address and profile information, to provide key app features, show you relevant ads, and improve our marketing. By tapping ")
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("\"Accept,\" ")
                    }
                    append("you consent to this in the ")
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Lovorise app.")
                    }
                },
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                //textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(19.dp))

            val annotatedText = buildAnnotatedString {
                append("You can manage your privacy preferences later at any time in the app settings. Learn more in our ")
                pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, textDecoration = TextDecoration.Underline)) {
                    append("Privacy Policy")
                }
                pop()

                append(" and ")

                pushStringAnnotation(tag = "COOKIE", annotation = "cookie")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, textDecoration = TextDecoration.Underline)) {
                    append("Cookie Policy.")
                }
                pop()
            }

            Text(
                text = annotatedText,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { offsetPosition ->
                        val layoutResult = textLayoutResult ?: return@detectTapGestures
                        val position = layoutResult.getOffsetForPosition(offsetPosition)
                        annotatedText.getStringAnnotations(position, position).firstOrNull()?.let {
                            when (it.tag) {
                                "PRIVACY" -> {
                                    openUrlInCustomTab(AppConstants.PRIVACY_POLICY_URL,context)
                                }
                                "COOKIE" -> {
                                    openUrlInCustomTab(AppConstants.PRIVACY_POLICY_URL,context)

                                }
                            }
                        }
                    }
                },
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
               // textAlign = TextAlign.Center,
                onTextLayout = { textLayoutResult = it },
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )
            Spacer(Modifier.height(15.dp))

            Text(
                text = "We and our partners use your data for the following purposes:",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
              //  textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(20.dp))



            PrivacyItem("Store and/or access information on a device",isDarkMode)
            Spacer(Modifier.height(6.dp))
            PrivacyItem("Personalized advertising and content, ad and content measurement, audience insights, and product development",isDarkMode)
            Spacer(Modifier.height(6.dp))
            PrivacyItem("Improve marketing and app experience",isDarkMode)


            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(62.dp))

            ButtonWithText(
                text = stringResource(Res.string.accept),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = {
                    onNext()
                }
            )

            Spacer(Modifier.height(16.dp))



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
            isDarkMode = isDarkMode
        )
    }




}

@Composable
fun PrivacyItem(text:String,isDarkMode: Boolean){
    Row(Modifier.fillMaxWidth().padding(start = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(Modifier.height(21.dp).width(3.dp), contentAlignment = Alignment.Center){
            Box(Modifier.size(3.dp).background(if (isDarkMode) DISABLED_LIGHT else Color(0xff344054), CircleShape))
        }

        Text(
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 21.sp,
//            textAlign = TextAlign.Start,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )
    }

}

