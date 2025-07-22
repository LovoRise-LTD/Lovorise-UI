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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.about_us
import coinui.composeapp.generated.resources.account
import coinui.composeapp.generated.resources.anonymous_mode
import coinui.composeapp.generated.resources.appearance
import coinui.composeapp.generated.resources.blocked_people
import coinui.composeapp.generated.resources.change_your_location_to_explore_lovorise_member_in_different_cities
import coinui.composeapp.generated.resources.community_guidelines
import coinui.composeapp.generated.resources.contact_us
import coinui.composeapp.generated.resources.control_your_profile
import coinui.composeapp.generated.resources.current_location
import coinui.composeapp.generated.resources.dark
import coinui.composeapp.generated.resources.devices
import coinui.composeapp.generated.resources.discover
import coinui.composeapp.generated.resources.help_and_support
import coinui.composeapp.generated.resources.hide_location
import coinui.composeapp.generated.resources.hide_my_age
import coinui.composeapp.generated.resources.ic_chevron_right_light_color
import coinui.composeapp.generated.resources.ic_lovorise_gray_circular
import coinui.composeapp.generated.resources.language
import coinui.composeapp.generated.resources.light
import coinui.composeapp.generated.resources.location
import coinui.composeapp.generated.resources.logout
import coinui.composeapp.generated.resources.lovorise
import coinui.composeapp.generated.resources.manage_account
import coinui.composeapp.generated.resources.manage_device
import coinui.composeapp.generated.resources.notifications
import coinui.composeapp.generated.resources.online_status
import coinui.composeapp.generated.resources.preferences
import coinui.composeapp.generated.resources.privacy_and_community
import coinui.composeapp.generated.resources.privacy_and_security
import coinui.composeapp.generated.resources.privacy_policy
import coinui.composeapp.generated.resources.profile_verification
import coinui.composeapp.generated.resources.read_receipts
import coinui.composeapp.generated.resources.send_read_receipts
import coinui.composeapp.generated.resources.settings
import coinui.composeapp.generated.resources.system_default
import coinui.composeapp.generated.resources.terms_and_conditions
import coinui.composeapp.generated.resources.turning_off_will_prevent_any_matches_from_activating
import coinui.composeapp.generated.resources.using_default_language_message
import coinui.composeapp.generated.resources.we_will_show_you_people_who_match_vibe_outside_your_range
import coinui.composeapp.generated.resources.when_turned_off_your_profile_will_be_hidden
import coinui.composeapp.generated.resources.you_can_change_your_location
import coinui.composeapp.generated.resources.you_will_be_discoverable
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.AppSettingsData
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signin.SignInScreen
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.profile_upload.ProfileUploadScreenViewModel
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.home.isNull
import com.lovorise.app.libs.location.LocationData
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.ProfileScreensState
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.profile.presentation.components.GetVerifiedBadgeDialog
import com.lovorise.app.profile.presentation.components.VerificationSuccessDialog
import com.lovorise.app.profile.presentation.components.VerificationUnderReviewDialog
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenModel
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenState
import com.lovorise.app.profile.presentation.edit_profile.hideWithCompletion
import com.lovorise.app.profile.presentation.verification.ChooseIDVerificationOptionScreen
import com.lovorise.app.profile.presentation.verification.ImageVerificationScreen
import com.lovorise.app.settings.presentation.components.AnonymousDisabledBottomSheetContent
import com.lovorise.app.settings.presentation.components.AnonymousEnabledBottomSheetContent
import com.lovorise.app.settings.presentation.components.ItemText
import com.lovorise.app.settings.presentation.components.LocationLoader
import com.lovorise.app.settings.presentation.components.LogoutConfirmationDialog
import com.lovorise.app.settings.presentation.components.SettingsScreenDivider
import com.lovorise.app.settings.presentation.components.TextWithBackground
import com.lovorise.app.settings.presentation.components.TextWithChevronRight
import com.lovorise.app.settings.presentation.components.TextWithSwitchBox
import com.lovorise.app.settings.presentation.components.TitleText
import com.lovorise.app.settings.presentation.screens.help_support.HelpAndSupportScreen1
import com.lovorise.app.settings.presentation.screens.privacy_and_security.screens.PrivacyAndSecurityScreen
import com.lovorise.app.settings.presentation.screens.travel_ticket.TravelTicketScreen
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeType
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import io.ktor.util.reflect.instanceOf
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class SettingsScreen  : Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val systemDarkMode = isSystemInDarkTheme()
        var isDarkMode = themeViewModel.isDarkMode(systemDarkMode)
        val editProfileScreenModel = navigator.koinNavigatorScreenModel<EditProfileScreenModel>()
        val editProfileScreenState by editProfileScreenModel.state.collectAsState()
        val context = LocalPlatformContext.current
       // val prefs = remember { SharedPrefsImpl(context) }
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()
        val profileUploadModel = navigator.koinNavigatorScreenModel<ProfileUploadScreenViewModel>()
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profileScreenState by profileScreenModel.state.collectAsState()

        val currentTheme by themeViewModel.currentThemeType.collectAsStateWithLifecycle()

        LaunchedEffect(currentTheme){
            isDarkMode = themeViewModel.isDarkMode(systemDarkMode)
        }

        LaunchedEffect(true){
//            if (accountsState.currentLocation == null){
//                val locationManager = LocationManager(context)
//                val currentLocation = locationManager.getCurrentLocation()
//                if (currentLocation != null) {
//                    accountsViewModel.updateCurrentLocation(currentLocation)
//                }
//            }
        }

        SettingsScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            navigateToBlockUsers = {
                navigator.push(BlockedUsersScreen())
            },
            navigateToManageAccount = {
                navigator.push(ManageAccountScreen())
            },
            navigateToAppLanguageScreen = {
                navigator.push(AppLanguageScreen())
            },
