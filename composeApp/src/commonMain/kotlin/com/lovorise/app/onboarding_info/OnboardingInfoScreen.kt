package com.lovorise.app.onboarding_info

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.daily_login_rewards
import coinui.composeapp.generated.resources.describe_here
import coinui.composeapp.generated.resources.how_did_you_hear_about_us
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.onboarding
import coinui.composeapp.generated.resources.submit
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.DropShadow
import com.lovorise.app.components.RewardsOverlay
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class OnboardingInfoScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        OnboardingInfoScreenContent(
            onBack = {
                navigator.pop()
            },
            isDarkMode = isDarkMode,
            profileScreenModel = profileScreenModel
        )
    }
}

@Composable
fun OnboardingInfoScreenContent(
    onBack:()->Unit,
    isDarkMode:Boolean,
    profileScreenModel: ProfileScreenModel
) {

    var description by remember { mutableStateOf("") }
    var charCount by remember { mutableStateOf(0) }

    val context = LocalPlatformContext.current

    val profileScreensState by profileScreenModel.state.collectAsState()



    var isEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(description){
        isEnabled = description.length >= 2 && !profileScreensState.showRewardsOverlay
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .weight(1f)
                .noRippleClickable {
                    keyboardController?.hide()
                }
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier.fillMaxSize()) {

                    Box(
                        modifier = Modifier.fillMaxHeight().size(24.dp)
                            .noRippleClickable { onBack() },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            tint = if (isDarkMode) Color.White else Color(0xff101828),
                            modifier = Modifier.width(16.dp).height(12.dp),
                            imageVector = vectorResource(Res.drawable.ic_left),
                            contentDescription = "back"
                        )
                    }



                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(Res.string.onboarding),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) Color.White else Color(0xff101828)
                        )
                    }


                }


            }
            DropShadow()

            Column(
                modifier = Modifier.fillMaxSize()
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.how_did_you_hear_about_us),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                )

                Spacer(modifier = Modifier.height(16.dp))

                DescriptionTextField(
                    value = description,
                    onValueChange = {
                        val len = it.length
                        if (len <= 500) {
                            description = it
                            charCount = len
                        }
                    },
                    textStyle = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff101828)
                    ),
                    label = stringResource(Res.string.describe_here),
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    bgColor = if (isDarkMode) BASE_DARK else Color.White,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${charCount}/500",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(modifier = Modifier.height(43.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color(if (isEnabled) 0xffF33358 else 0xffEAECF0), RoundedCornerShape(40))
                        .noRippleClickable{
                            if (isEnabled) {
                                keyboardController?.hide()
                                profileScreenModel.collectOnboardingReward(context,description,onBack)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.submit),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color(if (isEnabled) 0xffffffff else 0xff98A2B3),
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp
                    )

                }
            }

        }


        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )

    }

    if (profileScreensState.isLoading){
        CircularLoader()
    }

    if (profileScreensState.showRewardsOverlay){
        RewardsOverlay(hearts = 50, message = stringResource(Res.string.daily_login_rewards), onClick = {
            profileScreenModel.updateShowRewardsOverlayState(false)
            onBack()
        }, totalDuration = 4000L)
    }





}