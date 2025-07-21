package com.lovorise.app.accounts.presentation.signup.gender

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_more_about_your_gender
import coinui.composeapp.generated.resources.gender
import coinui.composeapp.generated.resources.genders
import coinui.composeapp.generated.resources.ic_chevron_down
import coinui.composeapp.generated.resources.ic_chevron_right
import coinui.composeapp.generated.resources.ic_person
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.more_gender_options
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.pick_gender_that_describe_you
import coinui.composeapp.generated.resources.save_and_close
import coinui.composeapp.generated.resources.tell_something_missing
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.meeting_prefs.WhoDoYouLikeToMeetScreen
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.CustomLinearProgressIndicator
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


class GenderScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val viewModel = navigator.koinNavigatorScreenModel<GenderScreenModel>()
        val ctx = LocalPlatformContext.current


        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        val genders = stringArrayResource(Res.array.genders)
        val moreOptions = stringArrayResource(Res.array.more_gender_options)

        LaunchedEffect(true){
            viewModel.loadData(genders,moreOptions)
            accountsViewModel.setSignupPage(SignupFlowPages.GENDER_SCREEN,ctx)
        }


        GenderScreenContent(
            goBack = {
                navigator.navigateToOnBoarding()
               // accountsViewModel.resetSignupFlow()
            },
            isDarkMode = isDarkMode,
            navigateToWhoDoYouLikeToMeet = {
                navigator.push(WhoDoYouLikeToMeetScreen())
            },
            navigateToAboutGenderScreen = {
                navigator.push(AboutYourGenderScreen())
            },
            genderScreenModel = viewModel,
            accountsViewModel = accountsViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class)
@Composable
fun GenderScreenContent(
    goBack:()->Unit,
    isDarkMode:Boolean,
    navigateToWhoDoYouLikeToMeet:()->Unit,
    navigateToAboutGenderScreen:()->Unit,
    genderScreenModel: GenderScreenModel,
    accountsViewModel: AccountsViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val state = genderScreenModel.state.collectAsStateWithLifecycle().value

    val sheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true)


    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
       // goBack()
    }


    val accountsState by accountsViewModel.state.collectAsState()


    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            navigateToWhoDoYouLikeToMeet()
            genderScreenModel.resetState()
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
            horizontalAlignment = Alignment.CenterHorizontally
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
                progress = 5f/16f,
                isDarkMode = isDarkMode
            )


            Spacer(Modifier.height(40.dp))

            Box(Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_person),
                    contentDescription = null,
                    modifier = Modifier.width(20.dp).height(22.dp),
                    tint = if (isDarkMode) Color.White else Color(0xff344054)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(Res.string.gender),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(40.dp))

            state.genders.forEachIndexed { index, value ->
                TextWithCheckbox(
                    modifier = Modifier.align(Alignment.Start),
                    text = value,
                    isChecked = index == state.selectedGenderIndex,
                    hideCheckBox = index != state.selectedGenderIndex,
                    onClick = { genderScreenModel.selectedGenderIndexChange(index) },
                    isDarkMode = isDarkMode
                )
                if (index == state.genders.lastIndex && state.selectedGenderIndex == state.genders.lastIndex){
                    Spacer(Modifier.height(6.dp))
                    AddMoreAboutGender(
                        onClick = {
                            genderScreenModel.updateBottomSheetState(true)
                        },
                        text = state.moreAboutGender.ifBlank { stringResource(Res.string.add_more_about_your_gender) }
                    )

                }
                Spacer(Modifier.height(8.dp))
                CustomDivider()

            }


            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(Res.string.pick_gender_that_describe_you),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                letterSpacing = 0.2.sp
            )


            Spacer(Modifier.height(40.dp))



            ButtonWithText(
                text = stringResource(Res.string.next),
                bgColor = if (state.selectedGenderIndex >= 0) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (state.selectedGenderIndex >= 0) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (state.selectedGenderIndex >= 0) {
                        accountsViewModel.addGender(gender = state.genders[state.selectedGenderIndex], moreInfo = state.moreAboutGender )
                    }
                }
            )


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


    if (state.showBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp,0.dp,0.dp,0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                genderScreenModel.updateBottomSheetState(false)
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ){

            GenderBottomSheetContent(
                isDarkMode = isDarkMode,
                onClick = genderScreenModel::selectedMoreOptionIndexChange,
                selectedIndex = state.selectedMoreOptionIndex,
                onSave = {
                    genderScreenModel.saveGenderInfo()
                    genderScreenModel.updateBottomSheetState(false)
                },
                options = state.moreOptions,
                navigateToAddMore = {
                    genderScreenModel.updateBottomSheetState(false)
                    genderScreenModel.selectedMoreOptionIndexChange(-1)
                    navigateToAboutGenderScreen()
                }
            )


        }
    }

}


@Composable
fun GenderBottomSheetContent(isDarkMode: Boolean,options:List<String>,selectedIndex:Int,onClick: (Int) -> Unit,onSave:()->Unit,navigateToAddMore:()->Unit){

    Column(Modifier
        .background(if (isDarkMode) BASE_DARK else Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 17.dp
                )
                .padding(top = 34.dp, bottom = 26.49.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(Res.string.add_more_about_your_gender),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 20.sp,
                color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(22.dp))

            options.forEachIndexed { index, value ->
                TextWithCheckbox(
                    modifier = Modifier.align(Alignment.Start),
                    text = value,
                    isChecked = index == selectedIndex,
                    hideCheckBox = false,
                    onClick = {
                        onClick(index)
                    },
                    isDarkMode = isDarkMode
                )
                Spacer(Modifier.height(8.dp))
                CustomDivider()
            }

            Spacer(Modifier.height(12.dp))

            TellUsMoreComponent(onClick = navigateToAddMore, isDarkMode = isDarkMode)

            Spacer(Modifier.height(29.dp))

            ButtonWithText(
                text = stringResource(Res.string.save_and_close),
                bgColor = if (selectedIndex >= 0) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (selectedIndex >= 0) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = onSave
            )


        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )

    }

}


@Composable
fun TellUsMoreComponent(onClick: () -> Unit,isDarkMode: Boolean){
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 11.5.dp).noRippleClickable(onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.tell_something_missing),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            letterSpacing = 0.2.sp
        )


        Icon(
            imageVector = vectorResource(Res.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier.width(10.51.dp).height(20.01.dp),
            tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )
    }
}


@Composable
fun AddMoreAboutGender(
    text:String,
    onClick:()->Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 11.5.dp).noRippleClickable(onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        Text(
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = Color(0xff555D69),
            letterSpacing = 0.2.sp
        )


        Image(
            imageVector = vectorResource(Res.drawable.ic_chevron_down),
            contentDescription = null,
            modifier = Modifier.width(12.dp).height(7.dp)
        )
    }
    
}