//            navigateToPreferencesScreen = {
//                navigator.push(PreferencesScreen())
//            },
            navigateToLocationTravelScreen = {
                navigator.push(TravelTicketScreen())
            },
            navigateToHelpAndSupportScreen = {
                navigator.push(HelpAndSupportScreen1())
            },
            onLogout = {
                accountsViewModel.logout(context) {
                    profileScreenModel.updateRefresh(true)
                    profileUploadModel.resetState()
                    if (navigator.items.any { it.instanceOf(SignInScreen::class) }) {
                        navigator.popUntil {
                            it.instanceOf(SignInScreen::class)
                        }
                    } else {
                        navigator.popAll()
                        navigator.push(SignInScreen())
                        navigator.push(SignInScreen())
                        navigator.pop()
                    }
                }
            },
            // accountsViewModel = accountsViewModel,
            navigateToNotificationSettings = {
                navigator.push(NotificationSettingsScreen())
            },
            accountsState = accountsState,
            accountsViewModel = accountsViewModel,
            profileScreenState = profileScreenState,
            navigateToPurchaseSubscription = {
                navigator.push(
                    PurchaseSubscriptionScreen(
                        //navigator = navigator,
                        subscriptionType = SubscriptionType.WEEKLY
                    )
                )
            },
            profileScreensState = profileScreenState,
            profileScreenModel = profileScreenModel,
            navigateToImageVerification = {
                navigator.push(ImageVerificationScreen())
            },
            navigateToIDVerification = {
                navigator.push(ChooseIDVerificationOptionScreen())
            },
            navigateToManageDevice = {
                navigator.push(ManageDeviceScreen())
            },
            themeViewModel = themeViewModel,
            themeType = currentTheme,
            systemDark = systemDarkMode,
            editProfileScreenModel = editProfileScreenModel,
            editProfileScreenState = editProfileScreenState,
            navigateToPrivacyAndSecurity = {
                navigator.push(PrivacyAndSecurityScreen())
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    isDarkMode:Boolean,
    goBack:()->Unit,
    navigateToBlockUsers:()->Unit,
    navigateToManageAccount:()->Unit,
   // navigateToSignInScreen:()->Unit,
    navigateToAppLanguageScreen: () -> Unit,
    navigateToPrivacyAndSecurity: () -> Unit,
    navigateToLocationTravelScreen: () -> Unit,
    navigateToHelpAndSupportScreen: () -> Unit,
    navigateToNotificationSettings: () -> Unit,
    onLogout:()->Unit,
    accountsState:AccountsApiCallState,
    accountsViewModel: AccountsViewModel,
    themeViewModel: ThemeViewModel,
    profileScreensState: ProfileScreensState,
    profileScreenModel: ProfileScreenModel,
    profileScreenState:ProfileScreensState,
    navigateToPurchaseSubscription:()->Unit,
    navigateToImageVerification:()->Unit,
    navigateToManageDevice:()->Unit,
    navigateToIDVerification:()->Unit,
    editProfileScreenState: EditProfileScreenState,
    editProfileScreenModel: EditProfileScreenModel,
    themeType: ThemeType,
    systemDark:Boolean
) {

    val isLocked = remember(profileScreenState.subscriptionType) { profileScreenState.subscriptionType == SubscriptionType.FREE}

    var showLogoutConfirmationDialog by remember { mutableStateOf(false) }
    var anonymousBottomSheetType by remember { mutableStateOf<AnonymousBottomSheetType?>(null) }
    val anonymousBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isAnonymous by rememberSaveable{ mutableStateOf(false) }
    val context = LocalPlatformContext.current
    LaunchedEffect(accountsState.appSettingsData?.anonymousModeExpiry){
        val expiry = accountsState.appSettingsData?.anonymousModeExpiry
        if (expiry != null){
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date // Get the current LocalDate
            isAnonymous = expiry >= currentDate
        }else{
            isAnonymous = false
        }
    }

    Column(modifier = Modifier.background(if (isDarkMode) BASE_DARK else Color.White)) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()


        Column(
            modifier = Modifier
             //   .background(Color.White)
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            HeaderWithTitleAndBack(title = stringResource(Res.string.settings), onBack = goBack, isDarkMode = isDarkMode)

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            ) {
                AccountSection(
//                    onProfileVerification = {
//                        if (accountsState.user?.isVerified != true){
//                            if (profileScreensState.isVerificationUnderReview){
//                                profileScreenModel.updateVerificationUnderReviewDialogState(true)
//                            }else{
//                                profileScreenModel.updateGetVerifyBadgeDialogState(true)
//                            }
//                        }else{
//                            profileScreenModel.updateVerificationSuccessDialog(true)
//                        }
//                    },
                    onBlockedUsers = navigateToBlockUsers,
                    onManageAccountClick = navigateToManageAccount,
                    isDarkMode = isDarkMode,
                    onPrivacyAndSecurity = navigateToPrivacyAndSecurity
                )

                SettingsScreenDivider(isDarkMode = isDarkMode)

                LocationSection(onClick = navigateToLocationTravelScreen, location = accountsState.currentLocation,isDarkMode = isDarkMode)

//                SettingsScreenDivider(isDarkMode = isDarkMode)

//                ShowInDistance(
//                    selectedDistance = editProfileScreenState.distanceMeasurement,
//                    onClick = editProfileScreenModel::updateDistanceMeasurement,
//                    isDarkMode = isDarkMode
//                )

//                TextWithBackground(stringResource(Res.string.we_will_show_you_people_who_match_vibe_outside_your_range), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))

//                SettingsScreenDivider(isDarkMode)
//
//                DiscoverSection(
//                    isAnonymous = isAnonymous,
//                    onSetAnonymousMode = {
//                        if (isLocked) {
//                            navigateToPurchaseSubscription()
//                        }else{
//                            isAnonymous = !isAnonymous
//                            if (!isAnonymous){
//                                accountsViewModel.removeAnonymousMode(context){
//                                    anonymousBottomSheetType = AnonymousBottomSheetType.NOT_ANONYMOUS_SHEET
//                                }
//                            }else{
//                                accountsViewModel.setAnonymousStatus(context){
//                                    anonymousBottomSheetType = AnonymousBottomSheetType.ANONYMOUS_SHEET
//                                }
//                            }
//                        }
//                    },
//                    isDarkMode = isDarkMode
//                )

                SettingsScreenDivider(isDarkMode)

                NotificationsSection(navigateToNotificationSettings,isDarkMode)

//                SettingsScreenDivider(isDarkMode)
//
//                ReadReceiptsSection(
//                    value = accountsState.appSettingsData?.readReceipt ?: false,
//                    onChange = {
//                        if (isLocked) {
//                            navigateToPurchaseSubscription()
//                        }else{
//                            accountsViewModel.updateReadReceipt(context,it)
//                        }
//                    },
//                    isDarkMode = isDarkMode
//                )

//                SettingsScreenDivider(isDarkMode)
//
//                AppearanceSection(
//                    isDarkMode = isDarkMode,
//                    onUpdateTheme = { themeViewModel.updateTheme(it,context,systemDark) },
//                    type = themeType
//                )

                SettingsScreenDivider(isDarkMode)

                LanguageSection(language = accountsState.language ?: "English",onLanguageClick = navigateToAppLanguageScreen, isDarkMode = isDarkMode)

                SettingsScreenDivider(isDarkMode)

                ManageDeviceSection(onManageDevice = navigateToManageDevice , isDarkMode = isDarkMode)

                SettingsScreenDivider(isDarkMode)

                ContactUsSection(
                    onContactEnquiryClick = {
                        openUrlInCustomTab(AppConstants.CONTACT_US_URL, context)
                    },
                    onHelpAndSupportClick = navigateToHelpAndSupportScreen,
                    isDarkMode = isDarkMode
                )

                SettingsScreenDivider(isDarkMode)

                PrivacyAndCommunity(
                    onTermsAndConditionClick = {
                        openUrlInCustomTab(AppConstants.TERMS_AND_CONDITIONS_URL,context)
                    },
                    onPrivacyPolicyClick = {
                        openUrlInCustomTab(AppConstants.PRIVACY_POLICY_URL,context)
                    },
                    onAboutUsClick = {
                        openUrlInCustomTab(AppConstants.ABOUT_US_URL,context)
                    },
                    onCommunityGuidelinesClick = {
                        openUrlInCustomTab(AppConstants.COMMUNITY_GUIDELINES_URL,context)
                    },
                    isDarkMode = isDarkMode
                )

                SettingsScreenDivider(isDarkMode)

                LogoutSection(
                    onLogoutClick = {
                        showLogoutConfirmationDialog = true
                    },
                    isDarkMode = isDarkMode
                )

                SettingsScreenDivider(isDarkMode)

                VersionSection(isDarkMode)

            }

        }
        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9))
        )
    }

    if (showLogoutConfirmationDialog){
        LogoutConfirmationDialog(
            onCancel = {
                showLogoutConfirmationDialog = false
            },
            onLogout = {
                showLogoutConfirmationDialog = false
                onLogout()
                //navigateToSignInScreen()
            },
            isDarkMode = isDarkMode
        )
    }


    if (anonymousBottomSheetType != null) {
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = anonymousBottomSheetState,
            onDismissRequest = {
                anonymousBottomSheetType = null
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            if (anonymousBottomSheetType == AnonymousBottomSheetType.ANONYMOUS_SHEET){
                AnonymousEnabledBottomSheetContent(
                    isDarkMode = isDarkMode,
                    onCancel = {
                        anonymousBottomSheetState.hideWithCompletion(scope){
                            anonymousBottomSheetType = null
                        }
                    }
                )
            }
            else if (anonymousBottomSheetType == AnonymousBottomSheetType.NOT_ANONYMOUS_SHEET){
                AnonymousDisabledBottomSheetContent(
                    isDarkMode = isDarkMode,
                    onCancel = {
                        anonymousBottomSheetState.hideWithCompletion(scope){
                            anonymousBottomSheetType = null
                        }
                    }
                )
            }

        }
    }

    if (profileScreensState.showGetVerifyBadgeDialog){
        GetVerifiedBadgeDialog(
            onCancel = { profileScreenModel.updateGetVerifyBadgeDialogState(false) },
            onIDPhotoVerification = { profileScreenModel.updateGetVerifyBadgeDialogState(false); navigateToIDVerification() },
            onPhotoVerification = { profileScreenModel.updateGetVerifyBadgeDialogState(false); navigateToImageVerification() },
            isDarkMode = isDarkMode
        )
    }

    if(profileScreensState.showVerificationSuccessDialog){
        VerificationSuccessDialog(
            onCancel = {
                profileScreenModel.updateVerificationSuccessDialog(false)
            },
            isDarkMode = isDarkMode
        )
    }

    if (profileScreensState.showVerificationUnderReviewDialog){
        VerificationUnderReviewDialog(
            onCancel = { profileScreenModel.updateVerificationUnderReviewDialogState(false) },
            isDarkMode = isDarkMode
        )
    }

    if (accountsState.isLoading){
        CircularLoader(true)
    }


}

