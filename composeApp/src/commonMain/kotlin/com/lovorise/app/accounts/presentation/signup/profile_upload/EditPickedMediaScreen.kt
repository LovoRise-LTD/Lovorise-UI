package com.lovorise.app.accounts.presentation.signup.profile_upload

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
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_crop
import coinui.composeapp.generated.resources.ic_cross_white
import coinui.composeapp.generated.resources.ic_delete_bin
import coinui.composeapp.generated.resources.ic_pause_button
import coinui.composeapp.generated.resources.ic_play_button
import coinui.composeapp.generated.resources.save
import com.attafitamim.krop.core.crop.AspectRatio
import com.attafitamim.krop.core.crop.CropError
import com.attafitamim.krop.core.crop.CropResult
import com.attafitamim.krop.core.crop.crop
import com.attafitamim.krop.core.crop.cropperStyle
import com.attafitamim.krop.core.crop.rememberImageCropper
import com.lovorise.app.MediaItem
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.ShimmerAnimation
import com.lovorise.app.formatToTwoDecimalPlaces
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.camera.ImageAspect
import com.lovorise.app.libs.camera.IosImageView
import com.lovorise.app.libs.file_handler.getByteArrayFromPath
import com.lovorise.app.libs.media_player.VideoPlayerView
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreen
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.VideoTrimmingSlider
import com.lovorise.app.swipe.presentation.toPx
import com.lovorise.app.ui.ThemeViewModel
import io.github.alexzhirkevich.compottie.internal.platform.fromBytes
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class EditPickedMediaScreen : Screen{


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val profileUploadScreenViewModel = navigator.koinNavigatorScreenModel<ProfileUploadScreenViewModel>()
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()

        EditPickedMediaScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                navigator.pop()
            },
            profileUploadScreenViewModel = profileUploadScreenViewModel,
            accountsState = accountsState,
            navigateBack = {
                navigator.popUntil { it.instanceOf(EditProfileScreen::class) || it.instanceOf(ProfileUploadScreen::class) }
                profileUploadScreenViewModel.resetSelectedMedias()
            },
            navigator = navigator
        )
    }
}


