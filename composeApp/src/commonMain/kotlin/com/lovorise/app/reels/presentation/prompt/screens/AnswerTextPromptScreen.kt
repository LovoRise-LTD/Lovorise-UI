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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.write_something_fun
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.onboarding_info.DescriptionTextField
import com.lovorise.app.reels.presentation.prompt.components.AnswerCancelPrompt
import com.lovorise.app.reels.presentation.prompt.components.PromptTitleCard
import com.lovorise.app.reels.presentation.states.PromptScreenState
import com.lovorise.app.reels.presentation.viewModels.PromptScreenModel
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource

class AnswerTextPromptScreen(
    private val promptScreenModel: PromptScreenModel
) : Screen {

    @Composable
    override fun Content() {

        val state by promptScreenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        AnswerTextPromptScreenContent(
            isDarkMode = isDarkMode,
            screenModel = promptScreenModel,
            onBack = {
                navigator.pop()
            },
            navigateToSavePrompt = {
                navigator.push(SavePromptScreen(promptScreenModel))
            },
            state = state
        )
    }
}

@Composable
fun AnswerTextPromptScreenContent(isDarkMode:Boolean,screenModel: PromptScreenModel,state: PromptScreenState,onBack:()->Unit,navigateToSavePrompt:()->Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(Modifier.noRippleClickable { keyboardController?.hide() }) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        AnswerCancelPrompt(
            onBack = {
                screenModel.setPromptType(null)
                screenModel.updateTextPrompt("")
                screenModel.updateEditTitleState(false)
                onBack()
            },
            onSave = navigateToSavePrompt,
            isSaveButtonEnabled = state.textPrompt.isNotBlank() && !state.editTitleEnabled
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {

            Spacer(Modifier.height(13.dp))

            PromptTitleCard(
                text = state.promptTitle,
                onPromptChange = {
                    screenModel.updatePromptTitle(it)
                    screenModel.updateEditTitleState(false)
                },
                isEditModeEnabled = state.editTitleEnabled,
                enableEditMode = {
                    screenModel.updateEditTitleState(true)
                }
            )

            Spacer(Modifier.height(16.dp))

            DescriptionTextField(
                value = state.textPrompt,
                onValueChange = {
                    if (it.length <= 150) {
                        screenModel.updateTextPrompt(it)
                    }
                },
                label = stringResource(Res.string.write_something_fun),
                height = 100.dp,
                cornerRadiusPercent = 8,
                borderColor = Color(0xff344054),
                cursorColor = Color(0xffF33358),
                textStyle = TextStyle(
                    letterSpacing = 0.2.sp,
                  //  lineHeight = 24.sp,
                    fontSize = 12.sp,
                    color = Color(0xff344054),
                    fontWeight = FontWeight.Normal,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${state.textPrompt.length}/150",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = Color(0xff475467),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp
            )





        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )
    }
    
}