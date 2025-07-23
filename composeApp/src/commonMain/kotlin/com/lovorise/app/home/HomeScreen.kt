package com.lovorise.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.daily_login_rewards
import com.lovorise.app.GpsProviderInstance
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.onboarding.OnboardingScreen
import com.lovorise.app.accounts.presentation.signup.location.EnableGpsDialog
import com.lovorise.app.bottom_bar.BottomNavBar
import com.lovorise.app.chat.presentation.ChatScreen
import com.lovorise.app.chat.presentation.ChatScreenModel
import com.lovorise.app.closeApp
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.RewardsOverlay
import com.lovorise.app.libs.location.LocationData
import com.lovorise.app.libs.location.LocationManager
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.profile.presentation.ProfileScreen
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.edit_profile.hideWithCompletion
import com.lovorise.app.rate_app.presentation.RateAppBottomSheetContent
import com.lovorise.app.rate_app.presentation.RateAppScreen
import com.lovorise.app.reels.presentation.reels_create_upload_view.screens.ReelsScreen
import com.lovorise.app.reels.presentation.viewModels.ReelsScreenModel
import com.lovorise.app.settings.presentation.components.ResumeAccountOverlay
import com.lovorise.app.swipe.presentation.SwipeLeftRightScreen
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max


class HomeScreen : Screen{

    @Composable
    override fun Content() {

        val ctx = LocalPlatformContext.current
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val reelsScreenModel = navigator.koinNavigatorScreenModel<ReelsScreenModel>()
        val chatScreenModel = navigator.koinNavigatorScreenModel<ChatScreenModel>()
        val prefs = remember { SharedPrefsImpl(ctx) }
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()
       // val tabsState by tabsScreenModel.state.collectAsState()
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val accountsState by accountsViewModel.state.collectAsState()
//        val coroutineScope = rememberCoroutineScope()
//
//
//        val factory = rememberPermissionsControllerFactory()


       // val locationScreenModel = rememberScreenModel { LocationScreenModel(factory.createPermissionsController()) }

     //   BindEffect(locationScreenModel.controller)

       // val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(true){
            profileScreenModel.getPurchaseInfo(ctx)
        }

        LaunchedEffect(true){
            delay(1500L)
            profileScreenModel.collectDailyLoginReward(ctx)
        }



//        DisposableEffect(lifecycleOwner) {
//            val observer = LifecycleEventObserver { _, event ->
//                when (event) {
//                    Lifecycle.Event.ON_RESUME -> {
//                        coroutineScope.launch {
////                            locationScreenModel.provideOrRequestPermission()
////                            GpsProviderInstance.DEFAULT?.startGpsListener()
////                            if (locationScreenModel.permissionState.value == PermissionState.NotGranted || locationScreenModel.permissionState.value == PermissionState.NotGranted){
////                                locationScreenModel.provideOrRequestPermission()
////                            }
//                        }
//                    }
//                    else ->{}
//                }
//            }
//
//            lifecycleOwner.lifecycle.addObserver(observer)
//
//            onDispose {
//                GpsProviderInstance.DEFAULT?.stopGpsListener()
//                lifecycleOwner.lifecycle.removeObserver(observer)
//            }
//        }


        LaunchedEffect(true){
            if (accountsState.user == null) {
                accountsViewModel.getUser(ctx) {
                    accountsViewModel.logout(ctx){
                        navigator.push(OnboardingScreen())
                        navigator.push(OnboardingScreen())
                        navigator.pop()
                    }
                }
                if (reelsScreenModel.state.value.reelsForYou?.reels.isNullOrEmpty()) {
                    withContext(Dispatchers.IO) {
                        reelsScreenModel.getReelsForYou(ctx, 1)
                        reelsScreenModel.getReelsFromMatches(ctx, 1)
                    }
                }
            }

            if (accountsState.language.isNullOrBlank()){
                accountsViewModel.getAppLanguage(ctx)
            }
            if (accountsState.appSettingsData == null){
                accountsViewModel.getAppSettings(ctx)
            }
        }


