package com.lovorise.app.accounts.presentation.signup.choose_interests

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
import androidx.compose.runtime.key
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
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.choose_your_interests
import coinui.composeapp.generated.resources.get_better_recommendation_by_selecting_more_interests
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.no_result_for_interest
import coinui.composeapp.generated.resources.search_interest
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.choose_interests.components.InterestSection
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.height.HeightScreen
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.CustomLinearProgressIndicator
import com.lovorise.app.components.SearchTextField
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ChooseInterestsScreen : Screen {

    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { ChooseInterestsScreenModel() }
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            screenModel.reloadItems(emptyList())
            accountsViewModel.setSignupPage(SignupFlowPages.CHOOSE_INTEREST_SCREEN,ctx)
        }

        ChooseInterestsScreenContent(
            isDarkMode = isDarkMode,
            navigateToHeightScreen = {
                navigator.push(HeightScreen())
            },
            goBack = {
                navigator.navigateToOnBoarding()
                //  accountsViewModel.resetSignupFlow()
            },
            screenModel = screenModel,
            accountsViewModel = accountsViewModel
        )


    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun ChooseInterestsScreenContent(
    isDarkMode:Boolean,
    navigateToHeightScreen:()->Unit,
    goBack:()->Unit,
    screenModel: ChooseInterestsScreenModel,
    accountsViewModel: AccountsViewModel
){

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val state by screenModel.state.collectAsState()

    BackHandler(true){
        //goBack()
        accountsViewModel.showExitConfirmationDialog()
    }

    val accountsState by accountsViewModel.state.collectAsState()

    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            navigateToHeightScreen()
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
                progress = 8f/16f,
                isDarkMode = isDarkMode
            )



            Spacer(Modifier.height(40.dp))


            Text(
                text = stringResource(Res.string.choose_your_interests),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.get_better_recommendation_by_selecting_more_interests),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
            )

            Spacer(Modifier.height(8.dp))

            CustomDivider(color = Color(0xffD9D9D9).copy(alpha = 0.5f))

            Spacer(Modifier.height(16.dp))

            SearchTextField(
                query = state.searchQuery,
                onQueryChange = screenModel::search,
                label = stringResource(Res.string.search_interest),
                isDarkMode = isDarkMode,
                cursorColor = SolidColor(if (isDarkMode) Color.White else Color.Black)
            )

            Spacer(Modifier.height(16.dp))

            if ((state.searchResults.isEmpty() && state.searchQuery.isNotBlank())){
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){
                    Text(
                        text = stringResource(Res.string.no_result_for_interest),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 21.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.searchResults.ifEmpty { state.allItems }) { item ->
                        key(state.selectedItems) {
                            InterestSection(
                                item = item,
                                onChipClicked = {
                                    screenModel.onChipClicked(it)
                                },
                                isSelected = screenModel::isItemSelected,
                                onShowLess = {screenModel.onShowLess(item.id)},
                                onShowMore = {screenModel.onShowMore(item.id)},
                                showLess = !state.showMoreIds.contains(item.id),
                                isDarkMode = isDarkMode
                            )
                        }
                    }
                }
            }


            Spacer(Modifier.height(16.dp))


            ButtonWithText(
                text = "${stringResource(Res.string.next)} ${state.selectedItems.size}/10",
                bgColor = if (state.selectedItems.size >= 5) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (state.selectedItems.size >= 5) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (state.selectedItems.size >= 5) {
                        accountsViewModel.addInterests(screenModel.getInterestsData())
                       // navigateToHeightScreen()
                    }
                }
            )
            Spacer(Modifier.height(16.dp))



        }



        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )



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

    if (accountsState.isLoading){
        CircularLoader()
    }






}