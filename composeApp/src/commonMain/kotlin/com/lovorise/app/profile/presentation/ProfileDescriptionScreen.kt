package com.lovorise.app.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.basics
import coinui.composeapp.generated.resources.bio
import coinui.composeapp.generated.resources.ic_back_white
import coinui.composeapp.generated.resources.ic_drink
import coinui.composeapp.generated.resources.ic_edit_profile
import coinui.composeapp.generated.resources.ic_education
import coinui.composeapp.generated.resources.ic_family_planning
import coinui.composeapp.generated.resources.ic_gender
import coinui.composeapp.generated.resources.ic_height_scale
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_pets
import coinui.composeapp.generated.resources.ic_profession
import coinui.composeapp.generated.resources.ic_smoke
import coinui.composeapp.generated.resources.interests
import coinui.composeapp.generated.resources.languages
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.libs.camera.ImageAspect
import com.lovorise.app.libs.media_player.VideoPlayerView
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreen
import com.lovorise.app.swipe.presentation.SwipeLeftRightScreen
import com.lovorise.app.swipe.presentation.components.ChipItem
import com.lovorise.app.swipe.presentation.components.DescriptionProfile
import com.lovorise.app.swipe.presentation.components.LookingForSection
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ProfileDescriptionScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        ProfileDescriptionScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                navigator.pop()
            },
            accountsState = accountsState,
            navigateToEditProfile = {
                navigator.push(EditProfileScreen)
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileDescriptionScreenContent(
    isDarkMode:Boolean,
    onBack:()->Unit,
    accountsState: AccountsApiCallState,
    navigateToEditProfile:()->Unit
) {

    val scrollState = rememberScrollState()


    var showTopBar by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isPlaying by rememberSaveable { mutableStateOf(true) }


    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollPosition ->
                val threshold = (scrollState.maxValue * 0.1).toInt()
                showTopBar = scrollPosition > threshold
            }
    }

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(0){accountsState.user?.medias?.filter { it.url.isNotBlank() || !it.localPath.isNullOrBlank() }?.size ?: 0}

    var progress by rememberSaveable {  mutableStateOf(mapOf<String,Float>()) }

    LaunchedEffect(pagerState.currentPage){
        isPlaying = true
    }

    Column {

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
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            BoxWithConstraints {
                val height = maxHeight
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {
                    Box(
                        modifier = Modifier
                            .requiredHeight(height.times(0.6f))
                    ) {

                        HorizontalPager(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight().pointerInput(Unit) {
                                detectTapGestures { offset: androidx.compose.ui.geometry.Offset ->
                                    val boxSize = size.toSize()
                                    when {
                                        offset.x < boxSize.width / 2 -> {

                                            if (pagerState.currentPage > 0){
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(pagerState.currentPage-1)
                                                }
                                            }
                                        }

                                        offset.x > boxSize.width / 2 ->{


                                            if (pagerState.currentPage < pagerState.pageCount){
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(pagerState.currentPage+1)
                                                }
                                            }


                                        }
                                        else -> {}
                                    }
                                }
                            },
                            state = pagerState
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                  //  .background(Color(0xffEAECF0))
                            ) {
                                val media = accountsState.user?.medias?.getOrNull(it)
                                if (media != null) {
                                    if (media.type == SignedUrlMediaItem.Type.VIDEO) {
                                       // progress = 0f
                                        val path = if(media.localPath.isNullOrBlank()) media.url else media.localPath
                                        ProfileVideoPlayer(
                                            path = path,
                                            thumbnail = media.thumbnail,
                                            onProgress = { new->
                                                println("the progress is $it")
                                                progress = progress.toMutableMap().apply {
                                                    this[media.id] = new
                                                }
                                            },
                                            enableProgress = true,
                                            onLoading = {
                                                isLoading = it
                                            }
                                        )
                                    }
                                    if (media.type == SignedUrlMediaItem.Type.IMAGE) {
                                        val path = if(media.localPath.isNullOrBlank()) media.url else media.localPath.removePrefix("file://")

                                        //progress = 1f
                                        AsyncImage(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                                .background(if (isLoading) Color(0xffEAECF0) else Color.Black),
                                            model = path,
                                            contentScale = ContentScale.Fit,
                                            contentDescription = "",
                                            onSuccess = {
                                                isLoading = false
                                            },
                                            onLoading = {
                                                isLoading = true
                                            },
                                            onError = {
                                                isLoading = true
                                            }
                                        )
                                    }
                                }

                                // Description Profile
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(bottom = 25.dp)
                                ) {
                                    DescriptionProfile(
                                        modifier = Modifier.padding(start = 17.dp),
                                        isAgeVisible = accountsState.user?.isAgeVisible ?: true,
                                        showDistance = false,
                                        name = accountsState.user?.name ?: "",
                                        age = accountsState.user?.age ?: 0,
                                        distance = accountsState.user?.distance?.toInt() ?: 0,
                                        isVerified = accountsState.user?.isVerified ?: false
                                    )
                                }

                                if (!showTopBar) {
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(bottom = 25.dp, end = 24.dp)
                                    ) {
                                        Image(
                                            modifier = Modifier.size(48.dp).clip(CircleShape)
                                                .noRippleClickable(navigateToEditProfile),
                                            imageVector = vectorResource(Res.drawable.ic_edit_profile),
                                            contentDescription = "edit profile"
                                        )
                                    }
                                }
                            }
                        }

                        // Content Top Arrow Back, Dot Menu Horizontal Pager
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(top = 13.3.dp, start = 17.dp, end = 17.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.26.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                repeat(pagerState.pageCount) {
                                    val media = accountsState.user?.medias?.getOrNull(it)
                                    if (media?.type == SignedUrlMediaItem.Type.IMAGE || it != pagerState.currentPage){
                                        Box(
                                            modifier = Modifier
                                                .height(1.48.dp)
                                                .background(color = if (it == pagerState.currentPage) Color.White else Color(0xff6D657F), shape = RoundedCornerShape(8.dp))
                                                .weight(1f)
                                        )
                                    }else{
                                        LaunchedEffect(true){
                                            progress = progress.toMutableMap().apply {
                                                this[media?.id!!] = 0f
                                            }
                                        }
                                        val animatedProgress by animateFloatAsState(
                                            targetValue = progress[media?.id!!] ?: 0f,
                                            animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                                        )
                                        LinearProgressIndicator(
                                            modifier = Modifier.height(1.48.dp).background(Color(0xff6D657F),RoundedCornerShape(8.dp)).weight(1f),
                                            progress = { animatedProgress },
                                            color = Color.White,
                                            trackColor = Color(0xff6D657F),
                                            drawStopIndicator = {},
                                            gapSize = 0.dp
                                        )

//                                        Slider(
//                                            modifier = Modifier.height(1.48.dp).weight(1f),
//                                            enabled = false,
//                                            value = animatedProgress,
//                                            onValueChange = {},
//                                            colors = SliderDefaults.colors(
//                                                disabledActiveTickColor = Color.Transparent,
//                                                disabledInactiveTickColor = Color.Transparent,
//                                                inactiveTickColor = Color.Transparent,
//                                                activeTrackColor = Color.Transparent,
//                                                inactiveTrackColor = Color.Transparent,
//                                                thumbColor = Color.Transparent,
//                                                disabledActiveTrackColor = Color.White,
//                                                disabledInactiveTrackColor = Color(0xff6D657F)
//                                            ),
//                                            thumb = {
//                                               // Box(Modifier.width(1.dp).height(36.dp).background(Color.White))
//                                            }
//
//                                        )

                                       // PlayerProgressIndicator(animatedProgress)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                // Icon Back
                                Box(
                                    modifier = Modifier
                                        .noRippleClickable(onBack)
                                        .size(38.dp)
                                        .background(
                                            Color(0xFF121212).copy(alpha = 0.3f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        modifier = Modifier.size(height = 13.dp, width = 16.67.dp),
                                        imageVector = vectorResource(Res.drawable.ic_back_white),
                                        contentDescription = "",
                                    )
                                }

                                // Dot Menu
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }



                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)

                    ) {

                        Spacer(Modifier.height(19.dp))

                        if (!accountsState.user?.bio.isNullOrBlank() && accountsState.user?.bioVisible == true) {
                            Text(
                                text = stringResource(Res.string.bio),
                                fontSize = 14.sp,
                                color = if (isDarkMode) Color.White else Color(0xff101828),
                                lineHeight = 21.sp,
                                letterSpacing = 0.2.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = accountsState.user.bio ?: "",
                                fontSize = 14.sp,
                                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff101828),
                                lineHeight = 21.sp,
                                letterSpacing = 0.2.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Normal
                            )

                            Spacer(Modifier.height(15.dp))
                        }

                        Text(
                            text = stringResource(Res.string.basics),
                            fontSize = 14.sp,
                            color = if (isDarkMode) Color.White else Color(0xff101828),
                            lineHeight = 21.sp,
                            letterSpacing = 0.2.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(11.dp))

                        accountsState.user?.typeOfRelation?.let {items->
                            LookingForSection(items.mapNotNull {item-> SwipeLeftRightScreen.datingPrefsData.firstOrNull { it.text ==  item} }, isDarkMode = isDarkMode)
                        }

                        Spacer(Modifier.height(11.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!accountsState.user?.gender.isNullOrBlank() && accountsState.user?.genderVisible == true){
                                ChipItem(icon = Res.drawable.ic_gender, text = accountsState.user.gender!!,isDarkMode = isDarkMode)
                            }
                            if (accountsState.user?.height != null && accountsState.user.heightVisible == true) {
                                ChipItem(icon = Res.drawable.ic_height_scale, text = "${accountsState.user.height}cm",isDarkMode = isDarkMode)
                            }
                            if (!accountsState.user?.education.isNullOrBlank() && accountsState.user?.educationVisible == true) {
                                ChipItem(
                                    icon = Res.drawable.ic_education,
                                    text = accountsState.user.education!!,
                                    isDarkMode = isDarkMode
                                )
                            }
                            accountsState.user?.profession?.let{ profession ->
                                if (!profession.jobTitle.isNullOrBlank() && accountsState.user.profession.professionVisible == true){
                                    ChipItem(icon = Res.drawable.ic_profession, text = "${profession.jobTitle}${if (!profession.company.isNullOrBlank()) ",${profession.company}" else ""}",isDarkMode = isDarkMode)
                                }
                            }
                            if (!accountsState.user?.family.isNullOrBlank() && accountsState.user?.familyVisible == true){
                                ChipItem(icon = Res.drawable.ic_family_planning, text = accountsState.user.family!!,isDarkMode = isDarkMode)
                            }
                            if (accountsState.user?.pets?.firstOrNull()?.isNotBlank() == true && accountsState.user.petsVisible == true){
                                ChipItem(icon = Res.drawable.ic_pets, text = accountsState.user.pets.firstOrNull()!!,isDarkMode = isDarkMode)
                            }
                            if (!accountsState.user?.drinking.isNullOrBlank() && accountsState.user?.drinkingVisible == true){
                                ChipItem(icon = Res.drawable.ic_drink, text = accountsState.user.drinking!!,isDarkMode = isDarkMode)
                            }
                            if (!accountsState.user?.smoking.isNullOrBlank() && accountsState.user?.smokingVisible == true){
                                ChipItem(icon = Res.drawable.ic_smoke, text = accountsState.user.smoking!!,isDarkMode = isDarkMode)
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = stringResource(Res.string.interests),
                            fontSize = 14.sp,
                            color = if (isDarkMode) Color.White else Color(0xff101828),
                            lineHeight = 21.sp,
                            letterSpacing = 0.2.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            accountsState.user?.interests?.values?.filterNotNull()?.flatten()?.forEach {
                                it?.let {
                                    ChipItem(text = it,isDarkMode = isDarkMode)
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        if (accountsState.user?.languageVisible == true) {
                            Text(
                                text = stringResource(Res.string.languages),
                                fontSize = 14.sp,
                                color = if (isDarkMode) Color.White else Color(0xff101828),
                                lineHeight = 21.sp,
                                letterSpacing = 0.2.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                accountsState.user.language.filterNotNull().forEach {
                                    ChipItem(text = it,isDarkMode = isDarkMode)
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                        }


                    }

                    Spacer(Modifier.height(30.dp))


                }
                Column {
                    AnimatedVisibility(
                        visible = showTopBar,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {

                        Row(
                            modifier = Modifier.background(if (isDarkMode) BASE_DARK else Color.White).fillMaxWidth().noRippleClickable {}
                                .height(58.dp).padding(start = 21.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier.fillMaxHeight()
                                    .noRippleClickable { onBack() },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(
                                    modifier = Modifier.width(14.67.dp).height(13.29.dp),
                                    imageVector = vectorResource(Res.drawable.ic_left),
                                    contentDescription = "back",
                                    tint = if (isDarkMode) Color.White else Color(0xff101828)
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = accountsState.user?.name ?: "",
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    lineHeight = 20.sp,
                                    letterSpacing = 0.2.sp,
                                    color = if (isDarkMode) Color.White else Color(0xff101828)
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Image(
                                modifier = Modifier.size(32.dp).clip(CircleShape).noRippleClickable(navigateToEditProfile),
                                imageVector = vectorResource(Res.drawable.ic_edit_profile),
                                contentDescription = "edit profile"
                            )


                        }
                    }
                }
            }





        }



        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }



}





@Composable
fun ProfileVideoPlayer(modifier: Modifier=Modifier,path:String,thumbnail:String?,onProgress:(Float)->Unit,enableProgress:Boolean = false,onLoading:(Boolean)->Unit,aspect: ImageAspect=ImageAspect.ASPECT_FIT) {

    var totalDuration by remember { mutableLongStateOf(-1) }
    var currentPosition by remember { mutableLongStateOf(-1) }

//    val coroutineScope = rememberCoroutineScope()
//
//    val playerHost = remember(path) {
//        MediaPlayerHost(
//            mediaUrl = path,
//            initialVideoFitMode = ScreenResize.FILL
//        )
//    }

    LaunchedEffect(totalDuration,currentPosition){
        if (totalDuration != -1L && currentPosition != -1L){
            println("current Position: ${currentPosition.toFloat()}, total Duration: ${totalDuration.toFloat()}")
            onProgress(currentPosition.toFloat()/totalDuration.toFloat())
        }

    }

//    LaunchedEffect(totalDuration){
////        while (currentPosition != totalDuration){
////            currentPosition += 100
////            delay(100)
//////            onProgress()
////        }
//        if (totalDuration in 1000..3000){
//            playerHost.setSpeed(PlayerSpeed.X0_5)
//        }
//    }



//    playerHost.onEvent = { event ->
//        when (event) {
////            is MediaPlayerEvent.MuteChange -> { println("Mute status changed: ${event.isMuted}") }
////            is MediaPlayerEvent.PauseChange -> { println("Pause status changed: ${event.isPaused}") }
////            is MediaPlayerEvent.BufferChange -> { println("Buffering status: ${event.isBuffering}") }
//            is MediaPlayerEvent.CurrentTimeChange -> {
////                if (event.currentTime == 0){
////                    val t = totalDuration
////                    currentPosition = 0
////                    totalDuration = 0
////                    totalDuration = t
////                }
//                currentPosition = event.currentTime.toLong() * 1000
//            }
//            is MediaPlayerEvent.TotalTimeChange -> {
//                totalDuration = event.totalTime.toLong() * 1000
//                currentPosition = 1000
//            }
////            is MediaPlayerEvent.FullScreenChange -> { println("FullScreen status changed: ${event.isFullScreen}") }
//            MediaPlayerEvent.MediaEnd -> {
////                coroutineScope.launch {
////                    delay(1000)
////                    currentPosition = 1000
////                }
//            }
//            else -> {}
//        }
//    }



    Column(modifier.clipToBounds()) {
//
//        VideoPlayerComposable(
//            modifier = Modifier.fillMaxSize(),
//            playerHost = playerHost,
//            playerConfig = VideoPlayerConfig(
//                showControls = false,
//                showVideoQualityOptions = false,
//                isDurationVisible = false,
//                isZoomEnabled = false,
//                loadingIndicatorColor = Color.Transparent,
//                isGestureVolumeControlEnabled = false,
//                isAutoHideControlEnabled = true,
//
////            loaderView = {
////                Box(
////                    modifier = Modifier.height(130.dp).fillMaxWidth(),
////                    contentAlignment = Alignment.Center
////                ){
////                    Loader()
////                }
////            }
//            )
//        )

        VideoPlayerView(
            modifier = Modifier.fillMaxSize(),
            url = path,
            thumbnailUrl = thumbnail ?: "",
            isVideoPausedInitially = false,
            isPlaying = true,
            onContentDuration = {
                totalDuration = it
            },
            onCurrentPosition = {
                println("the current position is $it , duration is $totalDuration")
                currentPosition = it
            },
            enableAdvancedSettings = enableProgress,
            onLoadingState = onLoading,
            aspect = aspect,
            enableLoader = false,
            slowVideo = true
        )
    }


}