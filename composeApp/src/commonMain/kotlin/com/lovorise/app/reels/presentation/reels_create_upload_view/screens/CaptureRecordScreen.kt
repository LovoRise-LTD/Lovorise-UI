package com.lovorise.app.reels.presentation.reels_create_upload_view.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.AlertMessageDialog
import com.lovorise.app.libs.camera.CameraPreview
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions_compose.BindEffect
import com.lovorise.app.libs.permissions_compose.rememberPermissionsControllerFactory
import com.lovorise.app.reels.presentation.prompt.screens.PromptScreen
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.CaptureRecordOverlayItems
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.CaptureRecordPromptAction
import com.lovorise.app.reels.presentation.states.CaptureRecordScreenState
import com.lovorise.app.reels.presentation.viewModels.CaptureRecordScreenModel
import com.lovorise.app.reels.presentation.viewModels.ReelsScreenModel
import com.lovorise.app.ui.ThemeViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid

class CaptureRecordScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val reelsScreenModel = navigator.koinNavigatorScreenModel<ReelsScreenModel>()


        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        val screenModel = navigator.koinNavigatorScreenModel<CaptureRecordScreenModel>()
        val context = LocalPlatformContext.current


        LaunchedEffect(true){
            screenModel.onInit(controller,context)
            screenModel.checkPermissionStatus()
        }

        val screenState by screenModel.state.collectAsState()

        if (screenState.renderUi) {
            CaptureRecordScreenContent(
                isDarkMode = isDarkMode,
                onBack = {
                    navigator.pop()
                },
                screenModel = screenModel,
                screenState = screenState,
                navigateToMyReels = {
                    navigator.push(MyReelsAndFavouritesScreen())
                },
                navigateToGallery = {
                    navigator.push(ImageVideoPickerScreen())
                },
                navigateToEditScreen = {
                    navigator.push(EditUploadSelectedOrCapturedImageVideoScreen())
                },
                navigateToPromptScreen = {
                    navigator.push(PromptScreen())
                },
                reelsScreenModel = reelsScreenModel
            )
        }else{
            Box(Modifier.fillMaxSize().background(Color.Black))
        }

    }
}


