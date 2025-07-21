package com.lovorise.app.reels.presentation.reels_create_upload_view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_pause_button
import coinui.composeapp.generated.resources.ic_play_button
import coinui.composeapp.generated.resources.reel_posted_successfully
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.Toast
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.cropper.saveBitmapToCache
import com.lovorise.app.libs.download_manager.downloadFile
import com.lovorise.app.libs.media_player.VideoPlayerView
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.reels.domain.models.CreateReelRequest
import com.lovorise.app.reels.domain.models.ReelContentType
import com.lovorise.app.reels.domain.models.ReelSignedUrlRequest
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.DiscardPostConfirmationDialog
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelSettingsBottomSheetContent
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelsOverlayItems
import com.lovorise.app.reels.presentation.states.CaptureRecordScreenState
import com.lovorise.app.reels.presentation.states.ReelsApiState
import com.lovorise.app.reels.presentation.viewModels.CaptureRecordScreenModel
import com.lovorise.app.reels.presentation.viewModels.ReelsScreenModel
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class EditUploadSelectedOrCapturedImageVideoScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val reelsScreenModel = navigator.koinNavigatorScreenModel<ReelsScreenModel>()
        val screenModel = navigator.koinNavigatorScreenModel<CaptureRecordScreenModel>()

        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()



        val state by screenModel.state.collectAsState()

        LaunchedEffect(true){
            reelsScreenModel.hideLoader()
        }

        EditUploadSelectedOrCapturedImageVideoScreenContent(
            onBack = {
                screenModel.resetCaptureAndPickedContent()
                navigator.pop()
            },
            isDarkMode = isDarkMode,
            screenModel = screenModel,
            state = state,
            reelsScreenModel = reelsScreenModel,
            navigateToMyReels = {
               // navigator.pop()
                navigator.popUntil {
                    it.instanceOf(EditUploadSelectedOrCapturedImageVideoScreen::class)
                }
                screenModel.resetCaptureAndPickedContent()
                navigator.push(MyReelsAndFavouritesScreen())
            },
            navigateToSubscription = {
                navigator.push(PurchaseSubscriptionScreen(SubscriptionType.WEEKLY))
            },
            profileScreenModel = profileScreenModel
        )
    }
}

