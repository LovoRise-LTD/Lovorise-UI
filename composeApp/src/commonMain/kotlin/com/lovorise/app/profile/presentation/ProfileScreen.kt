package com.lovorise.app.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add
import coinui.composeapp.generated.resources.add_interests
import coinui.composeapp.generated.resources.complete_profile
import coinui.composeapp.generated.resources.create
import coinui.composeapp.generated.resources.create_reels
import coinui.composeapp.generated.resources.edit_profile
import coinui.composeapp.generated.resources.how_did_you_hear_about_us
import coinui.composeapp.generated.resources.ic_nav_5
import coinui.composeapp.generated.resources.ic_settings
import coinui.composeapp.generated.resources.ic_settings_white
import coinui.composeapp.generated.resources.ic_unverified_gray
import coinui.composeapp.generated.resources.ic_verified
import coinui.composeapp.generated.resources.inform
import coinui.composeapp.generated.resources.invite
import coinui.composeapp.generated.resources.invite_friends
import coinui.composeapp.generated.resources.my_hearts
import coinui.composeapp.generated.resources.onboarding
import coinui.composeapp.generated.resources.plans
import coinui.composeapp.generated.resources.post
import coinui.composeapp.generated.resources.profile
import coinui.composeapp.generated.resources.profile_visitors
import coinui.composeapp.generated.resources.share
import coinui.composeapp.generated.resources.share_reels
import coinui.composeapp.generated.resources.spotlights
import coinui.composeapp.generated.resources.unlimited
import coinui.composeapp.generated.resources.upload
import coinui.composeapp.generated.resources.upload_photos
import coinui.composeapp.generated.resources.verify
import coinui.composeapp.generated.resources.verify_identity
import coinui.composeapp.generated.resources.write_a_bio
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ShimmerAnimation
import com.lovorise.app.components.Toast
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.invite.InviteFriendsScreen
import com.lovorise.app.lovorise_hearts.presentation.screens.LovoriseHeartBalanceScreen
import com.lovorise.app.noRippleClickable
import com.lovorise.app.onboarding_info.OnboardingInfoScreen
import com.lovorise.app.profile.domain.models.GetCoinTask
import com.lovorise.app.profile.domain.models.IndividualTask
import com.lovorise.app.profile.domain.models.TaskProgress
import com.lovorise.app.profile.domain.models.TextButtonInfo
import com.lovorise.app.profile.presentation.components.CircularProfileImage
import com.lovorise.app.profile.presentation.components.ContinuePremiumSubscriptionDialog
import com.lovorise.app.profile.presentation.components.GetCoinsCard
import com.lovorise.app.profile.presentation.components.GetVerifiedBadgeDialog
import com.lovorise.app.profile.presentation.components.MyCoinsTaskCard
import com.lovorise.app.profile.presentation.components.PremiumCardSection
import com.lovorise.app.profile.presentation.components.PremiumSubscriptionCancelConfirmationDialog
import com.lovorise.app.profile.presentation.components.PremiumSubscriptionDetailsDialog
import com.lovorise.app.profile.presentation.components.PurchasedSpotlightBottomSheetContent
import com.lovorise.app.profile.presentation.components.RoundedTextButton
import com.lovorise.app.profile.presentation.components.SpotlightsButton
import com.lovorise.app.profile.presentation.components.SpotlightsSheetContent
import com.lovorise.app.profile.presentation.components.TextWithIndicator
import com.lovorise.app.profile.presentation.components.VerificationSuccessDialog
import com.lovorise.app.profile.presentation.components.VerificationUnderReviewDialog
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreen
import com.lovorise.app.profile.presentation.verification.ChooseIDVerificationOptionScreen
import com.lovorise.app.profile.presentation.verification.ImageVerificationScreen
import com.lovorise.app.profile_visitors.ProfileVisitorsScreen
import com.lovorise.app.reels.presentation.reels_create_upload_view.screens.CaptureRecordScreen
import com.lovorise.app.settings.presentation.screens.SettingsScreen
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class ProfileScreen(
    private val navigator: Navigator,
    private val onBottomNavStateChange:(TabsScreenModel.BottomTab)->Unit,
    private val screenHeight:Dp,
    private val accountsViewModel: AccountsViewModel,
    private val profileScreenModel: ProfileScreenModel,
   // private val reelsScreenModel: ReelsScreenModel
) :Tab{

    @Composable
    override fun Content() {
//        navigator.pop()
//        navigator.push()
        val accountsState by accountsViewModel.state.collectAsState()
        val context = LocalPlatformContext.current
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val profileScreenState by profileScreenModel.state.collectAsState()



        LaunchedEffect(true){
            if (profileScreenState.showHeartShopWelcomeDialog == null){
                profileScreenModel.updateHeartShopWelcomeDialogState(context)
            }
            profileScreenModel.updateRemainingTime()
//            if (accountsState.user == null){
//                CoroutineScope(Dispatchers.Main).launch {
//                    accountsViewModel.getUser(context){
//                        accountsViewModel.logout(context)
//                        navigator.push(OnboardingScreen())
//                        navigator.push(OnboardingScreen())
//                        navigator.pop()
//                    }
//                }
//            }


//            if (accountsState.images == null) {
//                CoroutineScope(Dispatchers.IO).launch {
//                    accountsViewModel.getImages(context)
//                }
//            }
        }
        ProfileScreenContent(
            isDarkMode = isDarkMode,
            navigateToOnboarding = {
                navigator.push(OnboardingInfoScreen())
            },
            navigateToInviteFriendsScreen = {
                navigator.push(InviteFriendsScreen())
            },
            navigateToCoinBalanceScreen = {
                navigator.push(LovoriseHeartBalanceScreen())
            },
            navigateToEditProfileScreen = {
                navigator.push(EditProfileScreen)
            },
            navigateToProfileVisitorsScreen = {
                navigator.push(ProfileVisitorsScreen())
            },
            navigateToProfileDescriptionScreen = {
                println("on click invoked!!")
                navigator.push(ProfileDescriptionScreen())
            },
            navigateToReelsScreen = {
                onBottomNavStateChange(TabsScreenModel.BottomTab.REELS)
            },
            navigateToSettingsScreen = {
                navigator.push(SettingsScreen())
            },
            screenHeight = screenHeight,
            accountsState = accountsState,
            navigateToSubscriptionScreen = {
                navigator.push(
                    PurchaseSubscriptionScreen(it)
                )
            },
            profileScreensState = profileScreenState,
            profileScreenModel = profileScreenModel,
            navigateToCreateReel = {
                navigator.push(CaptureRecordScreen())
            },
            navigateToImageVerification = {
                navigator.push(ImageVerificationScreen())
            },
            navigateToIDVerification = {
                navigator.push(ChooseIDVerificationOptionScreen())
            },
            navigateToLovoriseHearts = {
                navigator.push(LovoriseHeartBalanceScreen())
            }
        )
    }

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 4u,
            title = "Profile",
            icon = painterResource(Res.drawable.ic_nav_5)
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    navigateToOnboarding: () -> Unit,
    navigateToInviteFriendsScreen: () -> Unit,
    navigateToCoinBalanceScreen: () -> Unit,
//    navigateToRewardsScreen: () -> Unit,
  //  navigateToDummyScreen: () -> Unit,
    navigateToEditProfileScreen: () -> Unit,
    navigateToProfileVisitorsScreen: () -> Unit,
    navigateToProfileDescriptionScreen:()->Unit,
    navigateToReelsScreen:()->Unit,
    navigateToSettingsScreen:()->Unit,
    isDarkMode:Boolean,
    screenHeight: Dp,
    accountsState: AccountsApiCallState,
    navigateToSubscriptionScreen: (SubscriptionType)->Unit,
    profileScreensState: ProfileScreensState,
    profileScreenModel: ProfileScreenModel,
    navigateToCreateReel:()->Unit,
    navigateToImageVerification:()->Unit,
    navigateToIDVerification:()->Unit,
    navigateToLovoriseHearts:()->Unit
) {

    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    val sheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val purchasedSpotlightSheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var screenWidth by remember { mutableStateOf(0.dp) }

    val context = LocalPlatformContext.current


//    val animatedCurrentTabIndex by animateIntAsState(currentTabIndex)


    BoxWithConstraints(Modifier.fillMaxSize()) {
        //val screenHeight = maxHeight
        screenWidth = maxWidth
        Column(
            modifier = Modifier.fillMaxSize()
             //   .background(Color.White)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.padding(vertical = 8.dp).height(24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = vectorResource(if (isDarkMode) Res.drawable.ic_settings_white else Res.drawable.ic_settings),
                    contentDescription = "Settings",
                    modifier = Modifier.size(23.dp).noRippleClickable {
                        navigateToSettingsScreen()
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            key(accountsState.user?.medias){
                val media = accountsState.user?.medias?.firstOrNull()
                val url = if (!media?.localPath.isNullOrBlank()) media?.localPath
                else media?.url
                if (media?.type != null){
                    CircularProfileImage(
                        url = url ?: "",
                        completionPercent = accountsState.user.progress?.toFloat() ?: 0f,
                        onClick = navigateToProfileDescriptionScreen,
                        cachedPath = accountsState.user.medias.firstOrNull()?.localPath,
                        contentType = media.type,
                        isDarkMode = isDarkMode
                    )
                }else{
                    Box(Modifier
                        .size(106.dp)
                        .clip(CircleShape)
                        .background(Color(0xffEAECF0)))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (accountsState.user?.name.isNullOrBlank()){
                    ShimmerAnimation(Modifier.height(30.dp).width(200.dp).fillMaxHeight())
                }else {
                    Text(
                        text = "${accountsState.user?.name}, ",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.2.sp,
                        color =  if(isDarkMode) Color.White else Color(0xff101828)
                    )
                    Text(
                        text = "${accountsState.user?.age ?: ""}",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.2.sp,
                        color =if(isDarkMode) Color.White else Color(0xff101828)
                    )
                    if (accountsState.user?.isVerified != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            modifier = Modifier.noRippleClickable {
                                if (accountsState.user.isVerified == false){
                                    if (profileScreensState.isVerificationUnderReview){
                                        profileScreenModel.updateVerificationUnderReviewDialogState(true)
                                    }else{
                                        profileScreenModel.updateGetVerifyBadgeDialogState(true)
                                    }
                                }else{
                                    profileScreenModel.updateVerificationSuccessDialog(true)
                                }
                            },
                            imageVector = vectorResource(if (accountsState.user.isVerified == true) Res.drawable.ic_verified else Res.drawable.ic_unverified_gray),
                            contentDescription = null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.height(48.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedTextButton(
                    text = stringResource(if (accountsState.user?.progress?.toFloat() == 100f) Res.string.edit_profile else Res.string.complete_profile),
                    onClick = navigateToEditProfileScreen,
                    isDarkMode = isDarkMode
                )
                Spacer(modifier = Modifier.width(10.dp))
                RoundedTextButton(
                    text = stringResource(Res.string.profile_visitors),
                    onClick = navigateToProfileVisitorsScreen,
                    isDarkMode = isDarkMode
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(49.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextWithIndicator(
                    text = stringResource(Res.string.plans),
                    onClick = {
                        profileScreenModel.updateCurrentTabIndex(0)
//                        isPlanSelected = true
                    },
                    indicatorColor = Color(if (profileScreensState.currentTabIndex == 0) 0xffF33358 else 0xffEAECF0),
                    textColor = Color(if (profileScreensState.currentTabIndex == 0) 0xffF33358 else 0xff98A2B3),
                    modifier = Modifier.weight(0.5f)
                )
                TextWithIndicator(
                    text = stringResource(Res.string.my_hearts),
                    onClick = {
                        profileScreenModel.updateCurrentTabIndex(1)
                      //  isPlanSelected = false
                    },
                    indicatorColor = Color(if (profileScreensState.currentTabIndex == 1) 0xffF33358 else 0xffEAECF0),
                    textColor = Color(if (profileScreensState.currentTabIndex == 1) 0xffF33358 else 0xff98A2B3),
                    modifier = Modifier.weight(0.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (profileScreensState.currentTabIndex == 0) {
                PlansSection(
                    isDarkMode = isDarkMode,
                    onSpotlightBtnClick = {
                        if(profileScreensState.spotlightData != null && profileScreensState.spotlightData.remainingMinutes > 0){
                            profileScreenModel.updateShowPurchasedSpotlightState(true)
                        }else{
                            profileScreenModel.updateShowSpotlightOptionsState(true)
                        }
                    },
                    screenHeight = screenHeight,
                    onGetPlanClick = {
                        navigateToSubscriptionScreen(SubscriptionType.WEEKLY)
                    },
                    currentLovorisePlan = profileScreensState.subscriptionType,
                    onManagePlan = {
                        if (profileScreensState.subscriptionType == SubscriptionType.WEEKLY || profileScreensState.subscriptionType == SubscriptionType.MONTHLY) {
                            profileScreenModel.updateShowSubscriptionDetailsDialogState(true)
                        }
                    },
                    onUpgradePlan = {
                        navigateToSubscriptionScreen(SubscriptionType.MONTHLY)
                    },
                    onViewPlan = {
                        navigateToSubscriptionScreen(SubscriptionType.MONTHLY)
                    },
                    hearts = profileScreensState.hearts,
                    expires = profileScreensState.subscriptionData?.formatDDMONTHYEAR ?: ""

                )
            }
            if (profileScreensState.currentTabIndex == 1) {
                MyHeartsSection(
                    isDarkMode = isDarkMode,
                    navigateToOnboarding = navigateToOnboarding,
                    navigateToInviteFriendsScreen = navigateToInviteFriendsScreen,
                    navigateToCoinBalanceScreen = navigateToCoinBalanceScreen,
                    navigateToReelsScreen = navigateToReelsScreen,
                    navigateToEditProfileScreen = navigateToEditProfileScreen,
                    navigateToCreateReel = navigateToCreateReel,
                    profileScreensState = profileScreensState,
                    accountsState = accountsState
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

        }

//        if (accountsState.user?.name.isNullOrBlank()) {
//            Box(Modifier.fillMaxSize().noRippleClickable { })
//        }

    }

    if (profileScreensState.isLoading){
        CircularLoader()
    }

    if (profileScreensState.showSpotlightOptions) {
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                profileScreenModel.updateShowSpotlightOptionsState(false)
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            SpotlightsSheetContent(100, 1000, onCancelClick = {
                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        profileScreenModel.updateShowSpotlightOptionsState(false)
                    }
                }
            }, changeSystemNavColor = {
                //   setSystemNavigationColor(0xffffffff)
            }, isDarkMode = isDarkMode, screenWidth = screenWidth, onSpotlight = {
                profileScreenModel.updatePurchasedSpotlight(it,context){
                    coroutineScope.launch {
                        delay(500L)
                        profileScreenModel.updateToast("")
                        navigateToLovoriseHearts()
                    }
                }
            })
        }
    }

    if (profileScreensState.showContinueSubscriptionDialog) {
        ContinuePremiumSubscriptionDialog(
            onPositive = {
                profileScreenModel.updateShowContinueSubscriptionDialogState(false)
                profileScreensState.subscriptionType
                profileScreenModel.updateSubscriptionType(SubscriptionType.WEEKLY)
            },
            onCancel = {
                profileScreenModel.updateShowContinueSubscriptionDialogState(false)
            },
            isDarkMode = isDarkMode
        )
    }

    if (profileScreensState.showSubscriptionDetailsDialog) {
        PremiumSubscriptionDetailsDialog(
            onPositive = {
                profileScreenModel.updateShowSubscriptionDetailsDialogState(false)
                profileScreenModel.updateShowCancelConfirmationDialogState(true)
            },
            onCancel = {
                profileScreenModel.updateShowSubscriptionDetailsDialogState(false)
            },
            type = profileScreensState.subscriptionType,
            expiresDate = profileScreensState.subscriptionData?.formatDDMONTHYEAR ?: "",
            isDarkMode = isDarkMode
        )
    }

    if (profileScreensState.showCancelConfirmationDialog) {
        PremiumSubscriptionCancelConfirmationDialog(
            onPositive = {
                profileScreenModel.updateShowCancelConfirmationDialogState(false)
               // profileScreenModel.updateSubscriptionType(SubscriptionType.FREE)
            },
            onCancel = {
                profileScreenModel.updateShowCancelConfirmationDialogState(false)
            },
            expiresDate = profileScreensState.subscriptionData?.formatDDMONTHYEAR ?: "",
            isDarkMode = isDarkMode
        )
    }

    if (profileScreensState.showPurchasedSpotlight && profileScreensState.spotlightData != null){

        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = purchasedSpotlightSheetState,
            onDismissRequest = {
                profileScreenModel.updateShowPurchasedSpotlightState(false)
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {

            PurchasedSpotlightBottomSheetContent(
                isDarkMode = isDarkMode,
                spotlightType = profileScreensState.spotlightData.type,
                onCancel = {
                    coroutineScope.launch { purchasedSpotlightSheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            profileScreenModel.updateShowPurchasedSpotlightState(false)
                        }
                    }
                },
                getRemainingTime = profileScreenModel::getRemainingTimeFormatted
            )
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

    AnimatedVisibility(
        modifier = Modifier.padding(top = 70.dp).padding(horizontal = 60.dp).height(36.dp),
        visible = profileScreensState.toastMessage.isNotBlank(),
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Toast(text = profileScreensState.toastMessage)

        LaunchedEffect(Unit) {
            delay(2000) // Hide after 2 seconds
            profileScreenModel.updateToast("")
        }

    }


}

@Composable
fun MyHeartsSection(
    isDarkMode: Boolean,
    profileScreensState: ProfileScreensState,
    accountsState: AccountsApiCallState,
    navigateToOnboarding: () -> Unit,
    navigateToInviteFriendsScreen: () -> Unit,
    navigateToCoinBalanceScreen: () -> Unit,
   // navigateToRewardsScreen1: () -> Unit,
    navigateToReelsScreen:()->Unit,
    navigateToEditProfileScreen: () -> Unit,
    navigateToCreateReel:()->Unit
) {

    val context = LocalPlatformContext.current

    Column {
        GetCoinsCard(
            onGetCoinsClick = navigateToCoinBalanceScreen,
            coins = profileScreensState.hearts.toInt()
        )


        Spacer(modifier = Modifier.height(8.dp))

        //Share & Rate
//        MyCoinsTaskCard(
//            isDarkMode = isDarkMode,
//            task = GetCoinTask(
//                taskTitle = stringResource(Res.string.share),
//                individualTasks = listOf(
//                    IndividualTask(task = stringResource(Res.string.rate_app), coins = 200, buttonInfo = TextButtonInfo(text= stringResource(Res.string.rate), onClick = { rateApp(context, onSuccess = {}, onFailed = {}) }), taskProgress = null)
//                ),
//
//            )
//        )


        Spacer(modifier = Modifier.height(8.dp))

        // Onboarding
        if (!profileScreensState.isOnboardingDataSent){
            MyCoinsTaskCard(
                isDarkMode = isDarkMode,
                task = GetCoinTask(
                    taskTitle = stringResource(Res.string.onboarding),
                    individualTasks = listOf(
                        IndividualTask(task = stringResource(Res.string.how_did_you_hear_about_us), coins = 50, buttonInfo = TextButtonInfo(text=
                        stringResource(Res.string.inform), onClick = {
                            navigateToOnboarding()
                        }), taskProgress = null),
                    )
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }



        // Unlimited
        MyCoinsTaskCard(
            isDarkMode = isDarkMode,
            task = GetCoinTask(
                taskTitle = stringResource(Res.string.unlimited),
                individualTasks = listOf(
                    IndividualTask(task = stringResource(Res.string.invite_friends), coins = 500, buttonInfo = TextButtonInfo(text=
                    stringResource(Res.string.invite), onClick = {
                        navigateToInviteFriendsScreen()
                    }), taskProgress = null),

//                    IndividualTask(
//                        task = stringResource(Res.string.daily_login_rewards),
//                        coins = 100,
//                        buttonInfo = TextButtonInfo(text= stringResource(Res.string.claim), onClick = navigateToRewardsScreen, isEnabled = !profileScreensState.dailyLoginRewardCollected ),
//                        taskProgress = null
//                    )
                )
            )
        )


        Spacer(modifier = Modifier.height(8.dp))

        // Profile
        if (accountsState.user?.bio.isNullOrBlank() || accountsState.user?.isVerified != true || ((accountsState.user.medias?.size ?: 0) < 6)){
            MyCoinsTaskCard(
                isDarkMode = isDarkMode,
                task = GetCoinTask(
                    taskTitle = stringResource(Res.string.profile),
                    individualTasks = mutableListOf<IndividualTask>().apply {

                        val totalProfiles = accountsState.user?.medias?.size ?: 0
                        if (totalProfiles < 6){
                            add(IndividualTask(
                                task = stringResource(Res.string.upload_photos),
                                coins = 100,
                                buttonInfo = TextButtonInfo(
                                    text = stringResource(Res.string.upload),
                                    onClick = navigateToEditProfileScreen
                                ),
                                taskProgress = TaskProgress(completed = totalProfiles, total = 6))
                            )
                        }

                        if (accountsState.user?.bio.isNullOrBlank()){
                            add(IndividualTask(
                                task = stringResource(Res.string.write_a_bio),
                                coins = 50,
                                buttonInfo = TextButtonInfo(
                                    text = stringResource(Res.string.add),
                                    onClick = navigateToEditProfileScreen
                                ),
                                taskProgress = null
                            ))
                        }

                        if (accountsState.user?.isVerified != true) {
                            add(IndividualTask(
                                task = stringResource(Res.string.verify_identity),
                                coins = 1000,
                                buttonInfo = TextButtonInfo(
                                    text = stringResource(Res.string.verify),
                                    onClick = {}),
                                taskProgress = null
                            ))
                        }

                        val totalInterests = accountsState.user?.interests?.values?.filterNotNull()?.flatten()?.size ?: 0
                        if (totalInterests < 10) {
                            add(IndividualTask(
                                task = stringResource(Res.string.add_interests),
                                coins = 50,
                                buttonInfo = TextButtonInfo(
                                    text = stringResource(Res.string.add),
                                    onClick = navigateToEditProfileScreen
                                ),
                                taskProgress = TaskProgress(completed = totalInterests, total = 10)
                            ))
                        }

                    }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }






        //Post
        MyCoinsTaskCard(
            isDarkMode = isDarkMode,
            task = GetCoinTask(
                taskTitle = stringResource(Res.string.post),
                individualTasks = listOf(
                    IndividualTask(task = stringResource(Res.string.create_reels), coins = 100, buttonInfo = TextButtonInfo(text=
                    stringResource(Res.string.create), onClick = navigateToCreateReel), taskProgress = TaskProgress(completed = 1, total = 5)
                    ),

                    IndividualTask(task = stringResource(Res.string.share_reels), coins = 100, buttonInfo = TextButtonInfo(text=
                    stringResource(Res.string.share), onClick = navigateToReelsScreen), taskProgress = TaskProgress(completed = 0, total = 5)
                    )
                )
            ),
           // modifier = Modifier.navigationBarsPadding()
        )
//        Spacer(modifier = Modifier.height(50.dp).windowInsetsBottomHeight(WindowInsets.navigationBars))
    }








}


@Composable
fun PlansSection(
    isDarkMode: Boolean,
    hearts:Long,
    onSpotlightBtnClick: () -> Unit,
    screenHeight: Dp,
    onGetPlanClick : ()->Unit,
    onUpgradePlan: () ->Unit,
    onViewPlan: () -> Unit,
    onManagePlan:() ->Unit,
    currentLovorisePlan: SubscriptionType,
    expires:String
   // screenWidth:Dp
) {

    SpotlightsButton(
        onClick = onSpotlightBtnClick,
        text = stringResource(Res.string.spotlights),
        hearts = hearts,
        height = if (screenHeight <= 540.dp) 60.dp else 80.dp,
        isDarkMode = isDarkMode
    )

    Spacer(modifier = Modifier.height(16.dp))

    PremiumCardSection(
        onGetPlanClick = onGetPlanClick,
        currentLovorisePlan = currentLovorisePlan,
        onUpgradePlan = onUpgradePlan,
        onManagePlan = onManagePlan,
        onViewPlan = onViewPlan,
        expires = expires
    )
}

//    if (screenHeight < 705) {
//        Spacer(
//            modifier = Modifier.height(55.dp).windowInsetsBottomHeight(WindowInsets.navigationBars)
//        )
//    }
