package com.lovorise.app.reels.presentation.prompt.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.prompt
import coinui.composeapp.generated.resources.select_a_prompt
import coinui.composeapp.generated.resources.text_prompts
import coinui.composeapp.generated.resources.video_prompts
import coinui.composeapp.generated.resources.voice_prompts
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.reels.presentation.prompt.components.PromptTabs
import com.lovorise.app.reels.presentation.prompt.components.PromptsListItem
import com.lovorise.app.reels.presentation.states.PromptScreenState
import com.lovorise.app.reels.presentation.viewModels.PromptScreenModel
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

class PromptScreen : Screen {

    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { PromptScreenModel() }
        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        PromptScreenContent(
            isDarkMode = isDarkMode,
            screenModel = screenModel,
            state = state,
            onBack = {
                navigator.pop()
            },
            navigateToAnswerTextPrompt = {
                navigator.push(AnswerTextPromptScreen(screenModel))
            },
            navigateToAnswerVideoPrompt = {

            },
            navigateToAnswerVoicePrompt = {

            }
        )
        
    }
}

@Composable
fun PromptScreenContent(isDarkMode:Boolean,onBack:()->Unit,state: PromptScreenState,screenModel: PromptScreenModel,navigateToAnswerTextPrompt:()->Unit,navigateToAnswerVoicePrompt:()->Unit,navigateToAnswerVideoPrompt:()->Unit) {

    val textPrompts = stringArrayResource(Res.array.text_prompts)
    val voicePrompts = stringArrayResource(Res.array.voice_prompts)
    val videoPrompts = stringArrayResource(Res.array.video_prompts)

    Column {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        HeaderWithTitleAndBack(title = stringResource(Res.string.prompt), onBack = onBack, addShadow = false)

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {

            PromptTabs(
                activeIndex = state.selectedTabIndex,
                onIndexChange = screenModel::updateSelectedTabIndex
            )

            LazyColumn(Modifier.fillMaxWidth()) {
                item {
                    Column {
                        Text(
                            text = stringResource(Res.string.select_a_prompt),
                            fontFamily = PoppinsFontFamily(),
                            letterSpacing = 0.2.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xff344054),
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
                items(if (state.selectedTabIndex == 0) textPrompts else if (state.selectedTabIndex == 1) voicePrompts else videoPrompts){
                    PromptsListItem(it, onClick = {
                        when (state.selectedTabIndex) {
                            0 -> {
                                screenModel.setPromptType(PromptScreenState.PromptType.TEXT)
                                screenModel.updatePromptTitle(it)
                                navigateToAnswerTextPrompt()
                            }
                            1 -> {
                                screenModel.setPromptType(PromptScreenState.PromptType.VOICE)
                                screenModel.updatePromptTitle(it)
                                navigateToAnswerVoicePrompt()
                            }
                            else -> {
                                screenModel.setPromptType(PromptScreenState.PromptType.VIDEO)
                                screenModel.updatePromptTitle(it)
                                navigateToAnswerVideoPrompt()
                            }
                        }
                    })
                }
                item {
                    Spacer(Modifier.height(84.dp))
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
}



