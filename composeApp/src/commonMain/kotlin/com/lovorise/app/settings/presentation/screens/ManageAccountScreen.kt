package com.lovorise.app.settings.presentation.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
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
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.AccountSource
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.components.SettingsScreenDivider
import com.lovorise.app.settings.presentation.components.TextWithChevronRight
import com.lovorise.app.settings.presentation.screens.change_email.VerifyCurrentEmail
import com.lovorise.app.settings.presentation.screens.change_email.VerifyCurrentPasswordScreen
import com.lovorise.app.settings.presentation.screens.delete_account.DeleteFlow1Screen
import com.lovorise.app.settings.presentation.screens.delete_account.DeleteFlow1ScreenContent
import com.lovorise.app.settings.presentation.screens.delete_account.DeleteOrPauseAccountScreen
import com.lovorise.app.settings.presentation.screens.verify_recovery_email.AddRecoveryEmailScreen
import com.lovorise.app.settings.presentation.screens.verify_recovery_email.VerifyRecoveryEmail
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ManageAccountScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()

        ManageAccountScreenContent(
            isDarkMode = isDarkMode,
            changePassword = {
                navigator.push(ChangePasswordScreen())
            },
            changeEmail = {
                navigator.push(if (accountsState.user?.source == AccountSource.EMAIL) VerifyCurrentEmail() else VerifyCurrentPasswordScreen())
            },
            deleteAccount = {
                navigator.push(DeleteFlow1Screen(accountsViewModel))
            },
            goBack = {
                navigator.pop()
            },
            accountsViewModel = accountsViewModel,
            createPassword = {
                navigator.push(SetPasswordScreen())
            },
            onVerifyEmail = {
                if (accountsState.user?.isEmailVerified != true){
                    navigator.push(if (accountsState.user?.source == AccountSource.GOOGLE) VerifyRecoveryEmail(null) else AddRecoveryEmailScreen())
                }
            }
        )
    }
}


@Composable
fun ManageAccountScreenContent(isDarkMode:Boolean,goBack:()->Unit,changeEmail:()->Unit,changePassword:()->Unit,deleteAccount:()->Unit,accountsViewModel: AccountsViewModel,createPassword:()->Unit,onVerifyEmail:()->Unit) {


    val accountsState by accountsViewModel.state.collectAsState()

    Column(modifier = Modifier) {

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderWithTitleAndBack(title = stringResource(Res.string.manage_account), onBack = goBack, isDarkMode = isDarkMode)

            Column(
                modifier = Modifier
                    .fillMaxSize()


            ) {

                Box(Modifier.height(40.dp).fillMaxWidth().background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)), contentAlignment = Alignment.CenterStart){

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(Res.string.profile),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828)
                    )

                }

                Spacer(Modifier.height(16.dp))

                Box(Modifier.fillMaxWidth().height(28.dp).padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart){
                    TextWithChevronRight(
                        text = stringResource(if(accountsState.user?.isEmailVerified == true) Res.string.change_email else Res.string.email),
                        onClick = if (accountsState.user?.isEmailVerified == true) changeEmail else onVerifyEmail,
                        isDarkMode = isDarkMode
                    )
                }

                Spacer(Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(end = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = accountsState.user?.email ?: "",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                    )
                    Spacer(Modifier.weight(1f))
                    if (accountsState.user?.isEmailVerified != true){
                        Image(
                            imageVector = vectorResource(Res.drawable.ic_warning),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                    }

                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = stringResource(if (accountsState.user?.isEmailVerified == true) Res.string.verified else Res.string.verify),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        color = Color(if (accountsState.user?.isEmailVerified == true) 0xff12B76A else 0xff98A1AF)
                    )
                }

                Spacer(Modifier.height(16.dp))

                CustomDivider(isDarkMode = isDarkMode)
                Spacer(Modifier.height(16.dp))
                Box(
                    Modifier.fillMaxWidth().height(28.dp).padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    TextWithChevronRight(
                        text = stringResource(if (accountsState.isPasswordCreated == true) Res.string.change_password else Res.string.set_password),
                        onClick = if (accountsState.isPasswordCreated == true) changePassword else createPassword,
                        isDarkMode = isDarkMode
                    )
                }

                Spacer(Modifier.height(5.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = if (accountsState.isPasswordCreated == true) "**********" else stringResource(Res.string.add_password_to_your_account),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )


                Spacer(Modifier.height(16.dp))

                SettingsScreenDivider(isDarkMode = isDarkMode)

                Box(Modifier.fillMaxWidth().height(52.dp), contentAlignment = Alignment.Center){
                    Text(
                        modifier = Modifier.noRippleClickable(deleteAccount),
                        text = stringResource(Res.string.delete_account),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        color = Color(0xffF33358)
                    )
                }





                Box(Modifier.fillMaxSize().weight(1f).background( if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)))

            }




        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) CARD_BG_DARK else Color.White)
        )


    }

}