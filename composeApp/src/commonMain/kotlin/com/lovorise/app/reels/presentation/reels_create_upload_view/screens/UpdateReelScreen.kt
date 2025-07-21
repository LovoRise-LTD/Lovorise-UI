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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_play_button
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.Toast
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.download_manager.downloadFile
import com.lovorise.app.libs.media_player.VideoPlayerView
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.reels.domain.models.ReelContentType
import com.lovorise.app.reels.domain.models.UpdateReelRequest
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelPrivacySetting
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelSettingsBottomSheetContent
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelsOverlayItems
import com.lovorise.app.reels.presentation.viewModels.ReelsScreenModel
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.vectorResource


data class UpdateReelData(
    val reelId:Int,
    val privacySetting:ReelPrivacySetting,
    val caption:String,
    val mediaUrl:String,
    val mediaType:ReelContentType
)

class UpdateReelScreen(private val reelsScreenModel: ReelsScreenModel,val data: UpdateReelData) : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()

        UpdateReelScreenContent(
            isDarkMode = isDarkMode,
            reelsScreenModel = reelsScreenModel,
            data = data,
            onBack = {
                navigator.pop()
            },
            profileScreenModel = profileScreenModel,
            navigateToSubscription = {
                navigator.push(PurchaseSubscriptionScreen(SubscriptionType.WEEKLY))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateReelScreenContent(profileScreenModel: ProfileScreenModel,navigateToSubscription:()->Unit,isDarkMode:Boolean,reelsScreenModel: ReelsScreenModel,onBack:()->Unit,data: UpdateReelData) {

    val density = LocalDensity.current
    val bottomInsets = WindowInsets.ime.getBottom(density)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalPlatformContext.current
    var showPrivacyBottomSheet by remember { mutableStateOf(false) }

    val reelsApiState by reelsScreenModel.state.collectAsState()
    var reelsPrivacySettings by remember { mutableStateOf(data.privacySetting) }


    var currentDuration by remember { mutableLongStateOf(0L) }
    var totalDuration by remember { mutableLongStateOf(1L) }

    val animatedProgressValue by animateFloatAsState(
        targetValue = currentDuration.toFloat()/totalDuration,
        animationSpec = tween(
            durationMillis = if(isAndroid()) 500 else 1000,
            easing = LinearEasing
        )
    )

    val graphicsLayer = rememberGraphicsLayer()
    var isVideoPlaying by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    var caption by remember { mutableStateOf(data.caption) }


    val profileState by profileScreenModel.state.collectAsState()



    var isKeyBoardVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(bottomInsets) {
        isKeyBoardVisible = bottomInsets > 0
    }




    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
                .background(Color.White)
                .fillMaxSize()
                .weight(1f),
        ) {

            Box{
                Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center){

                    if (data.mediaType == ReelContentType.IMAGE){
                        AsyncImage(
                            model = data.mediaUrl,
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
                        if (data.mediaType == ReelContentType.VIDEO) {
                            VideoPlayerView(
                                modifier = Modifier.fillMaxSize().noRippleClickable { isVideoPlaying = !isVideoPlaying },
                                url = data.mediaUrl,
                                thumbnailUrl = "",
                                isVideoPausedInitially = true,
                                isPlaying = isVideoPlaying,
                                onContentDuration = {
                                    totalDuration = it
                                },
                                onCurrentPosition = {
                                    currentDuration = it
                                },
                                clipRange = 0f..100f,
                                enableAdvancedSettings = true
                            )

                            if (!isVideoPlaying) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Image(
                                        imageVector = vectorResource(Res.drawable.ic_play_button),
                                        contentDescription = null,
                                        modifier = Modifier.size(55.dp).clip(CircleShape).noRippleClickable { isVideoPlaying = true }
                                    )
                                }
                            }
                        }
                    }
                }

                ReelsOverlayItems(
                    caption = caption,
                    onCaptionChange = {
                        caption = it
                    },
                    isImeVisible = isKeyBoardVisible,
                    onDoneClick = {
                        if (!reelsApiState.isLoading && (reelsPrivacySettings != data.privacySetting || data.caption.trim() != caption.trim())) {
                            reelsScreenModel.showLoader()
                            reelsScreenModel.updateReel(
                                context = context,
                                data = UpdateReelRequest(
                                    reelId = data.reelId,
                                    privacySetting = reelsPrivacySettings.formatted(),
                                    caption = caption
                                )
                            )
                        }
                    },
                    onSettingsIconClick = {
                        showPrivacyBottomSheet = true
                    },
                    onCancel = {
                        onBack()
                        //showDiscardPostConformationDialog = true
                    },
                    videoFrames = emptyList(),
                    progress = animatedProgressValue,
                    isPlaying = isVideoPlaying,
                    onVideoClipRange = {},
                    isVideo = true,
                    onDownloadClicked = {
                        if (!isDownloading){
                            reelsScreenModel.showLoader()
                            coroutineScope.launch {
                                val fileName = "${Clock.System.now().toEpochMilliseconds()}_My_Reel${if (data.mediaType == ReelContentType.IMAGE) ".png" else ".mp4"}"
                                downloadFile(context, data.mediaUrl, fileName, true,
                                    onSuccess = {
                                        reelsScreenModel.hideLoader()
                                        reelsScreenModel.updateToast("Downloading success")
                                    },
                                    onError = {
                                        reelsScreenModel.hideLoader()
                                        reelsScreenModel.updateToast("Downloading failed")
                                    }
                                )
                                isDownloading = false
                            }
                        }
                    },
                    hideMutedState = true,
                    isVideoMuted = true,
                    onToggleMutedState = {  }
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

    if (reelsApiState.isLoading){
        CircularLoader()
    }


    if (showPrivacyBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                showPrivacyBottomSheet = false
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ReelSettingsBottomSheetContent(
                isDarkMode = isDarkMode,
                privacySettings = reelsPrivacySettings,
                onDone = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        showPrivacyBottomSheet = false
                    }
                },
                onPrivacySettingsChanged = {
                    reelsPrivacySettings = it
                },
                isLocked = profileState.subscriptionType == SubscriptionType.FREE,
                onLockClick = navigateToSubscription
            )
        }
    }

}