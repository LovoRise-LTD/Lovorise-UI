package com.lovorise.app.settings.presentation.screens.privacy_and_security.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_person
import coinui.composeapp.generated.resources.done
import coinui.composeapp.generated.resources.ic_empty_message
import coinui.composeapp.generated.resources.ic_empty_message_dark
import coinui.composeapp.generated.resources.keep_exploring
import coinui.composeapp.generated.resources.no_match_found_try_connecting_with_others
import coinui.composeapp.generated.resources.no_people_to_add_right_now
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyAndSecurityScreenModel
import com.lovorise.app.settings.presentation.screens.privacy_and_security.PrivacyAndSecurityState
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import io.ktor.util.reflect.instanceOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class AddPersonScreen : Screen {


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = navigator.koinNavigatorScreenModel<PrivacyAndSecurityScreenModel>()
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()

        val state by screenModel.state.collectAsState()
        AddPersonScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            state = state,
            screenModel = screenModel,
            onKeepExploring = {
                tabsScreenModel.updateTab(TabsScreenModel.BottomTab.SWIPE)
                navigator.popUntil {
                    it.instanceOf(HomeScreen::class)
                }
            }
        )
    }
}

@Composable
fun AddPersonScreenContent(isDarkMode: Boolean, goBack:()-> Unit,state: PrivacyAndSecurityState,screenModel: PrivacyAndSecurityScreenModel,onKeepExploring: () -> Unit){


    Column(modifier = Modifier.background(if (isDarkMode) BASE_DARK else Color.White)) {


        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        HeaderWithTitleAndBack(title = stringResource(Res.string.add_person), onBack = goBack, isDarkMode = isDarkMode, addShadow = false)


        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ){

            Spacer(Modifier.height(16.dp))

            EmptyProfiles(isDarkMode = isDarkMode, onKeepExploring = onKeepExploring)

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

@Composable
fun EmptyProfiles(isDarkMode: Boolean,onKeepExploring:()-> Unit){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                imageVector = vectorResource( if (isDarkMode) Res.drawable.ic_empty_message_dark else Res.drawable.ic_empty_message),
                contentDescription = null,
                modifier = Modifier.height(80.62.dp).width(69.29.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.no_people_to_add_right_now),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
                color = if(isDarkMode) Color.White else Color(0xff101828),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.no_match_found_try_connecting_with_others),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.2.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            ButtonWithText(
                modifier = Modifier.padding(horizontal = 57.dp),
                text = stringResource(Res.string.keep_exploring),
                bgColor = Color(0xffF33358),
                textColor = Color.White,
                onClick = {
                    onKeepExploring()
                }
            )

        }
    }

}






