package com.lovorise.app.accounts.presentation.signup.location

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.enable_location
import coinui.composeapp.generated.resources.ic_enable_location
import coinui.composeapp.generated.resources.let_us_know_your_location
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.open_settings
import coinui.composeapp.generated.resources.set_your_location_to_see_who_is_nearby
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.AccountsRepo
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions_compose.BindEffect
import com.lovorise.app.libs.permissions_compose.rememberPermissionsControllerFactory
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject

class LocationScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current


        val accountsRepo = koinInject<AccountsRepo>()
        val accountsViewModel = rememberScreenModel { AccountsViewModel(accountsRepo) }
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()


        LaunchedEffect(true){
            accountsViewModel.setSignupPage(SignupFlowPages.LOCATION_SCREEN,ctx)
        }

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }
        var navigateToProfile by rememberSaveable{ mutableStateOf(true) }

        BindEffect(controller)



        val viewModel = rememberScreenModel { LocationScreenModel(controller) }

        LocationScreenContent(
            isDarkMode = isDarkMode,
            onNext = {
                if (navigateToProfile) {
                    accountsViewModel.setSignUpCompleted(ctx)
                    tabsScreenModel.updateTab(TabsScreenModel.BottomTab.SWIPE)
                    navigator.push(HomeScreen())
                    navigator.popUntil { it.instanceOf(HomeScreen::class) }
                    navigateToProfile = false
                }
            },
            goBack = {
                navigator.navigateToOnBoarding()
              //  accountsViewModel.resetSignupFlow()
            },
            viewModel = viewModel,
            accountsViewModel = accountsViewModel

        )


    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun LocationScreenContent(
    isDarkMode:Boolean,
    onNext:()->Unit,
    goBack:()->Unit,
    viewModel: LocationScreenModel,
    accountsViewModel: AccountsViewModel
) {

    val coroutineScope = rememberCoroutineScope()

    val accountsState by accountsViewModel.state.collectAsState()

    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
      //  goBack()
    }

    val permissionState by viewModel.permissionState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current



    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    coroutineScope.launch {
                        if (viewModel.isLocationPermissionGranted()){
                            onNext()
                        }
                    }
                }
                else ->{}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    LaunchedEffect(permissionState){
        if (permissionState == PermissionState.Granted){
            onNext()
        }
    }

    Column{

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            Box(
                modifier = Modifier.size(68.44.dp).background(Color(0xffFF7791).copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ){
                Image(
                    imageVector = vectorResource(Res.drawable.ic_enable_location),
                    contentDescription = stringResource(Res.string.enable_location),
                    modifier = Modifier.size(39.82.dp)
                )
            }

            Spacer(Modifier.height(16.dp))


            Text(
                text = stringResource(Res.string.let_us_know_your_location),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.set_your_location_to_see_who_is_nearby),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(16.dp))

            ButtonWithText(
                modifier = Modifier.fillMaxWidth(0.65f),
                text = if (permissionState == PermissionState.Granted) stringResource(Res.string.next) else if (permissionState == PermissionState.DeniedAlways) stringResource(Res.string.open_settings) else stringResource(Res.string.enable_location),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = {
                    if (permissionState == PermissionState.Granted) {
                        onNext()
                    } else{
                        if (permissionState == PermissionState.DeniedAlways) viewModel.openSettings() else  viewModel.provideOrRequestPermission()
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