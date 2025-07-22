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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import coinui.composeapp.generated.resources.add_exceptions
import coinui.composeapp.generated.resources.add_person
import coinui.composeapp.generated.resources.done
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.never_share_with
import coinui.composeapp.generated.resources.who_can_see_my_online_status
import coinui.composeapp.generated.resources.who_can_see_my_profile
import coinui.composeapp.generated.resources.you_can_choose_who_can_see
import coinui.composeapp.generated.resources.you_can_restrict_who_can_see
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.components.TextWithBackground
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyAndSecurityScreenModel
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyAndSecurityState
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyOptions
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class WhoCanSeeMyProfileScreen : Screen {


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = navigator.koinNavigatorScreenModel<PrivacyAndSecurityScreenModel>()

        val state by screenModel.state.collectAsState()
        WhoCanSeeMyProfileScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            state = state,
            screenModel = screenModel,
            onAddPerson = {
                navigator.push(AddPersonScreen())
            }
        )
    }
}

@Composable
fun WhoCanSeeMyProfileScreenContent(isDarkMode: Boolean, goBack:()-> Unit,state: PrivacyAndSecurityState,screenModel: PrivacyAndSecurityScreenModel,onAddPerson:()-> Unit){


    Column(modifier = Modifier.background(if (isDarkMode) BASE_DARK else Color.White)) {


        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Row(Modifier.fillMaxWidth().height(40.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.weight(1f))
            Box(Modifier.size(24.dp).noRippleClickable(goBack), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_xmark),
                    contentDescription = null,
                    tint = Color(0xff101828)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ){

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(Res.string.who_can_see_my_profile),
                color = Color(0xff101828),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily(),
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(16.dp))


            PrivacyTextWithCheckbox(
                text = "Everybody",
                isChecked = state.photosAndVideos.privacyOption == PrivacyOptions.EVERYBODY,
                onClick = {
                    screenModel.updatePhotoPrivacy(state.photosAndVideos.copy(privacyOption = PrivacyOptions.EVERYBODY))
                }
            )
            Spacer(Modifier.height(8.dp))


            PrivacyTextWithCheckbox(
                text = "Likes & Matches",
                isChecked = state.photosAndVideos.privacyOption == PrivacyOptions.LIKES_AND_MATCHES,
                onClick = {
                    screenModel.updatePhotoPrivacy(state.photosAndVideos.copy(privacyOption = PrivacyOptions.LIKES_AND_MATCHES))
                }
            )
            Spacer(Modifier.height(8.dp))

            PrivacyTextWithCheckbox(
                text = "Matches only",
                isChecked = state.photosAndVideos.privacyOption == PrivacyOptions.MATCHES_ONLY,
                onClick = {
                    screenModel.updatePhotoPrivacy(state.photosAndVideos.copy(privacyOption = PrivacyOptions.MATCHES_ONLY))
                }
            )
            Spacer(Modifier.height(8.dp))

            PrivacyTextWithCheckbox(
                text = "Nobody",
                isChecked = state.photosAndVideos.privacyOption == PrivacyOptions.NOBODY,
                onClick = {
                    screenModel.updatePhotoPrivacy(state.photosAndVideos.copy(privacyOption = PrivacyOptions.NOBODY))
                }
            )
            Spacer(Modifier.height(9.dp))

            TextWithBackground(stringResource(Res.string.you_can_choose_who_can_see), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))

            Column(Modifier.padding(horizontal = 16.dp)) {


                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.add_exceptions),
                    color = Color(0xff101828),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth().height(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.never_share_with),
                        color = Color(0xff344054),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        lineHeight = 21.sp,
                        letterSpacing = 0.2.sp
                    )

                    Text(
                        modifier = Modifier.noRippleClickable(onAddPerson),
                        text = stringResource(Res.string.add_person),
                        color = PRIMARY,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        lineHeight = 21.sp,
                        letterSpacing = 0.2.sp
                    )

                }
                Spacer(Modifier.height(10.dp))
            }

            TextWithBackground(stringResource(Res.string.you_can_restrict_who_can_see), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))

            Spacer(Modifier.weight(1f))


            ButtonWithText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(Res.string.done),
                bgColor = Color(0xffF33358),
                textColor = Color.White,
                onClick = {
                    goBack()
                }
            )

            Spacer(Modifier.height(20.dp))

        }

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) Color.Black else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
    }
}






