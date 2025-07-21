package com.lovorise.app.accounts.presentation.restrictions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.closeApp
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.screens.help_support.HelpAndSupportScreen1
import com.lovorise.app.settings.presentation.screens.openUrlExternally
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class AccountBannedScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        AccountBannedScreenContent(
            isDarkMode = isDarkMode,
            navigateToContact = {
                navigator.push(HelpAndSupportScreen1())
            }
        )
    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun AccountBannedScreenContent(
    isDarkMode:Boolean,
    navigateToContact:()->Unit
) {
    //218 -> 150

    val context = LocalPlatformContext.current
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    BackHandler(true){
        closeApp(context)
    }

    Column(
        modifier = Modifier
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
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(Modifier.weight(1.08f))


            Image(
                imageVector = vectorResource(Res.drawable.ic_banned_account),
                contentDescription = null,
                modifier = Modifier.height(44.dp).width(35.5.dp)
            )

            Spacer(Modifier.height(25.88.dp))



            Text(
                text = stringResource(Res.string.your_account_has_been_banned_from_lovorise),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(24.dp))



            val text = buildAnnotatedString {
                append(stringResource(Res.string.we_want_everyone_feel_safe_and_welcome_chose_to_remove_you) + " ")
                pushStringAnnotation(tag = "terms", annotation = "terms")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFF33358),
                    )
                ) {
                    append(stringResource(Res.string.terms_and_conditions))
                }
                pop()
                append(".")
            }

            Text(
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        textLayoutResult?.let { textLayoutResult ->
                            // Get the character position from the tap offset
                            val position = textLayoutResult.getOffsetForPosition(tapOffset)

                            // Check for annotations at the clicked position
                            val privacyAnnotation =
                                text.getStringAnnotations("terms", position, position)

                            // Handle based on annotation
                            if (privacyAnnotation.isNotEmpty()) {
                                openUrlInCustomTab(AppConstants.TERMS_AND_CONDITIONS_URL,context)
                            }
                        }
                    }
                },
                text = text,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                color = Color(0xff667085),
                onTextLayout = { textLayoutResult = it },
               // textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth().height(24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(Res.string.does_not_sound_right) + " ",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color(0xff667085)
                )

                Text(
                    text = stringResource(Res.string.contact_us).lowercase(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color(0xffF33358),
                    modifier = Modifier.noRippleClickable(navigateToContact)
                )
            }


            Spacer(Modifier.weight(1f))



            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.please_note_if_subscribed_cancel_subscription_with_provider),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 18.sp,
                fontFamily = PoppinsFontFamily(),
                color = Color(0xff667085),
                textAlign = TextAlign.Center
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



}