@Composable
fun VersionSection(isDarkMode: Boolean) {
    Box(Modifier.fillMaxWidth().height(77.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)), contentAlignment = Alignment.Center){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth().height(34.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_lovorise_gray_circular),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp).clip(CircleShape)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = stringResource(Res.string.lovorise),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp,
                    color = Color(0xff98A2B3)
                )


            }
            Text(
                text = "Version 1.0.0",
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                letterSpacing = 0.2.sp,
                color = Color(0xff98A2B3)
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}


@Composable
fun LogoutSection(
    onLogoutClick:()->Unit,
    isDarkMode: Boolean
) {
    Box(Modifier.fillMaxWidth().height(52.dp), contentAlignment = Alignment.Center){
        Text(
            modifier = Modifier.noRippleClickable(onLogoutClick),
            text = stringResource(Res.string.logout),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontFamily = PoppinsFontFamily(),
            letterSpacing = 0.2.sp,
            color = if (isDarkMode) Color.White else Color.Black,
        )
    }
}


@Composable
fun PrivacyAndCommunity(
    onTermsAndConditionClick:()->Unit,
    onPrivacyPolicyClick:()->Unit,
    onCommunityGuidelinesClick:()->Unit,
    onAboutUsClick:()->Unit,
    isDarkMode: Boolean
) {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        Box(Modifier.fillMaxWidth().height(44.dp), contentAlignment = Alignment.CenterStart){
            TitleText(stringResource(Res.string.privacy_and_community),isDarkMode)
        }
        Spacer(Modifier.height(5.5.dp))
        TextWithChevronRight(text = stringResource(Res.string.terms_and_conditions), onClick = onTermsAndConditionClick,isDarkMode)
        Spacer(Modifier.height(8.dp))
        TextWithChevronRight(text = stringResource(Res.string.privacy_policy), onClick = onPrivacyPolicyClick,isDarkMode)
        Spacer(Modifier.height(8.dp))
        TextWithChevronRight(text = stringResource(Res.string.community_guidelines), onClick = onCommunityGuidelinesClick,isDarkMode)
        Spacer(Modifier.height(8.dp))
        TextWithChevronRight(text = stringResource(Res.string.about_us), onClick = onAboutUsClick,isDarkMode)
        Spacer(Modifier.height(20.5.dp))
    }
}

