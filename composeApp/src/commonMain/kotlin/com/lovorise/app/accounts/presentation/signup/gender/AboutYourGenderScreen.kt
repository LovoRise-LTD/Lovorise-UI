package com.lovorise.app.accounts.presentation.signup.gender

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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.about_your_gender
import coinui.composeapp.generated.resources.describe_here
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.save_and_close
import coinui.composeapp.generated.resources.tell_something_missing
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.DropShadow
import com.lovorise.app.noRippleClickable
import com.lovorise.app.onboarding_info.DescriptionTextField
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class AboutYourGenderScreen : Screen{
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val viewModel = navigator.koinNavigatorScreenModel<GenderScreenModel>()

        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        AboutYourGenderScreenContent(
            onSave = {
                viewModel.saveGenderInfo()
                navigator.pop()
            },
            onBack = {
                navigator.pop()
            },
            isDarkMode = isDarkMode,
            viewModel = viewModel
        )

    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun AboutYourGenderScreenContent(
    onBack:()->Unit,
    onSave:(String) -> Unit,
    isDarkMode:Boolean,
    viewModel: GenderScreenModel
){


    val state = viewModel.state.collectAsStateWithLifecycle().value


    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(true){
        onBack()
    }

    Column {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()

        Column(
            modifier = Modifier.fillMaxSize()
                .background(if (isDarkMode) BASE_DARK else Color.White)
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
                            modifier = Modifier.width(16.dp).height(12.dp),
                            imageVector = vectorResource(Res.drawable.ic_left),
                            contentDescription = "back",
                            tint = if (isDarkMode) Color.White else Color.Black
                        )
                    }



                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(Res.string.about_your_gender),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) Color.White else Color.Black
                        )
                    }


                }


            }
            DropShadow()

            Column(
                modifier = Modifier.fillMaxSize()
                   // .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.tell_something_missing),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                DescriptionTextField(
                    value = state.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = stringResource(Res.string.describe_here),
                    height = 90.dp,
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    bgColor = if (isDarkMode) BASE_DARK else Color.White,
                    textStyle = LocalTextStyle.current.copy(color = if (isDarkMode) Color.White else Color.Black)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${state.description.length}/20",
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
                        .background(Color(0XFFF33358), RoundedCornerShape(40))
                        .noRippleClickable { onSave(state.description) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.save_and_close),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.White,
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
                .background(if (isDarkMode) Color.Black else Color.White)
        )

    }

}