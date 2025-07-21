package com.lovorise.app.accounts.presentation.restrictions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import coinui.composeapp.generated.resources.get_a_photo_verification_badge_for_your_profile
import coinui.composeapp.generated.resources.get_back_completing_video_selfie
import coinui.composeapp.generated.resources.get_verified
import coinui.composeapp.generated.resources.ic_profile_verification_required
import coinui.composeapp.generated.resources.if_you_do_not_your_account_will_be_closed_after_two_years
import coinui.composeapp.generated.resources.profile_verification_required
import coinui.composeapp.generated.resources.prove_to_people_that_you_r_genuine
import coinui.composeapp.generated.resources.we_locked_your_profile_temporarily_due_to_recent_activities
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.closeApp
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.profile.presentation.verification.ImageVerificationScreen
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ProfileVerificationRequiredScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        ProfileVerificationRequiredScreenContent(
            isDarkMode = isDarkMode,
            navigateToVerifyProfile = {
                navigator.push(ImageVerificationScreen())
            }
        )
    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun ProfileVerificationRequiredScreenContent(
    isDarkMode:Boolean,
    navigateToVerifyProfile:()->Unit
) {

    val context = LocalPlatformContext.current

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
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Spacer(Modifier.weight(1f))

            Image(
                imageVector = vectorResource(Res.drawable.ic_profile_verification_required),
                contentDescription = null,
                modifier = Modifier.size(68.44.dp)
            )

            Spacer(Modifier.height(16.dp))



            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.profile_verification_required),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(24.dp))



            Text(
                modifier = Modifier.padding(horizontal = 24.5.dp),
                text = stringResource(Res.string.we_locked_your_profile_temporarily_due_to_recent_activities),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))


            GetProfileVerifiedOption(
                modifier = Modifier.padding(horizontal = 16.dp),
                index = 1,
                title = stringResource(Res.string.get_back_completing_video_selfie),
                body = stringResource(Res.string.if_you_do_not_your_account_will_be_closed_after_two_years),
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(28.dp))

            GetProfileVerifiedOption(
                modifier = Modifier.padding(horizontal = 16.dp),
                index = 2,
                title = stringResource(Res.string.prove_to_people_that_you_r_genuine),
                body = stringResource(Res.string.get_a_photo_verification_badge_for_your_profile),
                isDarkMode = isDarkMode
            )


            Spacer(Modifier.weight(1.46f))

            CustomDivider(isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            ButtonWithText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(Res.string.get_verified),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = navigateToVerifyProfile
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

@Composable
fun GetProfileVerifiedOption(modifier: Modifier=Modifier,index:Int,title:String,body:String,isDarkMode: Boolean) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ){


        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center){
            Box(Modifier.size(20.dp).background(brush = Brush.linearGradient(listOf(Color(0xffF3335D),Color(0xffF33386))), shape = CircleShape), contentAlignment = Alignment.Center){
                Text(
                    text = index.toString(),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.width(16.dp))


        Column {
            Text(
                text = title,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = if (isDarkMode) Color.White else Color(0xff101828),
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = body,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                letterSpacing = 0.2.sp
            )

        }


    }

}