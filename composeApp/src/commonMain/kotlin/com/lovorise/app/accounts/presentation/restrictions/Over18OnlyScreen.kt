package com.lovorise.app.accounts.presentation.restrictions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.eighteen_plus_info
import coinui.composeapp.generated.resources.get_verified
import coinui.composeapp.generated.resources.ic_18_plus
import coinui.composeapp.generated.resources.over_eighteen_only
import coinui.composeapp.generated.resources.read_our
import coinui.composeapp.generated.resources.terms_and_conditions
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.verification.ChooseIDVerificationOptionScreen
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class Over18OnlyScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        Over18OnlyScreenContent(
            isDarkMode = isDarkMode,
            onLeave = {
                navigator.popUntilRoot()
            },
            navigateToIDVerification = {
                navigator.push(ChooseIDVerificationOptionScreen())
            }
        )
    }
}


@Composable
fun Over18OnlyScreenContent(
    isDarkMode:Boolean,
    onLeave:()->Unit,
    navigateToIDVerification:()->Unit
) {
    //218 -> 150

    val context = LocalPlatformContext.current

    Column(
        modifier = Modifier
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


            Spacer(Modifier.weight(1.45f))


            Image(
                imageVector = vectorResource(Res.drawable.ic_18_plus),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.over_eighteen_only),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
//                letterSpacing = 0.2.sp,
//                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 14.dp),
                text = stringResource(Res.string.eighteen_plus_info),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )


            Spacer(Modifier.weight(1f))


//            Box(modifier = Modifier.fillMaxWidth().height(43.dp), contentAlignment = Alignment.Center){
//                Text(
//                    modifier = Modifier.noRippleClickable(navigateToIDVerification),
//                    text = stringResource(Res.string.over_eighteen_verify_with_id),
//                    fontFamily = PoppinsFontFamily(),
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 14.sp,
//                    lineHeight = 21.sp,
//                    color = Color(0xffF33358),
//                    letterSpacing = 0.2.sp,
//                    textAlign = TextAlign.Center
//                )
//            }



            Spacer(Modifier.height(16.dp))



            ButtonWithText(
                text = stringResource(Res.string.get_verified),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = {
                    navigateToIDVerification()
                }
            )


            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(38.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "${stringResource(Res.string.read_our)} ",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Text(
                    modifier = Modifier.noRippleClickable { openUrlInCustomTab(AppConstants.TERMS_AND_CONDITIONS_URL,context) },
                    text = stringResource(Res.string.terms_and_conditions),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

            }

            Spacer(Modifier.height(30.dp))

        }



        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }



}