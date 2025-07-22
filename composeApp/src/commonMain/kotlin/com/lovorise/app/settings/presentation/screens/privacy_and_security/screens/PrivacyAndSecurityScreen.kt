package com.lovorise.app.settings.presentation.screens.privacy_and_security.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.privacy
import coinui.composeapp.generated.resources.privacy_and_security
import coinui.composeapp.generated.resources.security
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.ProfileScreensState
import com.lovorise.app.profile.presentation.components.GetVerifiedBadgeDialog
import com.lovorise.app.profile.presentation.components.VerificationSuccessDialog
import com.lovorise.app.profile.presentation.components.VerificationUnderReviewDialog
import com.lovorise.app.profile.presentation.verification.ChooseIDVerificationOptionScreen
import com.lovorise.app.profile.presentation.verification.IDProfileVerificationScreenState
import com.lovorise.app.profile.presentation.verification.ImageVerificationScreen
import com.lovorise.app.settings.presentation.screens.BlockedUsersScreen
import com.lovorise.app.settings.presentation.screens.ManageDeviceScreen
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyAndSecurityScreenModel
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyAndSecurityState
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyOptions
import com.lovorise.app.settings.presentation.screens.privacy_and_security.components.SecurityItemsSection
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource


class PrivacyAndSecurityScreen : Screen {


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = navigator.koinNavigatorScreenModel<PrivacyAndSecurityScreenModel>()
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()
        val state by screenModel.state.collectAsState()
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profileScreenState by profileScreenModel.state.collectAsState()
        PrivacyAndSecurityScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            state = state,
            navigateTo = {
                navigator.push(it)
            },
            accountsState = accountsState,
            profileScreensState = profileScreenState,
            profileScreenModel = profileScreenModel
        )
    }
}

@Composable
fun PrivacyAndSecurityScreenContent(isDarkMode: Boolean, goBack:()-> Unit,state: PrivacyAndSecurityState,navigateTo:(Screen)-> Unit,accountsState:AccountsApiCallState,profileScreensState: ProfileScreensState,profileScreenModel: ProfileScreenModel){


    Column(modifier = Modifier.background(if (isDarkMode) BASE_DARK else Color.White)) {


        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        HeaderWithTitleAndBack(title = stringResource(Res.string.privacy_and_security), onBack = goBack, isDarkMode = isDarkMode, addShadow = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ){

            Column(Modifier.padding(horizontal = 16.dp)) {


                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.security),
                    color = Color(0xff101828),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(16.dp))

                SecurityItemsSection(
                    onProfileVerification = {
                        if (accountsState.user?.isVerified != true){
                            if (profileScreensState.isVerificationUnderReview){
                                profileScreenModel.updateVerificationUnderReviewDialogState(true)
                            }else{
                                profileScreenModel.updateGetVerifyBadgeDialogState(true)
                            }
                        }else{
                            profileScreenModel.updateVerificationSuccessDialog(true)
                        }
                    },
                    onDevices = {
                        navigateTo(ManageDeviceScreen())
                    },
                    onBlockedPeople = {
                        navigateTo(BlockedUsersScreen())
                    }
                )
            }
            PrivacySecurityDivider(isDarkMode)

            Column(Modifier.padding(horizontal = 16.dp)){

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.privacy),
                    color = Color(0xff101828),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(16.dp))

                PrivacyItem(
                    title = "Photos & Videos",
                    value = state.photosAndVideos.privacyOption.toActualValue(),
                    onClick = {
                        navigateTo(WhoCanSeeMyProfileScreen())
                    }
                )
                Spacer(Modifier.height(8.dp))
                CustomDivider()
                Spacer(Modifier.height(16.dp))
                PrivacyItem(
                    title = "Story",
                    value = state.story.privacyOption.toActualValue(),
                    onClick = {
                        navigateTo(WhoCanSeeMyStoryScreen())
                    }
                )
                Spacer(Modifier.height(8.dp))
                CustomDivider()
                Spacer(Modifier.height(16.dp))

                PrivacyItem(
                    title = "Share profile",
                    value = state.shareProfile.privacyOption.toActualValue(),
                    onClick = {
                        navigateTo(WhoCanShareMyProfileScreen())
                    }
                )
                Spacer(Modifier.height(8.dp))
                CustomDivider()
                Spacer(Modifier.height(16.dp))

                PrivacyItem(
                    title = "Last seen & online",
                    value = state.lastSeenAndOnline.privacyOption.toActualValue(),
                    onClick = {
                        navigateTo(WhoCanShareMyOnlineStatusScreen())
                    }
                )
                Spacer(Modifier.height(8.dp))
                CustomDivider()
                Spacer(Modifier.height(16.dp))

                PrivacyItem(
                    title = "Read receipt",
                    value = state.readReceipt.privacyOption.toActualValue(),
                    onClick = {
                        navigateTo(ReadReceiptScreen())
                    }
                )
                Spacer(Modifier.height(8.dp))
                CustomDivider()
                Spacer(Modifier.height(16.dp))

                PrivacyItem(
                    title = "Location",
                    value = state.location.privacyOption.toActualValue(),
                    onClick = {
                        navigateTo(WhoCanSeeMyLocationScreen())
                    }
                )
                Spacer(Modifier.height(16.dp))

            }

            Box(Modifier.fillMaxSize().weight(1f).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)))


        }


        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9))
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )


    }

    if (profileScreensState.showGetVerifyBadgeDialog){
        GetVerifiedBadgeDialog(
            onCancel = { profileScreenModel.updateGetVerifyBadgeDialogState(false) },
            onIDPhotoVerification = { profileScreenModel.updateGetVerifyBadgeDialogState(false); navigateTo(
                ChooseIDVerificationOptionScreen()) },
            onPhotoVerification = { profileScreenModel.updateGetVerifyBadgeDialogState(false); navigateTo(ImageVerificationScreen()) },
            isDarkMode = isDarkMode
        )
    }

    if(profileScreensState.showVerificationSuccessDialog){
        VerificationSuccessDialog(
            onCancel = {
                profileScreenModel.updateVerificationSuccessDialog(false)
            },
            isDarkMode = isDarkMode
        )
    }

    if (profileScreensState.showVerificationUnderReviewDialog){
        VerificationUnderReviewDialog(
            onCancel = { profileScreenModel.updateVerificationUnderReviewDialogState(false) },
            isDarkMode = isDarkMode
        )
    }








}

@Composable
fun PrivacyItem(title: String,value: String,onClick:()-> Unit){

    Row(Modifier.fillMaxWidth().height(24.dp).noRippleClickable(onClick), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = title,
            fontFamily = PoppinsFontFamily(),
            color = Color(0xff344054),
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp
        )
        Text(
            text = value,
            fontFamily = PoppinsFontFamily(),
            color = Color(0xff98A2B3),
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp
        )
    }


}

fun PrivacyOptions.toActualValue(): String{
    return when(this){
        PrivacyOptions.EVERYBODY -> "Everybody"
        PrivacyOptions.LIKES_AND_MATCHES -> "Likes & Matches"
        PrivacyOptions.MATCHES_ONLY -> "Matches only"
        PrivacyOptions.NOBODY -> "Nobody"
    }
}

@Composable
fun PrivacySecurityDivider(isDarkMode: Boolean){
    Box(Modifier.fillMaxWidth().height(8.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)))
}




