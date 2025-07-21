package com.lovorise.app.accounts.presentation.signup.notification

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.accept
import coinui.composeapp.generated.resources.don_t_miss_a_beat
import coinui.composeapp.generated.resources.ic_notification_bell
import coinui.composeapp.generated.resources.not_now
import coinui.composeapp.generated.resources.turn_on_notification
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.guidelines.GuidelinesScreen
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions_compose.BindEffect
import com.lovorise.app.libs.permissions_compose.rememberPermissionsControllerFactory
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class NotificationPermissionScreen : Screen{


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val ctx = LocalPlatformContext.current

        val accountsViewModel = koinScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.setSignupPage(SignupFlowPages.NOTIFICATION_PERMISSION_SCREEN,ctx)
        }

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }


        BindEffect(controller)
        val viewModel = rememberScreenModel { NotificationPermissionScreenModel(controller) }


        NotificationPermissionScreenContent(
            isDarkMode = isDarkMode,
            onNext = {
                navigator.push(GuidelinesScreen())
            },
            goBack = {
                navigator.navigateToOnBoarding()
             //   accountsViewModel.resetSignupFlow()
            },
            accountsViewModel = accountsViewModel,
            viewModel = viewModel

        )
    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun NotificationPermissionScreenContent(
    isDarkMode:Boolean,
    onNext:()->Unit,
    goBack:()->Unit,
    accountsViewModel: AccountsViewModel,
    viewModel:NotificationPermissionScreenModel
) {

    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
        //goBack()
    }

    val coroutineScope = rememberCoroutineScope()


    val permissionState by viewModel.permissionState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    coroutineScope.launch {
                        if (viewModel.isNotificationPermissionGranted()){
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

    val accountsState by accountsViewModel.state.collectAsState()

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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(Modifier.weight(1.39f))

            Image(
                imageVector = vectorResource(Res.drawable.ic_notification_bell),
                contentDescription = null,
                modifier = Modifier.size(52.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.don_t_miss_a_beat),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(21.dp))

            Text(
                text = stringResource(Res.string.turn_on_notification),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )


            Spacer(Modifier.weight(1f))

            ButtonWithText(
                text = stringResource(Res.string.accept),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = {
                    viewModel.provideOrRequestPermission()
                }
            )
            Spacer(Modifier.height(18.dp))

            Text(
                modifier = Modifier.noRippleClickable(onNext),
                text = stringResource(Res.string.not_now),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
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