@Composable
fun EditPickedMediaScreenContent(navigator: Navigator,isDarkMode:Boolean,profileUploadScreenViewModel: ProfileUploadScreenViewModel,onBack:()->Unit,accountsState: AccountsApiCallState,navigateBack:()->Unit) {

    val state by profileUploadScreenViewModel.state.collectAsState()



    val coroutineScope = rememberCoroutineScope()


//    val graphicsLayer = rememberGraphicsLayer()
    val context = LocalPlatformContext.current

    val pagerState = rememberPagerState { state.pickedMediaItems.size }

    val imageCropper = rememberImageCropper()
    val cropState = imageCropper.cropState



    LaunchedEffect(pagerState.settledPage){
        println("the settled page is ${pagerState.settledPage}")
      //  profileUploadScreenViewModel.updateSelectedPickedIndex(pagerState.currentPage)
        profileUploadScreenViewModel.loadVideoFrames(pagerState.settledPage,context)
    }

    LaunchedEffect(state.pickedMediaItems){
        if (state.pickedMediaItems.isEmpty() && navigator.lastItem.instanceOf(EditPickedMediaScreen::class)){
            println("the on back is triggering")
            onBack()
        }
    }


    Column(
        modifier = Modifier
    ) {

        Spacer(
            modifier = Modifier
                .background(Color.Black)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()


        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box {
                HorizontalPager(state = pagerState) {page ->

                    val selectedItem = state.pickedMediaItems.getOrNull(page)
                    val currentPosition = selectedItem?.videoInfo?.currentPosition?.toFloat() ?: 0f
                    val totalDuration = selectedItem?.videoInfo?.totalDuration?.toFloat() ?: 1f
                    val animatedProgressValue by animateFloatAsState(
                        targetValue = currentPosition / totalDuration,
                        animationSpec = tween(
                            durationMillis = if (isAndroid()) 500 else 1000,
                            easing = LinearEasing
                        )
                    )

                    key(page) {
                        if (selectedItem != null) {
                            if (selectedItem.type == MediaItem.Type.IMAGE) {
                                AsyncImage(
                                    model = selectedItem.path,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().background(Color.Transparent)
                                        .clipToBounds(),
                                    contentScale = ContentScale.FillWidth
                                )

                            } else if (selectedItem.type == MediaItem.Type.VIDEO) {
                                Box {
                                    println("the selected media is $selectedItem ${selectedItem.path} $page is ${selectedItem.videoInfo.clipRange}")
                                    // key(page) {
                                    val path = if (!isAndroid() && (selectedItem.path.contains("#")))  selectedItem.path.split("#").first() else selectedItem.path
                                    VideoPlayerView(
                                        modifier = Modifier.fillMaxSize().noRippleClickable {
                                            if (state.isPlaying) {
                                                profileUploadScreenViewModel.updatePauseButtonState(
                                                    true
                                                )
                                            }
                                        },
                                        url = path,
                                        thumbnailUrl = "",
                                        isVideoPausedInitially = true,
                                        isPlaying = state.isPlaying,
                                        onCurrentPosition = {
                                            profileUploadScreenViewModel.updateVideoInfoForCurrentMedia(
                                                page,
                                                state.pickedMediaItems[page].videoInfo.copy(currentPosition = it)
                                            )
                                        },
                                        clipRange = selectedItem.videoInfo.clipRange,
                                        enableAdvancedSettings = true
                                    )

                                    Box(
                                        Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 80.dp
                                        )
                                    ) {
                                        if (selectedItem.videoInfo.videoFrames.isNotEmpty()) {

                                            Column {
                                                VideoTrimmingSlider(
                                                    frameList = selectedItem.videoInfo.videoFrames,
                                                    progress = animatedProgressValue,
                                                    onVideoClipRange = { r ->
                                                        println("the clipRange change for $page:  $r")
                                                        selectedItem.videoInfo.copy(clipRange = r)
                                                            .let { info ->
                                                                profileUploadScreenViewModel.toggleVideoPlayingState(
                                                                    false
                                                                )
                                                                profileUploadScreenViewModel.updateVideoInfoForCurrentMedia(
                                                                    page,
                                                                    info
                                                                )
                                                            }
                                                    },
                                                    currentRange = selectedItem.videoInfo.clipRange
                                                )
                                                Spacer(Modifier.height(8.dp))

                                                Row(
                                                    modifier = Modifier.fillMaxWidth().height(26.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.End
                                                ) {

                                                    Text(
                                                        text = "${formatToTwoDecimalPlaces(selectedItem.videoInfo.totalDuration.toFloat()/1000)}s",
                                                        color = Color.White,
                                                        lineHeight = 24.sp,
                                                        fontSize = 14.sp,
                                                        letterSpacing = 0.2.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Spacer(Modifier.width(8.dp))
                                                    Box(Modifier.size(6.dp).background(Color.White, CircleShape))
                                                    Spacer(Modifier.width(8.dp))
                                                    Text(
                                                        text = "${formatToTwoDecimalPlaces(selectedItem.videoInfo.fileSize.toFloat()/(1024*1024))} mb",
                                                        color = Color.White,
                                                        lineHeight = 24.sp,
                                                        fontSize = 14.sp,
                                                        letterSpacing = 0.2.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )

                                                }



                                            }

                                        } else {
                                            ShimmerAnimation(
                                                Modifier.height(36.dp).fillMaxWidth(),
                                                shape = RoundedCornerShape(2.dp)
                                            )
                                        }
                                    }
                                    //   }


                                    //  }
                                    if (!state.isPlaying) {
                                        Box(
                                            Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                imageVector = vectorResource(Res.drawable.ic_play_button),
                                                contentDescription = null,
                                                modifier = Modifier.size(55.dp).clip(CircleShape)
                                                    .noRippleClickable {
                                                        profileUploadScreenViewModel.toggleVideoPlayingState(
                                                            true
                                                        )
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
                                                            profileUploadScreenViewModel.toggleVideoPlayingState(
                                                                false
                                                            ); profileUploadScreenViewModel.updatePauseButtonState(
                                                            false
                                                        )
                                                        }
                                                )
                                            }

                                            LaunchedEffect(Unit) {
                                                delay(1000)
                                                //  screenModel.updateToastMessage("")
                                                profileUploadScreenViewModel.updatePauseButtonState(
                                                    false
                                                )
                                            }

                                        }
                                    }

                                }
                            }
                        }
                    }
                }

                EditPickedMediaOverlayItems(
                    onSelectionChange = {
                       // profileUploadScreenViewModel.updateSelectedPickedIndex(it);
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                it
                            )
                        }
                    },
                    onBack = onBack,
                    onDeleteItem = profileUploadScreenViewModel::deletePickedMediaItem,
                    onSave = {
                        profileUploadScreenViewModel.updateLoadingState(true)
                        profileUploadScreenViewModel.uploadMedias(context, onComplete = {
                            profileUploadScreenViewModel.updateLoadingState(true)
                            navigateBack()
                        })


                    },
                    selectedIndex = pagerState.currentPage,
                    mediaItems = state.pickedMediaItems,
                    onCrop = {
                        coroutineScope.launch {
                            val selectedItem = state.pickedMediaItems.getOrNull(pagerState.currentPage)
                            if (selectedItem?.type == MediaItem.Type.IMAGE){
                                val arr = getByteArrayFromPath(selectedItem.path)
                                val result = if (arr != null) {
                                    imageCropper.crop(bmp = ImageBitmap.fromBytes(arr))
                                }else CropResult.Cancelled
                                when (result) {
                                    CropResult.Cancelled -> { }
                                    is CropError -> {}
                                    is CropResult.Success ->  {
                                        profileUploadScreenViewModel.onImageCropped(pagerState.currentPage,result.bitmap,context)
                                    }
                                }
                            }

                        }
                    },
                    isImage = state.pickedMediaItems.getOrNull(pagerState.settledPage)?.type == MediaItem.Type.IMAGE
                )
            }





        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(Color.Black)
        )
    }

    if (cropState != null) {
        Box(Modifier.fillMaxSize()) {
            ImageCropperDialog1(
                dialogProperties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnClickOutside = false,
                    dismissOnBackPress = true
                ),
                dialogPadding = PaddingValues(0.dp),
                dialogShape = RoundedCornerShape(0.dp),
                state = cropState,
                style = cropperStyle(
                    aspects = listOf(AspectRatio(1, 1), AspectRatio(4,3), AspectRatio(16, 9), AspectRatio(9,16)),
                ),
                isDarkMode = isDarkMode
            )
        }
    }


    if(accountsState.isLoading || state.isLoading){
        CircularLoader(true)
    }
}

