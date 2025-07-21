package com.lovorise.app.accounts.presentation.restrictions

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
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.community_guidelines
import coinui.composeapp.generated.resources.got_it
import coinui.composeapp.generated.resources.i_understand_it_wont_happen_again
import coinui.composeapp.generated.resources.ic_warning_yellow
import coinui.composeapp.generated.resources.warning
import coinui.composeapp.generated.resources.we_have_identified_inappropriate_activity_result_in_ban
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.closeApp
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import io.ktor.util.reflect.instanceOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class WarningBeforeAccountBanScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        WarningBeforeAccountBanScreenContent(
            isDarkMode = isDarkMode,
            navigateToHome = {
                val canPop = navigator.items.any { it.instanceOf(HomeScreen::class) }
                if (canPop) navigator.popUntil { it.instanceOf(HomeScreen::class) } else navigator.push(HomeScreen())
            }
        )
    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun WarningBeforeAccountBanScreenContent(
    isDarkMode:Boolean,
    navigateToHome:()->Unit
) {
    val context = LocalPlatformContext.current
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var isChecked by rememberSaveable { mutableStateOf(false) }

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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Spacer(Modifier.weight(1f))

            Image(
                imageVector = vectorResource(Res.drawable.ic_warning_yellow),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )

            Spacer(Modifier.height(16.dp))



            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.warning),
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
                append(stringResource(Res.string.we_have_identified_inappropriate_activity_result_in_ban) + " ")
                pushStringAnnotation(tag = "guidelines", annotation = "guidelines")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFF33358),
                    )
                ) {
                    append(stringResource(Res.string.community_guidelines).lowercase())
                }
                pop()
                append(".")
            }

            Text(
                modifier = Modifier.padding(9.dp).pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        textLayoutResult?.let { textLayoutResult ->
                            // Get the character position from the tap offset
                            val position = textLayoutResult.getOffsetForPosition(tapOffset)

                            // Check for annotations at the clicked position
                            val privacyAnnotation =
                                text.getStringAnnotations("guidelines", position, position)

                            // Handle based on annotation
                            if (privacyAnnotation.isNotEmpty()) {
                                openUrlInCustomTab(AppConstants.COMMUNITY_GUIDELINES_URL,context)
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




            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(11.dp),
                verticalAlignment = Alignment.Top
            ){
                Box(modifier = Modifier.size(24.dp).noRippleClickable { isChecked = !isChecked }, contentAlignment = Alignment.Center) {
                    Checkbox(
                        checked = isChecked,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xffD92D20),
                            checkmarkColor = Color.White,
                            uncheckedColor = Color(0xffD92D20),
//                        disabledCheckedColor = Color.White,
//                        disabledIndeterminateColor = Color.White,
//                        disabledUncheckedColor = Color.White
                        ),
                        onCheckedChange = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    text = stringResource(Res.string.i_understand_it_wont_happen_again),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = Color(0xffD92D20),
                    letterSpacing = 0.2.sp
                )

            }


            Spacer(Modifier.weight(1f))

            ButtonWithText(
                text = stringResource(Res.string.got_it),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = navigateToHome
            )


            Spacer(Modifier.height(40.dp))








        }



        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }



}