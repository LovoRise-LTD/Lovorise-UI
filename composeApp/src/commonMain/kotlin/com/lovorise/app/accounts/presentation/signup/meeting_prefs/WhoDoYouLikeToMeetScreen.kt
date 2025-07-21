package com.lovorise.app.accounts.presentation.signup.meeting_prefs

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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_person
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.open_to_everyone
import coinui.composeapp.generated.resources.who_would_you_like_to_meet
import coinui.composeapp.generated.resources.who_would_you_like_to_meet_options
import coinui.composeapp.generated.resources.you_will_only_be_shown_to_people_looking_to_date_your_gender
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.dating_prefs.DatingPreferenceScreen
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.CustomLinearProgressIndicator
import com.lovorise.app.components.TextWithCheckbox
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.components.CustomSwitch
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class WhoDoYouLikeToMeetScreen : Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.setSignupPage(SignupFlowPages.WHO_DO_YOU_LIKE_TO_MEET_SCREEN,ctx)
        }

        WhoDoYouLikeToMeetScreenContent(
            isDarkMode = isDarkMode,
            navigateToDatingPrefScreen = {
                navigator.push(DatingPreferenceScreen())
            },
            goBack = {
                navigator.navigateToOnBoarding()
               // accountsViewModel.resetSignupFlow()
            },
            accountsViewModel = accountsViewModel
        )
    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun WhoDoYouLikeToMeetScreenContent(
    isDarkMode:Boolean,
    navigateToDatingPrefScreen:()->Unit,
    goBack:()->Unit,
    accountsViewModel: AccountsViewModel
) {

    val res = stringArrayResource(Res.array.who_would_you_like_to_meet_options)
    val genders by remember { mutableStateOf(res) }
    var selectedGenders by rememberSaveable{ mutableStateOf(listOf<String>()) }

    var isEveryoneSelected by rememberSaveable { mutableStateOf(false) }


    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
       // goBack()
    }

    val accountsState by accountsViewModel.state.collectAsState()

    LaunchedEffect(accountsState.success){
        if (accountsState.success){
            navigateToDatingPrefScreen()
        }
    }

    Column(
        modifier = Modifier
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
                progress = 6f/16f,
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
                text = stringResource(Res.string.who_would_you_like_to_meet),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(40.dp))
            OpenToEveryOne(
                isDarkMode = isDarkMode,
                isChecked = isEveryoneSelected,
                onCheckChanged = {
                    isEveryoneSelected = !isEveryoneSelected
                    selectedGenders = selectedGenders.toMutableList().apply {
                        if (isEveryoneSelected) {
                            removeAll(selectedGenders)
                            add("All")
                        }else{
                            remove("All")
                        }
                    }

                }
            )
            Spacer(Modifier.height(20.dp))

            genders.forEach { value ->
                if (value != "All") {
                    val isSelected = selectedGenders.contains(value)
                    TextWithCheckbox(
                        modifier = Modifier.align(Alignment.Start),
                        text = value,
                        isChecked = isSelected,
                        hideCheckBox = !isSelected,
                        onClick = {
                            if (isSelected) {
                                selectedGenders = selectedGenders.toMutableList().apply {
                                    remove(value)
                                }
                            } else {
                                if (selectedGenders.size < 2) {
                                    selectedGenders = selectedGenders.toMutableList().apply {
                                        isEveryoneSelected = false
                                        remove("All")
                                        add(value)
                                    }
                                }
                            }
                        },
                        isDarkMode = isDarkMode
                    )
//                if (index == genders.lastIndex && selectedGenderIndex == genders.lastIndex){
//                    Spacer(Modifier.height(6.dp))
//                    AddMoreAboutGender(
//                        onClick = {
//                            showBottomSheet = true
//                        }
//                    )
//
//                }
                    Spacer(Modifier.height(8.dp))
                    CustomDivider()
                }

            }


            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(Res.string.you_will_only_be_shown_to_people_looking_to_date_your_gender),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                letterSpacing = 0.2.sp
            )


            Spacer(Modifier.height(40.dp))



            ButtonWithText(
                text = "${stringResource(Res.string.next)} ${selectedGenders.size}/2",
                bgColor = if (selectedGenders.isNotEmpty()) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (selectedGenders.isNotEmpty()) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (selectedGenders.isNotEmpty()) {
                        accountsViewModel.addWhoDoYouLikeToMeet(selectedGenders)
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



}

@Composable
fun OpenToEveryOne(
    isChecked:Boolean,
    onCheckChanged:()->Unit,
    isDarkMode:Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Box(Modifier.requiredHeight(20.dp).requiredWidth(36.dp)) {
            CustomSwitch(
                modifier = Modifier.fillMaxSize(),
                isChecked = isChecked,
                onCheckChanged = {
                    onCheckChanged()
                },
                isDarkMode = isDarkMode
            )
        }

        Text(
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            text = stringResource(Res.string.open_to_everyone),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontFamily = PoppinsFontFamily()
        )
    }
}