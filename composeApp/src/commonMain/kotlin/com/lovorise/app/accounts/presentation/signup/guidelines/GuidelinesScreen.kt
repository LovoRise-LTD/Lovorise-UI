package com.lovorise.app.accounts.presentation.signup.guidelines

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
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
import coinui.composeapp.generated.resources.be_yourself
import coinui.composeapp.generated.resources.community_guidelines
import coinui.composeapp.generated.resources.creating_meaningful_connections_together
import coinui.composeapp.generated.resources.engage_positively
import coinui.composeapp.generated.resources.i_agree
import coinui.composeapp.generated.resources.ic_logo_with_red_bg
import coinui.composeapp.generated.resources.keep_conversation_meaningful_and_constructive
import coinui.composeapp.generated.resources.our_team_is_here_to_help_when_need
import coinui.composeapp.generated.resources.protect_your_personal_information_and_report
import coinui.composeapp.generated.resources.respect_others
import coinui.composeapp.generated.resources.show_your_true_and_honest_self
import coinui.composeapp.generated.resources.stay_safe
import coinui.composeapp.generated.resources.to_maintain_this_safe_and_inclusive_space
import coinui.composeapp.generated.resources.treat_everyone_with_dignity
import coinui.composeapp.generated.resources.welcome_to_lovorise
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.location.LocationScreen
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class GuidelinesScreen : Screen{


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.setSignupPage(SignupFlowPages.GUIDELINES_SCREEN,ctx)
        }

        GuidelinesScreenContent(
            isDarkMode = isDarkMode,
            onNext = {
                navigator.push(LocationScreen())
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
fun GuidelinesScreenContent(
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

            Spacer(Modifier.height(63.7.dp))

            Image(
                imageVector = vectorResource(Res.drawable.ic_logo_with_red_bg),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.welcome_to_lovorise),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 36.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )


            Text(
                text = stringResource(Res.string.creating_meaningful_connections_together),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(26.dp))

            GuidelineItem(
                isDarkMode = isDarkMode,
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                title = stringResource(Res.string.be_yourself),
                body = stringResource(Res.string.show_your_true_and_honest_self)
            )
            Spacer(Modifier.height(6.dp))


            GuidelineItem(
                isDarkMode = isDarkMode,
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                title = stringResource(Res.string.respect_others),
                body = stringResource(Res.string.treat_everyone_with_dignity)
            )
            Spacer(Modifier.height(6.dp))


            GuidelineItem(
                isDarkMode = isDarkMode,
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                title = stringResource(Res.string.stay_safe),
                body = stringResource(Res.string.protect_your_personal_information_and_report)
            )
            Spacer(Modifier.height(6.dp))

            GuidelineItem(
                isDarkMode = isDarkMode,
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                title = stringResource(Res.string.engage_positively),
                body = stringResource(Res.string.keep_conversation_meaningful_and_constructive)
            )
            Spacer(Modifier.height(20.dp))

            val text = buildAnnotatedString {
                append(stringResource(Res.string.to_maintain_this_safe_and_inclusive_space))
                pushStringAnnotation(tag = "guidelines", annotation = "guidelines")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, textDecoration = TextDecoration.Underline)){
                    append(" ${stringResource(Res.string.community_guidelines).lowercase()}. ")
                }
                pop()
                append(stringResource(Res.string.our_team_is_here_to_help_when_need))
            }

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Start).pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        textLayoutResult?.let { textLayoutResult ->
                            // Get the character position from the tap offset
                            val position = textLayoutResult.getOffsetForPosition(tapOffset)

                            // Check for annotations at the clicked position
                            val privacyAnnotation =
                                text.getStringAnnotations("guidelines", position, position)

                            // Handle based on annotation
                            if (privacyAnnotation.isNotEmpty()) {
                                openUrlInCustomTab(AppConstants.COMMUNITY_GUIDELINES_URL, context)
                            }
                        }
                    }
                },
                text = text,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Start,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                onTextLayout = { textLayoutResult = it },
            )

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(20.dp))

            ButtonWithText(
                text = stringResource(Res.string.i_agree),
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
fun GuidelineItem(modifier: Modifier,title:String,body:String,isDarkMode: Boolean) {
    Text(
        modifier = modifier,
        text = title,
        fontFamily = PoppinsFontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 21.sp,
        color = if (isDarkMode) Color.White else Color(0xff101828)
    )

    Spacer(Modifier.height(4.dp))

    Text(
        modifier = modifier,
        text = body,
        fontFamily = PoppinsFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 21.sp,
        color =if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
    )
    
}