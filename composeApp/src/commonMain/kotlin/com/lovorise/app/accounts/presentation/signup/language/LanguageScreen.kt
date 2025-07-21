package com.lovorise.app.accounts.presentation.signup.language

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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.languages
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.no_result_for_language
import coinui.composeapp.generated.resources.search_languages
import coinui.composeapp.generated.resources.select_up_to_five
import coinui.composeapp.generated.resources.skip
import coinui.composeapp.generated.resources.what_languages_do_you_know
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.AccountsRepo
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.profile_upload.ProfileUploadScreen
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.CustomLinearProgressIndicator
import com.lovorise.app.components.SearchTextField
import com.lovorise.app.components.TextWithCheckbox
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject

class LanguageScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = rememberScreenModel { LanguageScreenModel() }
        val ctx = LocalPlatformContext.current

        val res = stringArrayResource(Res.array.languages)

        val accountsRepo = koinInject<AccountsRepo>()
        val accountsViewModel = rememberScreenModel { AccountsViewModel(accountsRepo) }

        LaunchedEffect(true){
            screenModel.reloadItems(emptyList())
            screenModel.loadLanguages(res)
            accountsViewModel.setSignupPage(SignupFlowPages.LANGUAGE_SCREEN,ctx)
        }


        LanguageScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.navigateToOnBoarding()
              //  accountsViewModel.resetSignupFlow()
            },
            onSkip = {
                navigator.push(ProfileUploadScreen())
            },
            onNext = {
                navigator.push(ProfileUploadScreen())
            },
            screenModel = screenModel,
            accountsViewModel = accountsViewModel
        )
    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun LanguageScreenContent(
    isDarkMode:Boolean,
    goBack:()->Unit,
    onSkip:()->Unit,
    onNext:()->Unit,
    screenModel: LanguageScreenModel,
    accountsViewModel: AccountsViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val state by screenModel.state.collectAsState()


    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
      //  goBack()
    }


    val accountsState by accountsViewModel.state.collectAsState()

    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            onNext()
        }
    }



    Column(
        modifier = Modifier.noRippleClickable {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    ) {

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
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {

            Spacer(Modifier.height(3.dp))

            Box(
                modifier = Modifier.size(24.dp).align(Alignment.Start).noRippleClickable(accountsViewModel::showExitConfirmationDialog),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_xmark),
                    contentDescription = null,
                    modifier = Modifier.size(10.dp),
                    tint = if (isDarkMode) Color.White else Color(0xff475467)
                )
            }
            Spacer(Modifier.height(8.dp))

            CustomLinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                strokeWidth = 6.dp,
                progress = 15f/16f,
                isDarkMode = isDarkMode
            )



            Spacer(Modifier.height(40.dp))


            Text(
                text = stringResource(Res.string.what_languages_do_you_know),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.select_up_to_five),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )


            Spacer(Modifier.height(16.dp))

            SearchTextField(
                query = state.searchQuery,
                onQueryChange = screenModel::search,
                label = stringResource(Res.string.search_languages),
                isDarkMode = isDarkMode,
                cursorColor = SolidColor(if (isDarkMode) DISABLED_LIGHT else Color.Black)
            )

            Spacer(Modifier.height(16.dp))

            if ((state.searchResults.isEmpty() && state.searchQuery.isNotBlank())){
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){
                    Text(
                        text = stringResource(Res.string.no_result_for_language),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 21.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                        textAlign = TextAlign.Center
                    )
                }
            }else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.searchResults.ifEmpty { state.allLanguages }) {item ->
                        TextWithCheckbox(
                            modifier = Modifier.align(Alignment.Start),
                            text = item,
                            isChecked = screenModel.isItemSelected(item),
                            hideCheckBox = false,
                            onClick = { screenModel.onItemClicked(item) },
                            isDarkMode = isDarkMode
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomDivider()
                    }
                }
            }


            Spacer(Modifier.height(13.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).noRippleClickable(onSkip),
                text = stringResource(Res.string.skip),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            ButtonWithText(
                text = "${stringResource(Res.string.next)} ${state.selectedLanguages.size}/5",
                bgColor = if (state.selectedLanguages.isNotEmpty()) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (state.selectedLanguages.isNotEmpty()) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (state.selectedLanguages.isNotEmpty()) {
                        accountsViewModel.addLanguages(state.selectedLanguages)
                    }
                }
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

    if (accountsState.isLoading){
        CircularLoader()
    }

    if (accountsState.showExitConfirmationDialog){
        SignupConfirmExitDialog(
            onCancel = accountsViewModel::hideExitConfirmationDialog,
            onConfirm = {
                accountsViewModel.hideExitConfirmationDialog()
                goBack()
            },
            isDarkMode = isDarkMode
        )
    }


}