@Composable
fun EditPickedMediaOverlayItems(onBack: () -> Unit, onCrop:()->Unit ,onSave:()->Unit, onSelectionChange:(Int)->Unit, onDeleteItem: (ProfileUploadScreenState.PickedMediaItem) -> Unit,mediaItems:List<ProfileUploadScreenState.PickedMediaItem>,selectedIndex:Int,isImage:Boolean) {

    Column {
        Spacer(Modifier.height(16.dp))
        Box(Modifier.padding(start = 16.dp).size(24.dp).noRippleClickable(onBack), contentAlignment = Alignment.Center){
            Image(
                imageVector = vectorResource(Res.drawable.ic_cross_white),
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
        }


        Spacer(Modifier.weight(1f))

        LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            itemsIndexed(mediaItems){index,item->
                SelectedMediaItem(Modifier.animateItem(),item,index == selectedIndex, onClick = {onSelectionChange(index)}, onDeleteItem = onDeleteItem )
                if(index != mediaItems.lastIndex){
                    Spacer(Modifier.width(8.dp))
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        Row(Modifier.height(32.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (isImage){
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                        .noRippleClickable(onCrop),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_crop),
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Box(
                Modifier.height(32.dp).width(84.dp)
                    .background(Color(0xffF33358), RoundedCornerShape(8.dp))
                    .noRippleClickable(onSave), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.save),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 14.sp,
                    // lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.width(16.dp))

        }
        Spacer(Modifier.height(18.dp))
    }
}

@Composable
fun SelectedMediaItem(modifier: Modifier=Modifier,mediaItem: ProfileUploadScreenState.PickedMediaItem,isSelectedItem:Boolean,onClick:()->Unit,onDeleteItem:(ProfileUploadScreenState.PickedMediaItem)->Unit) {
    Box(modifier.noRippleClickable(onClick)){

        val height = 68.dp
        val width = 46.3.dp

        if (!isAndroid() && mediaItem.type == MediaItem.Type.VIDEO){

            Box(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                IosImageView(mediaItem.id, size = IntSize(width = width.toPx().toInt(), height = height.toPx().toInt()), aspect = ImageAspect.ASPECT_FILL)
            }
        }
        else {
            AsyncImage(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .background(Color(0xffEAECF0), RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp)),
                model = mediaItem.path,
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
        }

        if (isSelectedItem) {
            Box(Modifier.width(46.3.dp).height(68.dp).clipToBounds().noRippleClickable { onDeleteItem(mediaItem) }, contentAlignment = Alignment.Center) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_delete_bin),
                    contentDescription = null,
                    modifier = Modifier.height(18.dp).width(16.dp)
                )
            }
        }
    }
}