@Composable
fun ManageDeviceSection(
    onManageDevice:()->Unit,
    isDarkMode: Boolean
) {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        Box(Modifier.fillMaxWidth().height(44.dp), contentAlignment = Alignment.CenterStart){
            TitleText(stringResource(Res.string.manage_device), isDarkMode = isDarkMode)
        }
        Spacer(Modifier.height(5.5.dp))
        TextWithChevronRight(text = stringResource(Res.string.devices), onClick = onManageDevice, isDarkMode = isDarkMode)
//        Spacer(Modifier.height(8.dp))
//        TextWithChevronRight(text = stringResource(Res.string.contact_for_inquiries), onClick = onContactEnquiryClick, isDarkMode = isDarkMode)
        Spacer(Modifier.height(20.5.dp))
    }

}

@Composable
fun ContactUsSection(
    onHelpAndSupportClick:()->Unit,
    onContactEnquiryClick:()->Unit,
    isDarkMode: Boolean
) {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        Box(Modifier.fillMaxWidth().height(44.dp), contentAlignment = Alignment.CenterStart){
            TitleText(stringResource(Res.string.contact_us), isDarkMode = isDarkMode)
        }
        Spacer(Modifier.height(5.5.dp))
        TextWithChevronRight(text = stringResource(Res.string.help_and_support), onClick = onHelpAndSupportClick, isDarkMode = isDarkMode)
//        Spacer(Modifier.height(8.dp))
//        TextWithChevronRight(text = stringResource(Res.string.contact_for_inquiries), onClick = onContactEnquiryClick, isDarkMode = isDarkMode)
        Spacer(Modifier.height(20.5.dp))
    }

}