@OptIn(InternalVoyagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditUploadSelectedOrCapturedImageVideoScreenContent(profileScreenModel: ProfileScreenModel,navigateToSubscription:()->Unit,isDarkMode:Boolean, state: CaptureRecordScreenState, screenModel: CaptureRecordScreenModel, onBack:()->Unit,reelsScreenModel: ReelsScreenModel,navigateToMyReels:()->Unit) {

    val density = LocalDensity.current
    val bottomInsets = WindowInsets.ime.getBottom(density)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalPlatformContext.current

    val reelsApiState by reelsScreenModel.state.collectAsState()
    val reelPostedMessage = stringResource(Res.string.reel_posted_successfully)

    var showDiscardPostConformationDialog by rememberSaveable { mutableStateOf(false) }

    var currentDuration by remember { mutableLongStateOf(0L) }
    var isDownloading by remember { mutableStateOf(false) }
    var totalDuration by remember { mutableLongStateOf(1L) }

    val animatedProgressValue by animateFloatAsState(
        targetValue = currentDuration.toFloat()/totalDuration,
        animationSpec = tween(
            durationMillis = if(isAndroid()) 500 else 1000,
            easing = LinearEasing
        )
    )

    LaunchedEffect(animatedProgressValue){
        println("the current duration is ")
        println("the animated progress value from edit screen $animatedProgressValue")
    }

    val graphicsLayer = rememberGraphicsLayer()




    var isKeyBoardVisible by remember {
       mutableStateOf(false)
    }

    LaunchedEffect(bottomInsets) {
        isKeyBoardVisible = bottomInsets > 0
    }


    LaunchedEffect(true){
        if (state.selectedVideoIndex != -1 || !state.capturedVideoPath.isNullOrBlank()) {
            val path = if (!state.capturedVideoPath.isNullOrBlank()) state.capturedVideoPath else screenModel.videoPaginationState.allItems?.get(state.selectedVideoIndex)?.videoUri ?: ""

            //val uri  = if (!state.capturedVideoPath.isNullOrBlank()) state.capturedVideoPath else state.galleryVideos[state.selectedVideoIndex].videoUri
            if (path.isNotBlank()){
                screenModel.loadVideoFrames(context,path)
            }
        }

    }

    BackHandler(true) {
        showDiscardPostConformationDialog = true
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val profileState by profileScreenModel.state.collectAsState()


    Column(
        modifier = Modifier
            .imePadding()
            .noRippleClickable {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
    ) {

//        Spacer(
//            modifier = Modifier
//                .background(Color.White)
//                .windowInsetsTopHeight(WindowInsets.statusBars)
//                .fillMaxWidth()
//        )

        Column(
            modifier = Modifier
                .background(if (isDarkMode) Color.Black else Color.White)
                .fillMaxSize()
                .weight(1f),
        ) {

            Box{
                Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center){

                    if (state.selectedImageIndex != -1 || !state.capturedImagePath.isNullOrBlank()){
                        AsyncImage(
                            model = if (state.selectedImageIndex != -1) screenModel.imagePaginationState.allItems?.get(state.selectedImageIndex)?.image else state.capturedImagePath,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().background(Color.Transparent).clipToBounds() // Ensure content is clipped to the bounds
                                .drawWithCache {
                                    onDrawWithContent {
                                        // Ensure drawing is clipped to the container
                                        graphicsLayer.record {
                                            clipRect {
                                                this@onDrawWithContent.drawContent()
                                            }
                                        }
                                        drawLayer(graphicsLayer)
                                    }
                                },
                            contentScale = ContentScale.FillWidth
                        )
                    }else{
                        if ( state.selectedVideoIndex != -1 || !state.capturedVideoPath.isNullOrBlank()) {
                            val path = if (!state.capturedVideoPath.isNullOrBlank()) state.capturedVideoPath else screenModel.videoPaginationState.allItems?.get(state.selectedVideoIndex)?.videoUri ?: ""
                            if (path.isNotBlank()) {
                                VideoPlayerView(
                                    modifier = Modifier.fillMaxSize()
                                        .noRippleClickable {
                                            if (state.isVideoPlaying) {
                                                screenModel.updatePauseButtonState(true)
                                            }
                                        },
                                    url = path,
                                    thumbnailUrl = "",
                                    isVideoPausedInitially = true,
                                    isPlaying = state.isVideoPlaying,
                                    onContentDuration = {
                                        totalDuration = it
                                    },
                                    onCurrentPosition = {
                                        currentDuration = it
                                    },
                                    clipRange = state.videoClipRange,
                                    enableAdvancedSettings = true,
                                    isMuted = state.isVideoMuted
                                )


                                if (!state.isVideoPlaying) {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            imageVector = vectorResource(Res.drawable.ic_play_button),
                                            contentDescription = null,
                                            modifier = Modifier.size(55.dp).clip(CircleShape)
                                                .noRippleClickable {
                                                    screenModel.updatePlayingState(true)
                                                }
                                        )
                                    }
                                }

                                Column {
                                    AnimatedVisibility(
//                                    modifier = Modifier.padding(top = 56.dp).padding(horizontal = 60.dp).wrapContentHeight(),
                                        visible = state.showPauseButton,
                                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                                        exit = fadeOut(animationSpec = tween(durationMillis = 300))
                                    ) {

                                        Box(
                                            Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                imageVector = vectorResource(Res.drawable.ic_pause_button),
                                                contentDescription = null,
                                                modifier = Modifier.size(55.dp)
                                                    .clip(CircleShape)
                                                    .noRippleClickable {
                                                        screenModel.updatePlayingState(false); screenModel.updatePauseButtonState(false)
                                                    }
                                            )
                                        }

                                        LaunchedEffect(Unit) {
                                            delay(1000)
                                            //  screenModel.updateToastMessage("")
                                            screenModel.updatePauseButtonState(false)
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                ReelsOverlayItems(
                    caption = state.selectedContentCaption,
                    onCaptionChange = screenModel::onCaptionValueChange,
                    isImeVisible = isKeyBoardVisible,
                    onDoneClick = {
                        if (state.selectedVideoIndex != -1 || !state.capturedVideoPath.isNullOrBlank()) {
                            val duration = totalDuration * (state.videoClipRange.endInclusive - state.videoClipRange.start) / 100
                            if ((duration/(1000*60)) <= 2){
                                reelsScreenModel.showLoader()
                                screenModel.exportClippedVideo(context, onVideoExported = {
                                    coroutineScope.launch {
                                       // val file = getByteArrayFromPath(filePath = it)
                                        //if (file != null) {
//                                            reelsScreenModel.createReel(context, file = it, ReelSignedUrlRequest(ReelContentType.VIDEO,"CroppedVideo_${Clock.System.now().toEpochMilliseconds()}.mp4"), createData = CreateReelRequest(state.reelsPrivacySettings.name.lowercase(), contentId = -1, caption = state.selectedContentCaption), onSuccess = {
//                                                coroutineScope.launch {
//                                                    reelsScreenModel.updateToast(reelPostedMessage)
//                                                    delay(1000L)
//                                                    navigateToMyReels()
//                                                }
//                                            })
                                        reelsScreenModel.updateProcessingReel(ReelsApiState.ProcessingReel(file = it, ReelSignedUrlRequest(ReelContentType.VIDEO,"CroppedVideo_${Clock.System.now().toEpochMilliseconds()}.mp4"), createData = CreateReelRequest(state.reelsPrivacySettings.name.lowercase(), contentId = -1, caption = state.selectedContentCaption)))
                                        navigateToMyReels()
                                       // }
                                    }
                                })
                            }
                        }

                        else if(state.selectedImageIndex != -1 || !state.capturedImagePath.isNullOrBlank()){
                            coroutineScope.launch {
                                reelsScreenModel.showLoader()
                                val path = saveBitmapToCache(graphicsLayer.toImageBitmap(),context)
                               // val file = getByteArrayFromPath(filePath = path)
                               // if (file != null) {
//                                    reelsScreenModel.createReel(context, file = path, ReelSignedUrlRequest(ReelContentType.IMAGE,"CroppedVideo_${Clock.System.now().toEpochMilliseconds()}.png"), createData = CreateReelRequest(state.reelsPrivacySettings.name.lowercase(), contentId = -1, caption = state.selectedContentCaption), onSuccess = {
//                                        coroutineScope.launch {
//                                            reelsScreenModel.updateToast(reelPostedMessage)
//                                            delay(2000L)
//                                            navigateToMyReels()
//                                        }
//                                    })
                                reelsScreenModel.updateProcessingReel(ReelsApiState.ProcessingReel(file = path, ReelSignedUrlRequest(ReelContentType.IMAGE,"CroppedImage_${Clock.System.now().toEpochMilliseconds()}.png"), createData = CreateReelRequest(state.reelsPrivacySettings.name.lowercase(), contentId = -1, caption = state.selectedContentCaption)))
                                navigateToMyReels()
                               // }
                            }
                        }
                    },
                    onSettingsIconClick = {
                        screenModel.setPrivacyBottomSheetState(!state.showPrivacyBottomSheet)
                    },
                    onCancel = {
                        showDiscardPostConformationDialog = true
                    },
                    videoFrames = state.videoFrames,
                    progress = animatedProgressValue,
                    isPlaying = state.isVideoPlaying,
                    onVideoClipRange = screenModel::updateVideoClipRange,
                    isVideo =state.selectedVideoIndex != -1 || !state.capturedVideoPath.isNullOrBlank(),
                    onDownloadClicked = {
                        if(!isDownloading){
                            reelsScreenModel.showLoader()
                            val filename = Clock.System.now().toEpochMilliseconds().toString() + if (state.selectedImageIndex != -1 || !state.capturedImagePath.isNullOrBlank()) "_My_Reel.png" else "_My_Reel.mp4"

                            if (state.selectedVideoIndex != -1 || !state.capturedVideoPath.isNullOrBlank()) {
                                screenModel.exportClippedVideo(context, onVideoExported = {
                                    coroutineScope.launch {
                                        downloadFile(
                                            context,
                                            it,
                                            fileName = filename,
                                            false,
                                            onSuccess = {
                                                reelsScreenModel.hideLoader()
                                                reelsScreenModel.updateToast("Downloading success")
                                            },
                                            onError = {
                                                reelsScreenModel.hideLoader()
                                                reelsScreenModel.updateToast("Downloading failed")
                                            }
                                        )
                                    }
                                })
                            }

                            else if(state.selectedImageIndex != -1 || !state.capturedImagePath.isNullOrBlank()){
                                coroutineScope.launch {
                                    val path = saveBitmapToCache(graphicsLayer.toImageBitmap(),context)
                                    downloadFile(
                                        context,
                                        path,
                                        fileName = filename,
                                        false,
                                        onSuccess = {
                                            reelsScreenModel.hideLoader()
                                            reelsScreenModel.updateToast("Downloading success")
                                        },
                                        onError = {
                                            reelsScreenModel.hideLoader()
                                            reelsScreenModel.updateToast("Downloading failed")
                                        }
                                    )
                                }
                            }
                        }
                    },
                    isVideoMuted = state.isVideoMuted,
                    onToggleMutedState = screenModel::toggleVideoMutedState
                )
            }
        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )
    }


    AnimatedVisibility(
        modifier = Modifier.padding(top = 56.dp).padding(horizontal = 60.dp).wrapContentHeight(),
        visible = reelsApiState.toastMessage.isNotBlank(),
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Toast(text = reelsApiState.toastMessage)

        LaunchedEffect(Unit) {
            delay(1000)
          //  screenModel.updateToastMessage("")
            reelsScreenModel.updateToast("")
        }

    }

    if (state.isLoading || reelsApiState.isLoading){
        CircularLoader()
    }


    if (state.showPrivacyBottomSheet){
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                screenModel.setPrivacyBottomSheetState(false)
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ReelSettingsBottomSheetContent(
                isDarkMode = isDarkMode,
                privacySettings = state.reelsPrivacySettings,
                onDone = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        screenModel.setPrivacyBottomSheetState(false)
                    }
                },
                onPrivacySettingsChanged = screenModel::setReelsPrivacy,
                isLocked = profileState.subscriptionType == SubscriptionType.FREE,
                onLockClick = navigateToSubscription
            )
        }
    }

    if (showDiscardPostConformationDialog){
        DiscardPostConfirmationDialog(
            onCancel = {
                showDiscardPostConformationDialog = false
            },
            onDiscard = {
                showDiscardPostConformationDialog = false
                onBack()
            }
        )
    }



}





