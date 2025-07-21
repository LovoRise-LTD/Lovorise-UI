package com.lovorise.app.accounts.presentation.signup.profile_upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_your_best_photos
import coinui.composeapp.generated.resources.camera
import coinui.composeapp.generated.resources.camera_permissions_required
import coinui.composeapp.generated.resources.hold_drag_to_reorder
import coinui.composeapp.generated.resources.ic_info
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.let_add_one_photo_to_start
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.permissions_in_your_settings
import coinui.composeapp.generated.resources.photo
import coinui.composeapp.generated.resources.photo_permissions_required
import coinui.composeapp.generated.resources.please_grant
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.SignupFlowPages
import com.lovorise.app.accounts.presentation.onboarding.OnboardingScreen
import com.lovorise.app.accounts.presentation.signup.email.SignupConfirmExitDialog
import com.lovorise.app.accounts.presentation.signup.guidelines.GuidelinesScreen
import com.lovorise.app.accounts.presentation.signup.privacy.PrivacyScreen
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.AlertMessageDialog
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.ImageSourceOptionDialog
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.ProgressIndicator
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.ReorderableLazyGrid
import com.lovorise.app.accounts.presentation.utils.navigateToOnBoarding
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomLinearProgressIndicator
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions_compose.BindEffect
import com.lovorise.app.libs.permissions_compose.rememberPermissionsControllerFactory
import com.lovorise.app.noRippleClickable
import com.lovorise.app.photo_capture_pick.rememberCameraManager
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class ProfileUploadScreen : Screen{


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }
        val ctx = LocalPlatformContext.current

        BindEffect(controller)

        val viewModel = navigator.koinNavigatorScreenModel<ProfileUploadScreenViewModel>()
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()
        val state by viewModel.state.collectAsState()


        LaunchedEffect(true){
            viewModel.onInit(controller, ctx)
            viewModel.resetPickedMedias()
            if (accountsState.user == null) {
                accountsViewModel.getUser(ctx, navigateToSignIn = {
                    navigator.push(OnboardingScreen())
                    navigator.push(OnboardingScreen())
                    navigator.pop()
                })
            }
            if (!state.isPageStateUpdated) {
                accountsViewModel.setSignupPage(SignupFlowPages.PROFILE_UPLOAD_SCREEN, ctx)
                viewModel.markPageStateUpdated()
            }

            if (accountsState.user?.medias != null) {
                viewModel.loadImages(accountsState.user?.medias)
            }
        }

        LaunchedEffect(accountsState.user?.medias){
            if (accountsState.user?.medias != null) {
                viewModel.loadImages(accountsState.user?.medias)
            }
        }


        val lifecycle = LocalLifecycleOwner.current.lifecycle

        DisposableEffect(lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    println("on disposing!!")
                    viewModel.reorderImages(ctx)
                }
            }

            lifecycle.addObserver(observer)
            onDispose {
                viewModel.reorderImages(ctx)
                lifecycle.removeObserver(observer)
            }
        }

        ProfileUploadScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.navigateToOnBoarding()
              //  accountsViewModel.resetSignupFlow()
            },
            viewModel = viewModel,
            navigateToGuidelinesScreen = {
                navigator.push(PrivacyScreen())
            },
            navigateToGalleryScreen = {
                navigator.push(GalleryAlbumScreen())
            },
            navigateToEditPickedMediaScreen = {
                navigator.push(EditPickedMediaScreen())
            },
            accountsViewModel = accountsViewModel,
            state = state
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class,
    InternalVoyagerApi::class
)
@Composable
fun ProfileUploadScreenContent(
    isDarkMode:Boolean,
    goBack:()->Unit,
    viewModel: ProfileUploadScreenViewModel,
    navigateToGuidelinesScreen:()->Unit,
    navigateToGalleryScreen: ()->Unit,
    accountsViewModel: AccountsViewModel,
    navigateToEditPickedMediaScreen: () -> Unit,
    state: ProfileUploadScreenState
) {

    BackHandler(true){
        accountsViewModel.showExitConfirmationDialog()
       // goBack()
    }


    val context = LocalPlatformContext.current



    val accountsState by accountsViewModel.state.collectAsState()

    LaunchedEffect(accountsState.isUploadConfirmed){
        if (accountsState.isUploadConfirmed){
            navigateToGuidelinesScreen()
        }
    }



    val cameraManager = rememberCameraManager {
        it?.let { viewModel.updateCapturedImage(image = it,context = context, onComplete = {
            navigateToEditPickedMediaScreen()
        }) }
    }


    val sheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true)


    LaunchedEffect(state.cameraPermissionState,state.galleryPermissionState){

        when(state.cameraPermissionState){
            PermissionState.Granted -> {
                if (state.launchCamera) {
                    cameraManager.launch()
                    viewModel.onToggleCamera()
                }
            }
            PermissionState.DeniedAlways -> {
                if (state.launchCamera) {
                    viewModel.onTogglePermissionRationale()
                    viewModel.onToggleCamera()
                }
            }
            PermissionState.Denied , PermissionState.NotGranted ->{
                if (state.launchCamera) {
                    viewModel.onToggleCamera()
                }
            }
            PermissionState.NotDetermined ->{}
        }

        when(state.galleryPermissionState){
            PermissionState.Granted -> {
                if (state.launchGallery) {
                    viewModel.retryFetching()
                    navigateToGalleryScreen()
                    viewModel.onToggleGallery()
                }
            }
            PermissionState.DeniedAlways -> {
                if (state.launchGallery) {
                    viewModel.onTogglePermissionRationale()
                    viewModel.onToggleGallery()
                }
            }
            PermissionState.Denied , PermissionState.NotGranted ->{
                if (state.launchGallery) {
                    viewModel.onToggleGallery()
                }
            }
            PermissionState.NotDetermined ->{}
        }
    }

    LaunchedEffect(state.images){
        viewModel.updateButtonState()
    }


    if (state.permissionRationalDialog) {
        AlertMessageDialog(title = if (state.launchCamera) stringResource(Res.string.camera_permissions_required) else stringResource(Res.string.photo_permissions_required),
            message = "${stringResource(Res.string.please_grant)} ${if (state.launchCamera)  stringResource(Res.string.camera) else stringResource(Res.string.photo)} ${stringResource(Res.string.permissions_in_your_settings)}",
            onPositiveClick = {
             //   if (state.launchCamera) viewModel.onToggleCamera() else viewModel.onToggleGallery()
                if (state.launchGallery) viewModel.onToggleGallery()
                if (state.launchCamera) viewModel.onToggleCamera()
                viewModel.onTogglePermissionRationale()
                viewModel.openSettings()
            },
            onNegativeClick = {
               // if (state.launchCamera) viewModel.onToggleCamera() else viewModel.onToggleGallery()
                if (state.launchGallery) viewModel.onToggleGallery()
                if (state.launchCamera) viewModel.onToggleCamera()
                viewModel.onTogglePermissionRationale()
            },
            isDarkMode = isDarkMode
        )

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
                progress = if (state.isButtonEnabled) 16f/16f else 15f/16f,
                isDarkMode = isDarkMode,
            )


            Spacer(Modifier.height(40.dp))

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(Res.string.add_your_best_photos),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(31.dp))

            if (state.images.isNotEmpty()) {

                Box (
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ){

                    ReorderableLazyGrid(
                        images = state.images,
                        onClickAdd = {
                            viewModel.updateClickedIndex(it)
                            viewModel.onToggleImageSourceOptionDialog()
                        },
                        onEdit = {
                            viewModel.updateClickedIndex(it)
                            viewModel.onToggleImageSourceOptionDialog()
                        },
                        onRemove = {
                            viewModel.updateClickedIndex(it)
                            viewModel.onToggleImageSourceOptionDialog()
                        },
                        onMove = { from, to ->
                            viewModel.movePhoto(from, to)
                        },
                        // cropPreviewSize = state.cropSize.toSize(),
                        aspectRatio = state.aspectRatio
                    )

                    if (state.progress < 1f) {
                        ProgressIndicator(state.progress)
                    }
                }
            }
            Spacer(Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){

                Image(
                    imageVector = vectorResource(Res.drawable.ic_info),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = stringResource(Res.string.hold_drag_to_reorder),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

            }

            Spacer(Modifier.weight(1f))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.height(43.dp).width(45.dp).border(width = 2.dp, color = Color(0xffF33358), shape = RoundedCornerShape(100)), contentAlignment = Alignment.Center){
                    Text(
                        text = "${state.images.filter { it.image != null }.size}/6",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 24.sp,
                        color = if(isDarkMode) Color.White else Color(0xff000000),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.width(14.dp))

                Text(
                    text = stringResource(Res.string.let_add_one_photo_to_start),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    color = Color(0xff667085),
                    textAlign = TextAlign.Start
                )

            }

            Spacer(Modifier.height(18.dp))

            ButtonWithText(
                text = stringResource(Res.string.next),
                bgColor = if (state.isButtonEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (state.isButtonEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (!accountsState.isLoading) {
                        if (state.isButtonEnabled) {
//                            navigateToGuidelinesScreen()
                            accountsViewModel.uploadConfirmation(state.images.filter { it.image != null }.mapNotNull { it.imgId })
//                            viewModel.getImageUploadDataForImages { images ->
//                                accountsViewModel.uploadImages(images,context)
//                            }
                        }
                    }
                }
            )

            Spacer(Modifier.height(29.dp))
        }


        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )


    }

//    if (accountsState.isLoading){
//        CircularLoader(center = true)
//    }

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

    PaginatedLazyVerticalGrid(paginationState = viewModel.paginationState, columns = GridCells.Fixed(3)){}


    if (state.imageSourceOptionDialog){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp,0.dp,0.dp,0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = viewModel::onToggleImageSourceOptionDialog,
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ){

            ImageSourceOptionDialog(onGalleryRequest = {
                viewModel.onToggleImageSourceOptionDialog()
                viewModel.launchGallery(navigateToGalleryScreen)
            }, onCameraRequest = {
                viewModel.onToggleImageSourceOptionDialog()
                viewModel.launchCamera { cameraManager.launch() }
            }, isDarkMode = isDarkMode,
                showDeleteOption = state.clickedIndex > 0 && state.images[state.clickedIndex].image != null,
                onDeleteRequest = {
                    viewModel.onToggleImageSourceOptionDialog()
                    viewModel.deleteItem(context)
                },
                isUpdate = state.images.getOrNull(state.clickedIndex)?.image != null
            )


        }
    }


}