@Composable
fun LanguageSection(
    language:String,
    onLanguageClick:()->Unit,
    isDarkMode: Boolean
) {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        Box(Modifier.fillMaxWidth().height(44.dp), contentAlignment = Alignment.CenterStart){
            TitleText(stringResource(Res.string.language), isDarkMode = isDarkMode)
        }
        Spacer(Modifier.height(5.5.dp))
        TextWithChevronRight(text = language.replaceFirstChar {  if (it.isLowerCase()) it.titlecase() else it.toString() }, onClick = onLanguageClick, isDarkMode = isDarkMode)
        Spacer(Modifier.height(20.5.dp))
    }

}



@Composable
fun AppearanceSection(isDarkMode: Boolean,type: ThemeType,onUpdateTheme:(ThemeType)->Unit) {


    Column(Modifier.padding(horizontal = 16.dp)) {

        Spacer(Modifier.height(16.dp))

        TitleText(stringResource(Res.string.appearance), isDarkMode = isDarkMode)


        Spacer(Modifier.height(16.dp))

        TextWithSwitchBox(
            text = stringResource(Res.string.system_default),
            isChecked = type == ThemeType.AUTO,
            onCheckChanged = {
                if (type != ThemeType.AUTO) {
                    onUpdateTheme(ThemeType.AUTO)
                }
            },
            isDarkMode = isDarkMode
        )

        Spacer(Modifier.height(16.dp))

        TextWithSwitchBox(
            text = stringResource(Res.string.light),
            isChecked = type == ThemeType.LIGHT,
            onCheckChanged = {
                if (type != ThemeType.LIGHT) {
                    onUpdateTheme(ThemeType.LIGHT)
                }
            },
            isDarkMode = isDarkMode
        )

        Spacer(Modifier.height(16.dp))

        TextWithSwitchBox(
            text = stringResource(Res.string.dark),
            isChecked = type == ThemeType.DARK,
            onCheckChanged = {
                if (type != ThemeType.DARK) {
                    onUpdateTheme(ThemeType.DARK)
                }
            },
            isDarkMode = isDarkMode
        )


        Spacer(Modifier.height(16.dp))

    }

    TextWithBackground(stringResource(Res.string.using_default_language_message), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))
}