        HomeScreenContent(
            isDarkMode = isDarkMode,
            screenModel = tabsScreenModel,
            onBack = {
                closeApp(ctx)
            },
            navigateToRateScreen = {
                navigator.push(RateAppScreen(prefs))
            },
            prefs = prefs,
            accountsViewModel = accountsViewModel,
            reelsScreenModel = reelsScreenModel,
            profileScreenModel = profileScreenModel,
            accountsState = accountsState,
         //   locationScreenModel = locationScreenModel,
            chatScreenModel = chatScreenModel
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class)
@Composable
fun HomeScreenContent(isDarkMode:Boolean, onBack:()->Unit, screenModel: TabsScreenModel, navigateToRateScreen:()->Unit, prefs: SharedPrefs?, accountsViewModel: AccountsViewModel, reelsScreenModel: ReelsScreenModel,profileScreenModel: ProfileScreenModel,accountsState:AccountsApiCallState,chatScreenModel: ChatScreenModel) {



    val state by screenModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.currentOrThrow

    var showRatingDialog by remember{ mutableStateOf(false) }
    val ratingSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()


    val isAccountPaused by derivedStateOf {  accountsState.user?.status == "PAUSED" }
    val showResumeOverlay by derivedStateOf { isAccountPaused && state.activeTab in listOf(TabsScreenModel.BottomTab.SWIPE,TabsScreenModel.BottomTab.EVENTS,TabsScreenModel.BottomTab.REELS) }
    val reelsScreenState by reelsScreenModel.reelsScreenState.collectAsState()
    val reelsState by reelsScreenModel.state.collectAsState()

   // val locationPermissionState by locationScreenModel.permissionState.collectAsState()

   // val showLocationPermissionDialog by rememberSaveable(locationPermissionState){ mutableStateOf(locationPermissionState == PermissionState.DeniedAlways) }
    var showEnableGpsPrompt by rememberSaveable { mutableStateOf(false) }

    val ctx = LocalPlatformContext.current
//    val currentGpsStateFlow = GpsProviderInstance.DEFAULT?.currentGpsState
    val profileScreenState by profileScreenModel.state.collectAsState()
    var gpsState by rememberSaveable { mutableStateOf(false) }

//    LaunchedEffect(currentGpsStateFlow){
//        currentGpsStateFlow?.collect{
//            gpsState = it
//            showEnableGpsPrompt = it == false
//            println("the current gps state is $it")
//        }
//    }

    LaunchedEffect(state.activeTab){
        println("the current active tab: ${state.activeTab}")
    }

//    LaunchedEffect(accountsState.currentLocation){
//        if (accountsState.currentLocation.isNull()){
//            screenModel.updateLocation(locationPermissionState,ctx){
//                showEnableGpsPrompt = true
//            }
//        }
//    }

//    LaunchedEffect(locationPermissionState){
//
//        screenModel.updateLocation(locationPermissionState,ctx){
//            showEnableGpsPrompt = true
//        }
////        if (locationPermissionState == PermissionState.Granted && accountsState.currentLocation == null){
////
////            val fetchLocation : suspend ()->Unit = {
////                val locationManager = LocationManager(ctx)
////                val currentLocation = locationManager.getCurrentLocation()
////                println("the current location is $currentLocation")
////                if (currentLocation != null) {
////                    accountsViewModel.updateCurrentLocation(currentLocation)
////                }
////            }
////
////            if (GpsProviderInstance.DEFAULT?.isGpsEnabled() == true){
////                fetchLocation()
////            }else{
////                showEnableGpsPrompt = true
////            }
////
////
////        }
//    }

//    LaunchedEffect(true){
//        delay(3000L)
//        val value = prefs?.getBoolean(PreferencesKeys.IS_RATING_DIALOG_PRESENTED,false) ?: false
//        showRatingDialog = !value
//    }

    BackHandler(true){
        if (state.navStack.size == 1){
            onBack()
        }else{
            screenModel.updateStack(stack = state.navStack.toMutableList().apply { removeAt(state.navStack.lastIndex) })
        }
    }

    var screenHeight by remember { mutableStateOf(0.dp) }


  //  val navigator = LocalNavigator.currentOrThrow

//    LaunchedEffect(state.activeNavIndex){
//        when (state.activeNavIndex) {
//
//            0 -> {navigator.push(VisitorScreen())}
//            4 -> {navigator.push(ProfileScreen())}
//
//        }
//    }

    Box{
        Column(Modifier.fillMaxSize().background(if (isDarkMode) BASE_DARK else Color.White)) {

            Spacer(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .fillMaxWidth()
            )
            if (state.activeTab != TabsScreenModel.BottomTab.REELS && state.activeTab != TabsScreenModel.BottomTab.CHAT && state.activeTab != TabsScreenModel.BottomTab.SWIPE){
                ConnectivityToast()
            }


            Box(Modifier.fillMaxSize().weight(1f)) {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    if (screenHeight == 0.dp){
                        screenHeight = maxHeight
                    }
                    when (state.activeTab) {

                        TabsScreenModel.BottomTab.SWIPE -> SwipeLeftRightScreen(navigator,accountsViewModel, isAccountPaused = showResumeOverlay, blurProfile = !gpsState).Content()

                        TabsScreenModel.BottomTab.REELS -> ReelsScreen(isAccountPaused = isAccountPaused, navigator = navigator, reelsScreenModel = reelsScreenModel, tabsScreenModel = screenModel, reelsScreenState = reelsScreenState, accountsViewModel = accountsViewModel, accountsState = accountsState).Content()

                        TabsScreenModel.BottomTab.CHAT -> ChatScreen(navigator, profileScreenModel = profileScreenModel, chatScreenModel = chatScreenModel, tabsScreenModel = screenModel).Content()

                        TabsScreenModel.BottomTab.PROFILE -> ProfileScreen(
                            navigator,
                            onBottomNavStateChange = { screenModel.updateTab(it) },
                            screenHeight = maxHeight,
                            accountsViewModel = accountsViewModel,
                            profileScreenModel = profileScreenModel
                        ).Content()

                        else -> {

                        }

                    }
                }
                if (showResumeOverlay && !(state.activeTab == TabsScreenModel.BottomTab.REELS && (reelsState.reelsForYou?.reels ?: emptyList()).isEmpty())){
                    ResumeAccountOverlay(
                        onResume = {accountsViewModel.resumeAccount(ctx)},
                        isLoading = accountsState.isLoading
                    )
                }
            }

            Column {
                BottomNavBar(
                    onClick = screenModel::updateTab,
                    activeTab = state.activeTab,
                    height = if (screenHeight < 720.dp) 48.dp else 56.dp,
                    isSpotlightEnabled = profileScreenState.isProfileSpotlight,
                    profileUrl = accountsState.user?.medias?.firstOrNull()?.url ?: "",
                    isDarkMode = isDarkMode,
                    progress = profileScreenState.spotlightData?.let { it.remainingMinutes.toFloat() / max(1,it.totalMinutes) } ?: 0f,
                    spotlightType = profileScreenState.spotlightData?.type
                )
                Spacer(
                    modifier = Modifier
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        .fillMaxWidth()
                        .background(if (isDarkMode) BASE_DARK else Color.White)
                )
            }
        }

//        if (showLocationPermissionDialog) {
//            LocationPermissionDialog(openSettings = locationScreenModel::openSettings)
//        }
        if (showEnableGpsPrompt){
            EnableGpsDialog(onEnable = {
                val fetchLocation : suspend ()->Unit = {
                    val locationManager = LocationManager(ctx)
                    locationManager.getCurrentLocation().collectLatest {
                        it?.let {accountsViewModel.updateCurrentLocation(it)}
                    }
                }
                val provider = GpsProviderInstance.DEFAULT
                if (provider?.isGpsEnabled() == true){
                    showEnableGpsPrompt = false
                }else{
                    provider?.promptEnableGPS {
                        if (it){
                            scope.launch {
                                fetchLocation()
                            }
                        }else{
                            showEnableGpsPrompt = true
                        }
                    }
                    showEnableGpsPrompt = false
                }
            }, isDarkMode = isDarkMode)
        }
    }

    if (profileScreenState.showRewardsOverlay){
        RewardsOverlay(hearts = 100, message = stringResource(Res.string.daily_login_rewards), onClick = {
            profileScreenModel.updateShowRewardsOverlayState(false)
        })
    }



    if (showRatingDialog) {
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = ratingSheetState,
            onDismissRequest = {
                showRatingDialog = false
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            RateAppBottomSheetContent(
                isDarkMode = isDarkMode,
                onCancel = {
                    ratingSheetState.hideWithCompletion(scope){
                        showRatingDialog = false
                    }
                },
                navigateToRateAppScreen = {
                    ratingSheetState.hideWithCompletion(scope){
                        showRatingDialog = false
                        navigateToRateScreen()
                    }
                }
            )

        }
    }





}

fun LocationData?.isNull() : Boolean{
    return (this == null || this.city.isNullOrBlank() || this.country.isNullOrBlank())
}