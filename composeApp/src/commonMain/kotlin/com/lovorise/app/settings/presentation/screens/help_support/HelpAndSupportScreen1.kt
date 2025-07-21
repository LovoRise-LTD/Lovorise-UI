package com.lovorise.app.settings.presentation.screens.help_support

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.a_guide_to_lovorise
import coinui.composeapp.generated.resources.account_and_payment
import coinui.composeapp.generated.resources.how_can_we_help_you
import coinui.composeapp.generated.resources.ic_chevron_right_light_color
import coinui.composeapp.generated.resources.learn_more
import coinui.composeapp.generated.resources.report_a_problem
import coinui.composeapp.generated.resources.reporting_and_blocking
import coinui.composeapp.generated.resources.safety_center
import coinui.composeapp.generated.resources.safety_tips_for_online_dating
import coinui.composeapp.generated.resources.security_and_privacy
import coinui.composeapp.generated.resources.stay_safe_with_lovorise
import coinui.composeapp.generated.resources.troubleshooting
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class HelpAndSupportScreen1 : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        HelpAndSupportScreen1Content(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            navigateToSupportScreen2 = {
                navigator.push(HelpAndSupportScreen2())
            }
        )
    }
}

@Composable
fun HelpAndSupportScreen1Content(isDarkMode:Boolean,goBack:()->Unit,navigateToSupportScreen2:()->Unit) {

    var query by remember{ mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.noRippleClickable {
        keyboardController?.hide()
        focusManager.clearFocus()
    }) {

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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderWithTitleAndBack(title = "", onBack = goBack, isDarkMode = isDarkMode)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(15.dp))

//                SearchTextField(
//                    label = stringResource(Res.string.ask_anything),
//                    onQueryChange = { query = it },
//                    query = query,
//                    roundedCornerPercent = 23
//                )
//                Spacer(Modifier.height(15.dp))

                Box(Modifier.height(44.dp).fillMaxWidth(), contentAlignment = Alignment.CenterStart){

                    Text(
                       // modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(Res.string.how_can_we_help_you),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 19.2.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828)
                    )

                }

                Spacer(Modifier.height(8.dp))

                HelpItems(stringResource(Res.string.a_guide_to_lovorise), onClick = {}, isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))
                HelpItems(stringResource(Res.string.troubleshooting), onClick = {}, isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))
                HelpItems(stringResource(Res.string.account_and_payment), onClick = {}, isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))
                HelpItems(stringResource(Res.string.report_a_problem), onClick = navigateToSupportScreen2, isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))
                LearnMoreButton(onClick = {})



                Box(Modifier.height(44.dp).fillMaxWidth(), contentAlignment = Alignment.CenterStart){

                    Text(
                        // modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(Res.string.safety_center),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 19.2.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828)
                    )

                }

                Spacer(Modifier.height(8.dp))

                HelpItems(stringResource(Res.string.stay_safe_with_lovorise), onClick = {}, isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))

                HelpItems(stringResource(Res.string.safety_tips_for_online_dating), onClick = {}, isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))

                HelpItems(stringResource(Res.string.reporting_and_blocking), onClick = {}, isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))


                HelpItems(stringResource(Res.string.security_and_privacy), onClick = {}, isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))
                LearnMoreButton(onClick = {})

                Spacer(Modifier.height(48.dp))

            }




        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )


    }
}

@Composable
fun LearnMoreButton(onClick:()->Unit) {

    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(34.dp), contentAlignment = Alignment.CenterStart) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .noRippleClickable(onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Text(
                text = stringResource(Res.string.learn_more),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                letterSpacing = 0.2.sp,
                color = Color(0xff2885FF)
            )

        }
    }

}



@Composable
fun HelpItems(title:String,onClick:()->Unit,isDarkMode: Boolean) {

    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(44.dp), contentAlignment = Alignment.CenterStart) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .noRippleClickable(onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = PoppinsFontFamily(),
                letterSpacing = 0.2.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                if (isDarkMode){
                    Icon(
                        tint = DISABLED_LIGHT,
                        modifier = Modifier.size(24.dp),
                        imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                        contentDescription = null
                    )
                }else{
                    Image(
                        modifier = Modifier.size(24.dp),
                        imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                        contentDescription = null
                    )
                }
            }

        }
    }

}