@Composable
fun ReadReceiptsSection(value:Boolean,onChange:(Boolean)->Unit,isDarkMode: Boolean) {

    Column(Modifier.padding(horizontal = 16.dp)) {

        Spacer(Modifier.height(16.dp))

        TitleText(stringResource(Res.string.read_receipts),isDarkMode)


        Spacer(Modifier.height(16.dp))

        TextWithSwitchBox(
            text = stringResource(Res.string.send_read_receipts),
            isChecked = value,
            onCheckChanged = {
                onChange(!value)
            },
            isDarkMode = isDarkMode
        )

        Spacer(Modifier.height(16.dp))

    }

    TextWithBackground(stringResource(Res.string.turning_off_will_prevent_any_matches_from_activating), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))
}





@Composable
fun NotificationsSection(navigateToNotificationSettings:()->Unit,isDarkMode: Boolean) {

    Column(Modifier.padding(horizontal = 16.dp)) {

        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth().noRippleClickable(navigateToNotificationSettings), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            TitleText(stringResource(Res.string.notifications),isDarkMode)

            Box(Modifier.size(24.dp), contentAlignment = Alignment.Center){
                Image(
                    modifier = Modifier.size(24.dp),
                    imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}


@Composable
fun DiscoverSection(isAnonymous:Boolean,onSetAnonymousMode:()->Unit,isDarkMode: Boolean) {

    Column(Modifier.padding(horizontal = 16.dp)) {

        Spacer(Modifier.height(16.dp))

        TitleText(stringResource(Res.string.discover), isDarkMode = isDarkMode)


        Spacer(Modifier.height(16.dp))

        TextWithSwitchBox(
            text = stringResource(Res.string.anonymous_mode),
            isChecked = isAnonymous,
            onCheckChanged = onSetAnonymousMode,
            isDarkMode = isDarkMode
        )

        Spacer(Modifier.height(16.dp))

    }

    TextWithBackground(stringResource(Res.string.when_turned_off_your_profile_will_be_hidden), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))
}


@Composable
fun ControlProfileSection(data: AppSettingsData.ControlProfile,onValueChange:(AppSettingsData.ControlProfile) -> Unit,isDarkMode: Boolean) {

    Column(Modifier.padding(horizontal = 16.dp)) {

        Spacer(Modifier.height(16.dp))

        TitleText(stringResource(Res.string.control_your_profile), isDarkMode = isDarkMode)


        Spacer(Modifier.height(16.dp))

        TextWithSwitchBox(
            text = stringResource(Res.string.hide_my_age),
            isChecked = data.hideAge,
            onCheckChanged = {
                onValueChange(data.copy(hideAge = !data.hideAge))
            },
            isDarkMode = isDarkMode
        )

        Spacer(Modifier.height(16.dp))

        TextWithSwitchBox(
            text = stringResource(Res.string.online_status),
            isChecked = data.onlineStatus,
            onCheckChanged = {
                onValueChange(data.copy(onlineStatus = !data.onlineStatus))
            },
            isDarkMode = isDarkMode
        )

        Spacer(Modifier.height(16.dp))

        TextWithSwitchBox(
            text = stringResource(Res.string.hide_location),
            isChecked = data.hideLocation,
            onCheckChanged = {
                onValueChange(data.copy(hideLocation = !data.hideLocation))
            },
            isDarkMode = isDarkMode
        )


        Spacer(Modifier.height(16.dp))

    }

    TextWithBackground(stringResource(Res.string.you_will_be_discoverable), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))
}


@Composable
fun LocationSection(onClick:()->Unit,location: LocationData?,isDarkMode: Boolean) {
    Spacer(Modifier.height(16.dp))

    Column(Modifier.padding(horizontal = 16.dp).noRippleClickable(onClick)) {
        TitleText(stringResource(Res.string.location), isDarkMode = isDarkMode)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemText(
                text = stringResource(Res.string.current_location),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )
            if (location == null || location.isNull()){
                LocationLoader(Modifier)
            }else {
                ItemText(
                    text = "${location.city}, ${location.country}"
                )
            }
        }

    }

    Spacer(modifier = Modifier.height(20.dp))

    TextWithBackground(text = stringResource(Res.string.change_your_location_to_explore_lovorise_member_in_different_cities), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))

}





