package com.lovorise.app.settings.presentation.screens.change_email

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add
import coinui.composeapp.generated.resources.add_email
import coinui.composeapp.generated.resources.email
import coinui.composeapp.generated.resources.enter_new_email
import coinui.composeapp.generated.resources.please_enter_a_valid_email
import coinui.composeapp.generated.resources.this_email_will_be_used_for_login
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.utils.isEmailValid
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomTextField
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource

class AddEmailScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        AddEmailScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            navigateToVerifyNewEmail = {
                navigator.push(VerifyNewEmail(it))
            }
        )
    }
}

@Composable
fun AddEmailScreenContent(isDarkMode:Boolean,goBack:()->Unit,navigateToVerifyNewEmail:(String)->Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val enterValidEmailMsg = stringResource(Res.string.please_enter_a_valid_email)

    var email by remember { mutableStateOf("") }

    var isEnabled by remember { mutableStateOf(false) }

    var error by remember { mutableStateOf("") }



    LaunchedEffect(email){
        if(email.isNotBlank()){
            error = ""
            isEnabled = true
        }else{
            isEnabled = false
        }
    }

    Column(modifier = Modifier
        .noRippleClickable {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    ){

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
                .weight(1f),
            //  horizontalAlignment = Alignment.CenterHorizontally
        ) {


            HeaderWithTitleAndBack(title = stringResource(Res.string.add_email), onBack = goBack, isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = stringResource(Res.string.enter_new_email),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )


                Spacer(Modifier.height(2.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = stringResource(Res.string.this_email_will_be_used_for_login),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Start,
                    color = if(isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )


                Spacer(Modifier.height(16.dp))

                CustomTextField(
                    label = stringResource(Res.string.email),
                    value = email,
                    onChange = {email = it},
                    keyboardType = KeyboardType.Email,
                    isDarkMode = isDarkMode
                )



                if (error.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                        text = error,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = Color(0xffD92D20),
                    )
                }


                Spacer(Modifier.height(150.dp))

                ButtonWithText(
                    text = stringResource(Res.string.add),
                    bgColor = if (isEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
                            if (!isEmailValid(email)){
                                error = enterValidEmailMsg
                            }else {
                                keyboardController?.hide()
                                navigateToVerifyNewEmail(email)
                            }
                        }
                    }
                )

            }




        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )


    }
}