@Composable
fun CaptureRecordScreenContent(isDarkMode:Boolean, onBack:()->Unit, screenModel: CaptureRecordScreenModel, screenState: CaptureRecordScreenState,navigateToMyReels:()->Unit,navigateToGallery:()->Unit,navigateToEditScreen:()->Unit,navigateToPromptScreen:()->Unit,reelsScreenModel: ReelsScreenModel) {

    val ctx = LocalPlatformContext.current

    LaunchedEffect(true){
        if (screenState.galleryPermissionState == PermissionState.Granted) {
            screenModel.retryFetching()
//            CoroutineScope(Dispatchers.IO).launch {
//                screenModel.loadVideosFromGallery(ctx, 0)
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                screenModel.loadImagesFromGallery(ctx, 0)
//            }
        }
    }

    var launchGallery by remember { mutableStateOf(false) }

    LaunchedEffect(screenState.galleryPermissionState){
        if (screenState.galleryPermissionState == PermissionState.Granted && launchGallery){
            screenModel.retryFetching()
//            CoroutineScope(Dispatchers.IO).launch {
//                screenModel.loadVideosFromGallery(ctx, 0)
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                screenModel.loadImagesFromGallery(ctx,0)
//            }
            navigateToGallery()
        }
    }

    LaunchedEffect(screenState.capturedImagePath,screenState.capturedVideoPath){
        if (!screenState.capturedImagePath.isNullOrBlank()  || !screenState.capturedVideoPath.isNullOrBlank()){
            navigateToEditScreen()
        }
    }

    LaunchedEffect(screenState.cameraPermissionState,screenState.microphonePermissionState){
        if (screenState.cameraPermissionState == PermissionState.Granted && screenState.microphonePermissionState != PermissionState.Granted) {
            screenModel.provideOrRequestMicrophonePermission()
        }
        else if (screenState.cameraPermissionState != PermissionState.Granted && screenState.microphonePermissionState == PermissionState.Granted){
            screenModel.provideOrRequestCameraPermission()
        }
        else if (screenState.cameraPermissionState == PermissionState.Granted && screenState.microphonePermissionState == PermissionState.Granted && screenState.galleryPermissionState != PermissionState.Granted){
            screenModel.provideOrRequestGalleryPermission()
        }
    }

    Column {
        Spacer(
            modifier = Modifier
                .background(Color.Black)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(Modifier
                .background(Color.Black)
                .fillMaxSize()
                .weight(1f)
        ) {

            if (screenState.microphonePermissionState != PermissionState.Granted || screenState.cameraPermissionState != PermissionState.Granted) {
                RequestPermissionScreen(
                    onBackIconClick = onBack,
                    requestPermissions = {
                        if (screenState.cameraPermissionState != PermissionState.Granted) {
                            println("requesting the camera permission")
                            screenModel.provideOrRequestCameraPermission()
                        }else{
                            println("requesting the microphone permission")
                            screenModel.provideOrRequestMicrophonePermission()
                        }
                    }
                )
            }
            else {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        captureMode = screenState.captureMode,
                        flashMode = screenState.flashMode,
                        cameraFacingMode = screenState.cameraFacingMode,
                        startRecording = screenState.isRecording,
                        capturePhoto = screenState.captureImage,
                        onRecordingComplete = screenModel::onVideoRecordCompleted,
                        onCaptureComplete = screenModel::onCapturedImage
                    )
                    CaptureRecordOverlayItems(
                        onBackIconClick = onBack,
                        flashMode = screenState.flashMode,
                        onPromptAction ={
                            if (!screenState.isRecording) {
                                println("the isRecording is ${screenState.isRecording} $it")
                                if (it == CaptureRecordPromptAction.PROMPT) {
                                    navigateToPromptScreen()
                                } else {
                                    screenModel.setCaptureRecordPromptAction(it)
                                }
                            }
                        },
                        onCaptureClick = screenModel::captureImage,
                        onToggleCameraFace = screenModel::toggleCameraFacingMode,
                        promptAction = screenState.currentCaptureRecordPromptAction,
                        onToggleFlash = screenModel::toggleFlashMode,
                        onProfileIconClick = navigateToMyReels,
                        onStopRecording = screenModel::stopRecording,
                        isRecording = screenState.isRecording,
                        recordTimer = screenState.timerValue.collectAsState("0:00").value,
                        onStartRecording = screenModel::startRecording,
                        onGalleryIconClick = {
                            if (screenState.galleryPermissionState != PermissionState.Granted) {
                                launchGallery = true
                                screenModel.provideOrRequestGalleryPermission()
                            }else {
                                navigateToGallery()
                            }
                        },
                        imagePath = screenModel.imagePaginationState.allItems?.firstOrNull()?.image ?: screenModel.videoPaginationState.allItems?.firstOrNull()?.thumbnail ?: "",
                     //   imagePath = (if (screenState.galleryImages.isEmpty()) "" else screenState.galleryImages.firstOrNull()?.imagePath).toString(),
                        screenWidth = maxWidth,
                        onRecordingDone = screenModel::stopRecording
                    )
                }
            }



        }


        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(Color.Black)
        )
    }

    PaginatedLazyVerticalGrid(paginationState = screenModel.videoPaginationState, columns = GridCells.Fixed(3)){}
    PaginatedLazyVerticalGrid(paginationState = screenModel.imagePaginationState, columns = GridCells.Fixed(3)){}


    if (screenState.permissionRationalDialog) {
        AlertMessageDialog(title = if (screenState.cameraPermissionState == PermissionState.DeniedAlways || screenState.microphonePermissionState == PermissionState.DeniedAlways) "Camera and microphone permission required." else "Gallery permission required." + "Please grant permission via app settings.",
            message = "",
            onPositiveClick = {
                screenModel.updateRationaleState(false)
                screenModel.openAppSettings()
            },
            onNegativeClick = {
                screenModel.updateRationaleState(false)
            },
            isDarkMode = isDarkMode
        )

    }



}

