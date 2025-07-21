package com.lovorise.app.reels.presentation.prompt.screens

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import coinui.composeapp.generated.resources.done
import coinui.composeapp.generated.resources.ic_cross_white
import coinui.composeapp.generated.resources.ic_download
import coinui.composeapp.generated.resources.ic_reels_settings
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.reels.presentation.prompt.components.AnsweredTextPromptCard
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelSettingsBottomSheetContent
import com.lovorise.app.reels.presentation.states.PromptScreenState
import com.lovorise.app.reels.presentation.viewModels.PromptScreenModel
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class SavePromptScreen(private val promptScreenModel: PromptScreenModel) : Screen {

    @Composable
    override fun Content() {

        val promptScreenState by promptScreenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()


        SavePromptScreenContent(
            isDarkMode = isDarkMode,
            screenModel = promptScreenModel,
            state = promptScreenState,
            onBack = {
                navigator.pop()
            },
            navigateBackToPrompt = {
                navigator.popUntil {
                    it == PromptScreen::class
                }
            },
            profileScreenModel = profileScreenModel,
            navigateToSubscription = {
                navigator.push(PurchaseSubscriptionScreen(SubscriptionType.WEEKLY))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavePromptScreenContent(navigateToSubscription:()->Unit,profileScreenModel: ProfileScreenModel,isDarkMode:Boolean,onBack:()->Unit,screenModel: PromptScreenModel,state: PromptScreenState,navigateBackToPrompt:()->Unit) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val profileState by profileScreenModel.state.collectAsState()


    Column {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .weight(1f),
        ) {


            Row(Modifier.height(56.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(24.dp).noRippleClickable(onBack), contentAlignment = Alignment.Center){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_cross_white),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                Box(Modifier.size(24.dp), contentAlignment = Alignment.Center){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_download),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Box(Modifier.size(24.dp).noRippleClickable{screenModel.setPrivacyBottomSheetState(!state.showPrivacyBottomSheet)}, contentAlignment = Alignment.Center){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_reels_settings),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
            Spacer(Modifier.weight(1f))


            if(state.promptType == PromptScreenState.PromptType.TEXT){
                AnsweredTextPromptCard(
                    title = state.promptTitle,
                    text = state.textPrompt
                )
            }

            Spacer(Modifier.weight(1f))

            Box(
                Modifier.height(68.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
//                        Image(
//                            imageVector = vectorResource(Res.drawable.ic_crop),
//                            contentDescription = null,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//
//                    Spacer(Modifier.width(24.dp))
//
//                    Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
//                        Image(
//                            imageVector = vectorResource(Res.drawable.ic_brush),
//                            contentDescription = null,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//
//                    Spacer(Modifier.width(24.dp))
//
//                    Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
//                        Image(
//                            imageVector = vectorResource(Res.drawable.ic_microphone),
//                            contentDescription = null,
//                            modifier = Modifier.height(19.83.dp).width(15.47.dp)
//                        )
//                    }

                    Spacer(Modifier.weight(1f))


                    Box(
                        Modifier.wrapContentSize()
                            .background(Color(0xffF33358), RoundedCornerShape(8.dp))
                            .noRippleClickable(navigateBackToPrompt), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                            text = stringResource(Res.string.done),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.2.sp,
                            textAlign = TextAlign.Center
                        )
                    }


                }
            }

        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )


    }


    if (state.showPrivacyBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                screenModel.setPrivacyBottomSheetState(false)
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ReelSettingsBottomSheetContent(
                isDarkMode = isDarkMode,
                privacySettings = state.reelsPrivacySettings,
                onDone = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        screenModel.setPrivacyBottomSheetState(false)
                    }
                },
                onPrivacySettingsChanged = screenModel::setReelsPrivacy,
                isLocked = profileState.subscriptionType == SubscriptionType.FREE,
                onLockClick = navigateToSubscription
            )
        }
    }

}