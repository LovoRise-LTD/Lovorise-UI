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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_to_favorites
import coinui.composeapp.generated.resources.for_you
import coinui.composeapp.generated.resources.ic_bookmark_filled
import coinui.composeapp.generated.resources.ic_bookmark_outlined
import coinui.composeapp.generated.resources.ic_camera_outlined
import coinui.composeapp.generated.resources.ic_dot_menu
import coinui.composeapp.generated.resources.ic_filter_reels
import coinui.composeapp.generated.resources.ic_muted
import coinui.composeapp.generated.resources.ic_nav_3
import coinui.composeapp.generated.resources.ic_pause_button
import coinui.composeapp.generated.resources.ic_play_button
import coinui.composeapp.generated.resources.ic_seek_10sec_back
import coinui.composeapp.generated.resources.ic_seek_10sec_forward
import coinui.composeapp.generated.resources.ic_share_outlined
import coinui.composeapp.generated.resources.ic_spotlight_message
import coinui.composeapp.generated.resources.ic_unmuted
import coinui.composeapp.generated.resources.matches
import coinui.composeapp.generated.resources.removed_from_favorites
import coinui.composeapp.generated.resources.swipe_up_for_more
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.NoInternet
import com.lovorise.app.components.Toast
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.media_player.VideoPlayerView
import com.lovorise.app.libs.media_player.preloadVideoListInBackground
import com.lovorise.app.noRippleClickable
import com.lovorise.app.reels.domain.models.MarkReelRequest
import com.lovorise.app.reels.domain.models.ReelContentType
import com.lovorise.app.reels.domain.models.ReelStatus
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.NoReelsLeft
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.OriginalAudio
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReportReelBottomSheetContent
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ShareReelBottomSheetContent
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.TextTabRowItem
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.VideoTimeSlider
import com.lovorise.app.reels.presentation.states.ReelsScreenState
import com.lovorise.app.reels.presentation.viewModels.ReelsScreenModel
import com.lovorise.app.filters.FilterScreen
import com.lovorise.app.libs.connectivity.ConnectivityViewModel
import com.lovorise.app.swipe.presentation.components.DescriptionProfile
import com.lovorise.app.ui.ThemeViewModel
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ReelsScreen(private val isAccountPaused:Boolean,private val navigator: Navigator,private val accountsViewModel: AccountsViewModel,private val reelsScreenModel: ReelsScreenModel, private val reelsScreenState: ReelsScreenState, private val tabsScreenModel: TabsScreenModel,private val accountsState:AccountsApiCallState) : Tab{

    @Composable
    override fun Content() {

        val connectivityViewModel = navigator.koinNavigatorScreenModel<ConnectivityViewModel>()
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        ReelsScreenContent(
            isDarkMode = isDarkMode,
            isPaused = isAccountPaused,
            navigateToCreateCaptureScreen = {
                navigator.push(CaptureRecordScreen())
            },
            reelsScreenModel = reelsScreenModel,
            reelsScreenState = reelsScreenState,
            tabsScreenModel = tabsScreenModel,
            navigateToFilterScreen = {
                navigator.push(FilterScreen())
            },
            connectivityViewModel = connectivityViewModel,
//            accountsState = accountsState,
//            accountsViewModel = accountsViewModel
        )

    }

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 2u,
            title = "Reels",
            icon = painterResource(Res.drawable.ic_nav_3)
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReelsScreenContent(
    isDarkMode:Boolean,
    isPaused: Boolean,
    navigateToCreateCaptureScreen:()->Unit,
    navigateToFilterScreen:()->Unit,
    reelsScreenModel: ReelsScreenModel,
    reelsScreenState: ReelsScreenState,
    tabsScreenModel: TabsScreenModel,
    connectivityViewModel: ConnectivityViewModel
//    accountsState:AccountsApiCallState,
//    accountsViewModel: AccountsViewModel
) {
    val reelsState by reelsScreenModel.state.collectAsState()

    val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()


    val reportReelSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val shareReelBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalPlatformContext.current

    var reelId by rememberSaveable { mutableIntStateOf(-1) }


    LaunchedEffect(Unit) {
        reelsScreenModel.loadSwipeUpAnimation()
    }

    val swipeUpComposition =  rememberLottieComposition {
        LottieCompositionSpec.JsonString(reelsScreenState.swipeUpAnimation)
    }

    val reelsForYou =  reelsState.reelsForYou?.reels ?: emptyList()
    val reelsFromMatches = reelsState.reelsFromMatches?.reels ?: emptyList()



    LaunchedEffect(true){
        val reels = reelsForYou.toMutableList().apply { addAll(reelsFromMatches) }.mapNotNull { it?.content?.mediaUrl }
        preloadVideoListInBackground(reels, context = context)
    }

    val animatedProgressValue by animateFloatAsState(
        targetValue = if (reelsScreenState.currentPosition == 0L ||reelsScreenState.totalDuration == 0L) 0f else reelsScreenState.currentPosition/reelsScreenState.totalDuration.toFloat() * 100f,
        animationSpec = tween(
            durationMillis = if(isAndroid()) 500 else 1000,
            easing = LinearEasing
        )
    )







    val horizontalTabState = rememberPagerState{if (reelsFromMatches.isNotEmpty()) 2 else 1}

    val addedToFavorites = stringResource(Res.string.add_to_favorites)
    val removedFromFavorites = stringResource(Res.string.removed_from_favorites)

    LaunchedEffect(reelId){
        if (reelId >= 0) {
            reelsScreenModel.markReel(context, MarkReelRequest(reelId,ReelStatus.WATCHED,""))
        }
    }

    val showCreateReelsScreen = reelsForYou.isEmpty()

   // val isAccountPaused = isPaused && !showCreateReelsScreen


//    LaunchedEffect(true){
//        if (isAccountPaused && reelsForYou.isEmpty()){
//            isAccountPaused = false
//        }
//    }



    val pagerForYouTab = rememberPagerState { reelsForYou.size }
    val pagerForMatchesTab = rememberPagerState { reelsFromMatches.size }


    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerForYouTab.currentPage , pagerForMatchesTab.currentPage) {
        if (reelsScreenState.showSwipeUpAnimation && (pagerForMatchesTab.currentPage > 0 || pagerForYouTab.currentPage > 0)) {
            reelsScreenModel.updateShowSwipeUpAnimationState(false)
        }

    }

    if (!showCreateReelsScreen) {

        HorizontalPager(
            state = horizontalTabState,
            modifier = Modifier.then(if (isPaused) Modifier.blur(radius = 15.dp) else Modifier)
        ) { page ->
            val reels = if(page == 0) reelsState.reelsForYou?.reels else reelsState.reelsFromMatches?.reels
            if (!reels.isNullOrEmpty()) {
                Box {
                    VerticalPager(
                        state = if (page == 0) pagerForYouTab else pagerForMatchesTab,
                        pageSpacing = 0.dp,
                        //   key = { index -> val reels = if (page == 0)  reelsForYou else reelsFromMatches; reels[index].id }
                    ) { index ->
                        val reelData = reels[index]
                        val reelContent = reelData?.content
                        reelId = reelData?.reelId ?: -1
                        if (reelContent?.mediaUrl != null) {
                            Box {
                                Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                                    if (reelData.mediaType == ReelContentType.VIDEO) {
                                        VideoPlayerView(
                                            modifier = Modifier.fillMaxSize().noRippleClickable {
                                                if (reelsScreenState.isVideoPlaying) {
                                                    reelsScreenModel.updatePauseButtonState(true)
                                                }
                                            },
                                            url = reelContent.mediaUrl,
                                            thumbnailUrl = reelContent.thumbnailUrl ?: "",
                                            isPlaying = reelsScreenState.isVideoPlaying,
                                            isMuted = reelsScreenState.isVideoMuted,
                                            enableAdvancedSettings = true,
                                            onContentDuration = reelsScreenModel::updateTotalDuration,
                                            onCurrentPosition = reelsScreenModel::updateCurrentPosition,
                                            clipRange = reelsScreenState.currentSliderPos..100f
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth().height(55.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (!reelsScreenState.isVideoPlaying || reelsScreenState.showPauseButton) {
                                                Image(
                                                    imageVector = vectorResource(Res.drawable.ic_seek_10sec_back),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(30.71.dp)
                                                        .noRippleClickable {
//                                                        reelsScreenModel.updateCurrentPosition(max(0L,reelsScreenState.currentPosition - 10))
                                                            var newValue = reelsScreenState.currentPosition - 10_000L
                                                            newValue = if (newValue < 0) 0 else newValue
                                                            println("the backward newValue  is ${reelsScreenState.totalDuration} ${newValue/reelsScreenState.totalDuration.toFloat()} ${reelsScreenState.currentPosition} $newValue")
                                                            reelsScreenModel.updateVideoPlayingState(false)
                                                            reelsScreenModel.updateSliderCurrentPosition((newValue/reelsScreenState.totalDuration.toFloat()) * 100f)
                                                            reelsScreenModel.updateCurrentPosition(newValue)
                                                            reelsScreenModel.updateVideoPlayingState(true)

                                                        }
                                                )

                                                Spacer(Modifier.width(24.dp))
                                            }
                                            if (!reelsScreenState.isVideoPlaying){
                                                Image(
                                                    imageVector = vectorResource(Res.drawable.ic_play_button),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(55.dp).clip(CircleShape)
                                                        .noRippleClickable {
                                                            reelsScreenModel.updateVideoPlayingState(true)
                                                        }
                                                )
                                            }

                                            if(reelsScreenState.showPauseButton){
                                                Image(
                                                    imageVector = vectorResource(Res.drawable.ic_pause_button),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(55.dp)
                                                        .clip(CircleShape)
                                                        .noRippleClickable {
                                                            reelsScreenModel.updateVideoPlayingState(false); reelsScreenModel.updatePauseButtonState(false)
                                                        }
                                                )

                                                LaunchedEffect(Unit) {
                                                    delay(1000)
                                                    //  screenModel.updateToastMessage("")
                                                    reelsScreenModel.updatePauseButtonState(false)
                                                }
                                            }

                                            if (!reelsScreenState.isVideoPlaying || reelsScreenState.showPauseButton) {
                                                Spacer(Modifier.width(24.dp))
                                                Image(
                                                    imageVector = vectorResource(Res.drawable.ic_seek_10sec_forward),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(30.71.dp)
                                                        .noRippleClickable {
                                                            var newValue = reelsScreenState.currentPosition + 10_000L
                                                            newValue = if (newValue > reelsScreenState.totalDuration) reelsScreenState.totalDuration else newValue
                                                            println("the newValue is ${reelsScreenState.totalDuration} ${newValue/reelsScreenState.totalDuration.toFloat()} ${reelsScreenState.currentPosition} $newValue")
//                                                      reelsScreenModel.updateCurrentPosition(min(reelsScreenState.totalDuration,reelsScreenState.currentPosition + 10))
                                                            reelsScreenModel.updateVideoPlayingState(false)
                                                            reelsScreenModel.updateSliderCurrentPosition((newValue/reelsScreenState.totalDuration.toFloat()) * 100f)
                                                            reelsScreenModel.updateCurrentPosition(newValue)
                                                            reelsScreenModel.updateVideoPlayingState(true)
                                                        }
                                                )
                                            }
                                        }

                                    }else{
                                        AsyncImage(
                                            modifier = Modifier.fillMaxSize(),
                                            model = reelContent.mediaUrl,
                                            contentScale = ContentScale.FillWidth,
                                            contentDescription = null
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(51.dp)
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        TextTabRowItem(
                                            text = stringResource(Res.string.for_you),
                                            showIndicator = page == 0,
                                            onClick = {
                                                coroutineScope.launch {
                                                    horizontalTabState.animateScrollToPage(0)
                                                }
                                            }
                                        )

                                        Spacer(Modifier.width(10.dp))

                                        TextTabRowItem(
                                            text = stringResource(Res.string.matches),
                                            showIndicator = page == 1,
                                            onClick = {
                                                coroutineScope.launch {
                                                    if (reelsFromMatches.isNotEmpty()) {
                                                        horizontalTabState.animateScrollToPage(1)
                                                    }
                                                }
                                            }
                                        )

                                        Spacer(Modifier.weight(1f))

                                        if (page == 0) {
                                            Image(
                                                imageVector = vectorResource(Res.drawable.ic_filter_reels),
                                                contentDescription = null,
                                                modifier = Modifier.width(24.dp).height(14.93.dp)
                                            )
                                            Spacer(Modifier.width(24.dp))
                                        }


                                        Box(
                                            Modifier.size(24.dp)
                                                .noRippleClickable(navigateToCreateCaptureScreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                imageVector = vectorResource(Res.drawable.ic_camera_outlined),
                                                contentDescription = null,
                                                modifier = Modifier.size(height = 18.dp, width = 22.dp)
                                            )
                                        }


                                    }

                                }


                                Box(
                                    modifier = Modifier.fillMaxSize().padding(bottom = 70.dp),
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    Column(
                                        Modifier.padding(start = 16.dp)
                                    ) {
                                        reelData.caption?.let {
                                            Text(
                                                text = it,
                                                fontFamily = PoppinsFontFamily(),
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                lineHeight = 21.sp,
                                                letterSpacing = 0.2.sp,
                                                color = Color.White
                                            )
                                            Spacer(Modifier.height(6.dp))
                                        }

                                        reelData.userData?.let {
                                            if (!it.name.isNullOrBlank()) {
                                                DescriptionProfile(name = it.name, age = it.age!!, isVerified = it.verified ?: false, distance = it.distance)
                                                Spacer(Modifier.height(8.dp))
                                            }
                                        }
                                        if(reelData.originalAudio == true) {
                                            OriginalAudio()
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.BottomEnd
                                ) {

                                    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                                        Spacer(Modifier.weight(1f))
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                            Column(
                                                Modifier.width(38.dp).wrapContentHeight(),
                                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {

                                                AsyncImage(
                                                    model = reelData.userData?.imageUrl ?: "",
                                                    contentDescription = null,
                                                    modifier = Modifier.size(32.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop
                                                )


                                                Image(
                                                    imageVector = vectorResource(Res.drawable.ic_spotlight_message),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(28.dp)
                                                )

                                                Image(
                                                    imageVector = vectorResource(if (reelData.isFavorite) Res.drawable.ic_bookmark_filled else Res.drawable.ic_bookmark_outlined),
                                                    contentDescription = null,
                                                    modifier = Modifier.width(19.66.dp).height(25.67.dp)
                                                        .noRippleClickable {
                                                            reelsScreenModel.markReelFavoriteUnFavorite(context,reelId, !reelData.isFavorite, addedMsg = addedToFavorites, removedMsg = removedFromFavorites)
                                                        }
                                                )

                                                Image(
                                                    imageVector = vectorResource(Res.drawable.ic_share_outlined),
                                                    contentDescription = null,
                                                    modifier = Modifier.width(21.27.dp).height(19.63.dp)
                                                        .noRippleClickable {
                                                            reelsScreenModel.updateShowShareReelBottomSheetState(true)
                                                        }
                                                )

                                                Image(
                                                    imageVector = vectorResource(Res.drawable.ic_dot_menu),
                                                    contentDescription = null,
                                                    modifier = Modifier.width(24.dp).height(18.91.dp)
                                                        .noRippleClickable {
                                                            reelsScreenModel.updateShowReportReelBottomSheetState(true)
                                                        }
                                                )

                                                if (reelData.mediaType == ReelContentType.VIDEO) {
                                                    Image(
                                                        imageVector = vectorResource(if(reelsScreenState.isVideoMuted) Res.drawable.ic_muted else Res.drawable.ic_unmuted) ,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(24.dp).noRippleClickable{reelsScreenModel.toggleVideoMutedState()}
                                                    )
                                                    //  Spacer(Modifier.height(6.dp))
                                                }
                                            }
                                        }
                                        Spacer(Modifier.height(4.dp))
                                        VideoTimeSlider(
                                            start = (reelsScreenState.currentPosition/1000).toInt(),
                                            end = (reelsScreenState.totalDuration/1000).toInt(),
                                            onSliderValueChange = {
                                                reelsScreenModel.updateSliderCurrentPosition(it)
                                            },
                                            currentPos = animatedProgressValue
                                        )

                                    }


                                }

                                if (reelsScreenState.showSwipeUpAnimation) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth().height(220.dp)
                                                .background(
                                                    brush = Brush.verticalGradient(
                                                        listOf(
                                                            Color(0xff000000).copy(alpha = 0f),
                                                            Color(0xff000000).copy(alpha = 0.885f)
                                                        )
                                                    )
                                                ),
                                            verticalArrangement = Arrangement.Bottom,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                modifier = Modifier.size(68.dp)
                                                    .graphicsLayer(translationY = 55f),
                                                painter = rememberLottiePainter(
                                                    composition = swipeUpComposition.value,
                                                    iterations = Compottie.IterateForever,
                                                ),
                                                contentScale = ContentScale.FillBounds,
                                                contentDescription = "Lottie animation"
                                            )
                                            Text(
                                                modifier = Modifier.padding(bottom = 16.dp),
                                                text = stringResource(Res.string.swipe_up_for_more),
                                                fontFamily = PoppinsFontFamily(),
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                lineHeight = 20.sp,
                                                letterSpacing = 0.2.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }


                            }
                        }
                    }
                    ConnectivityToast()
                }
            }
        }




        AnimatedVisibility(
            modifier = Modifier.padding(top = 56.dp).padding(horizontal = 60.dp).wrapContentHeight(),
            visible = reelsScreenState.toastMessage.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Toast(text = reelsScreenState.toastMessage)

            LaunchedEffect(Unit) {
                delay(2000) // Hide after 2 seconds
                reelsScreenModel.updateToastMessage("")
            }

        }


    }


 //   key(isAccountPaused){
        if (showCreateReelsScreen && isConnected) {
//            DefaultCreateReelsScreen(
//                onCreateButtonClick = navigateToCreateCaptureScreen,
//                showAccountResume = isPaused,
//                onResumeAccount = {accountsViewModel.resumeAccount(context)},
//                isLoading = accountsState.isLoading
//            )
            NoReelsLeft(onCreateReelAction = navigateToCreateCaptureScreen, onChangeFilter = navigateToFilterScreen, isDarkMode = isDarkMode)
        }

        if (!isConnected){
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                NoInternet(isDarkMode = isDarkMode,onRetry = {
                    if (isConnected){
                        reelsScreenModel.getReelsForYou(context,1)
                    }
                })
            }
        }
   // }

    if (reelsScreenState.showReportReelBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = reportReelSheetState,
            onDismissRequest = {
                reelsScreenModel.updateShowReportReelBottomSheetState(false)
                reelsScreenModel.updateShowReportOptionsState(false)
                reelsScreenModel.updateShowNotInterestedOptionsState(false)

                //screenModel.hideSheet()
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ReportReelBottomSheetContent(
                isDarkMode = isDarkMode,
                onReportButtonClick = {
                    coroutineScope.launch {
                        reportReelSheetState.hide()
                        reelsScreenModel.reportReel(context,it)
                    }.invokeOnCompletion {
                        reelsScreenModel.updateShowReportReelBottomSheetState(false)
                        reelsScreenModel.updateShowReportOptionsState(false)
                        reelsScreenModel.updateShowNotInterestedOptionsState(false)
                    }
                },
                showNotInterestedOptions = reelsScreenState.showNotInterestedOptions,
                showReportOptions = reelsScreenState.showReportOptions,
                onReportClick = {
                    reelsScreenModel.updateShowReportOptionsState(true)
                },
                onNotInterestedClick = {
                    reelsScreenModel.updateShowNotInterestedOptionsState(true)
                    reelsScreenModel.markReel(context,it)
                },
                hideBottomSheet = {
                    coroutineScope.launch {
                        reportReelSheetState.hide()
                    }.invokeOnCompletion {
                        reelsScreenModel.updateShowReportReelBottomSheetState(false)
                        reelsScreenModel.updateShowReportOptionsState(false)
                        reelsScreenModel.updateShowNotInterestedOptionsState(false)
                    }
                },
                onToastMessage = {
                    reelsScreenModel.updateToastMessage(it)
                },
                reelId = reelId
            )
        }
    }

    if (reelsScreenState.showShareReelBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = shareReelBottomSheetState,
            onDismissRequest = {
                reelsScreenModel.updateShowShareReelBottomSheetState(false)
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ){

            ShareReelBottomSheetContent(
                isDarkMode = isDarkMode,
                onToastMessage = {
                    reelsScreenModel.updateToastMessage(it)
                },
                hideBottomSheet = {
                    coroutineScope.launch {
                        shareReelBottomSheetState.hide()
                    }.invokeOnCompletion {
                        reelsScreenModel.updateShowShareReelBottomSheetState(false)
                    }
                },
                onShareButtonClick = {
                    coroutineScope.launch {
                        shareReelBottomSheetState.hide()
                    }.invokeOnCompletion {
                        reelsScreenModel.updateShowShareReelBottomSheetState(false)
                    }
                }
            )
        }
    }

    if (reelsState.isLoading && reelsForYou.isNotEmpty()){
        CircularLoader()
    }
}