@Composable
fun AccountSection(
    onManageAccountClick:()->Unit,
    onPrivacyAndSecurity:()->Unit,
    onBlockedUsers:()->Unit,
    isDarkMode: Boolean
) {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(1.dp))
        Box(Modifier.fillMaxWidth().height(44.dp), contentAlignment = Alignment.CenterStart){
            TitleText(stringResource(Res.string.account), isDarkMode = isDarkMode)
        }
        Spacer(Modifier.height(5.5.dp))
        TextWithChevronRight(text = stringResource(Res.string.manage_account), onClick = onManageAccountClick, isDarkMode = isDarkMode)
        Spacer(Modifier.height(8.dp))
        TextWithChevronRight(text = stringResource(Res.string.privacy_and_security), onClick = onPrivacyAndSecurity, isDarkMode = isDarkMode)
        Spacer(Modifier.height(8.dp))
        TextWithChevronRight(text = stringResource(Res.string.blocked_people), onClick = onBlockedUsers, isDarkMode = isDarkMode)
//        Spacer(Modifier.height(8.dp))
//        TextWithChevronRight(text = stringResource(Res.string.blocked_users), onClick = onBlockedUsers, isDarkMode = isDarkMode)
        Spacer(Modifier.height(21.5.dp))
    }

}

fun openUrlExternally(uri: UriHandler,url:String){
    try {
        uri.openUri(url)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

enum class AnonymousBottomSheetType{
    ANONYMOUS_SHEET,NOT_ANONYMOUS_SHEET
}




