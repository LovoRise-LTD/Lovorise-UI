package com.lovorise.app.accounts.presentation.signup.profile_upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.choose_profile
import coinui.composeapp.generated.resources.gallery
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_play_button
import coinui.composeapp.generated.resources.ic_tick_mark
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.save
import com.lovorise.app.MediaItem
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.DropShadow
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.camera.ImageAspect
import com.lovorise.app.libs.camera.IosImageView
import com.lovorise.app.libs.media_player.VideoPlayerView
import com.lovorise.app.noRippleClickable
import com.lovorise.app.reels.presentation.reels_create_upload_view.screens.Loader
import com.lovorise.app.swipe.presentation.toPx
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class GalleryAlbumScreen: Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val profileUploadViewModel = navigator.koinNavigatorScreenModel<ProfileUploadScreenViewModel>()

        val context = LocalPlatformContext.current

        LaunchedEffect(true){
            profileUploadViewModel.onInit(null, context)
            profileUploadViewModel.updateLoadingState(false)
        }

        GalleryAlbumScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                profileUploadViewModel.reloadGalleryImages()
                navigator.pop()
            },
            profileUploadViewModel = profileUploadViewModel,
            onNext = {
//                profileUploadViewModel.updatePhotos(context)
//                profileUploadViewModel.reloadGalleryImages()
            //    profileUploadViewModel.cropImageIfRequired(context)
                navigator.push(EditPickedMediaScreen())
            }
        )


    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun GalleryAlbumScreenContent(
    isDarkMode:Boolean,
    goBack:()->Unit,
    profileUploadViewModel: ProfileUploadScreenViewModel,
    onNext:()->Unit
) {

    val ctx = LocalPlatformContext.current

    val state by profileUploadViewModel.state.collectAsState()

    var previewIndex by remember { mutableStateOf(0) }



    val coroutineScope = rememberCoroutineScope()
    var onItemChanged by remember { mutableStateOf(false) }



    BackHandler(true){
        goBack()
    }

    LaunchedEffect(true){
//        if (state.galleryImages.isEmpty()) {
//            //profileUploadViewModel.loadImagesFromGallery(ctx, 0)
//        }
    }



//    var displayedImgHeight by remember { mutableStateOf(0.dp) }
    var displayedImageWidth by remember { mutableStateOf(0.dp) }
    var maxPreviewHeight by remember { mutableStateOf(0.dp) }

    var orgSize by remember { mutableStateOf(IntSize(0,0)) }

    //val density = LocalDensity.current



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
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopHeaderSection(goBack, onSave = {
                profileUploadViewModel.processMediaItems(context = ctx, performNavigation =  { onNext() })
            }, text = stringResource(Res.string.choose_profile), btnText = stringResource(Res.string.next),isDarkMode = isDarkMode)


            if (profileUploadViewModel.paginationState.allItems.isNullOrEmpty()){
                CircularLoader(true)
            }else{
                Box(Modifier.weight(0.44f)){
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                        if (displayedImageWidth == 0.dp) {
                            displayedImageWidth = maxWidth
                            maxPreviewHeight = maxHeight
                        }
                        val maxHeight = maxHeight
                        val maxWidth = maxWidth
                        key(state.currentMediaItem) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .requiredHeight(maxHeight)
                                    .requiredWidth(maxWidth)
                                    .clipToBounds()

                            ) {
                                val data = state.currentMediaItem
                                val image = if (data?.type == MediaItem.Type.IMAGE) data.image else data?.thumbnail
                                if (data?.type == MediaItem.Type.VIDEO && data.videoUri != null){
//                                    val path = if (!isAndroid() && (data.videoUri.contains("#")))  data.videoUri.split("#").first() else data.videoUri
//                                    println("the videoUri is ${data.videoUri} ${path}")
                                    VideoPlayerWithButtons(
                                        uri = data.videoUri,
                                        togglePlayingState = profileUploadViewModel::toggleVideoPlayingState,
                                        isPlaying = state.isPlaying
                                    )
                                }
                                if (data?.type == MediaItem.Type.IMAGE) {
                                    if (!isAndroid()){
                                        println("recomposing ios image view")
                                        IosImageView(data.id,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .verticalScroll(rememberScrollState())
                                                //  .zoomable(zoomState)
                                                .onSizeChanged { intSize ->
                                                    // profileUploadViewModel.updateCropSize(intSize)
                                                    orgSize = intSize
                                                },
                                            aspect = ImageAspect.ASPECT_FIT,
                                            size = IntSize(maxWidth.value.toInt(),maxHeight.value.toInt()),
                                            refreshCache = true

                                        )
                                    }
                                    else if (image?.instanceOf(String::class) == true || isAndroid()) {
                                        AsyncImage(
                                            model = image,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                // .verticalScroll(rememberScrollState())
                                                //.zoomable(zoomState)
                                                .onSizeChanged { intSize ->
                                                    //  profileUploadViewModel.updateCropSize(intSize)
                                                    orgSize = intSize
                                                },
                                            contentScale = ContentScale.Fit
                                            //                            contentScale = if (displayedImgHeight < maxPreviewHeight) ContentScale.FillHeight else ContentScale.FillWidth
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(0.56f)
                ) {

                    Row(
                        modifier = Modifier.height(39.dp).fillMaxWidth().padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = stringResource(Res.string.gallery),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) Color.White else Color.Black
                        )

                        Box(
                            modifier = Modifier.height(23.dp)
                                .background(Color(0xffF33358), shape = RoundedCornerShape(50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 15.dp),
                                text = "${state.selectedItems.size + 6 - profileUploadViewModel.getAllowedSectionLength()}/6 Selected",
//                            text = "${state.gallerySelectionLength}/${profileUploadViewModel.getAllowedSectionLength()} Selected",
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                letterSpacing = 0.2.sp,
                                color = Color.White
                            )
                        }
                    }

                    BoxWithConstraints(modifier = Modifier.weight(1f)) {
                        val eachHeight = (maxWidth.value - 8).times(0.29).dp
                        // profileUploadViewModel.paginationState

                        PaginatedLazyVerticalGrid(
                            modifier = Modifier.padding(horizontal = 2.dp),
                            paginationState = profileUploadViewModel.paginationState,
                            newPageProgressIndicator = {
                                LoadingGridItem(height = eachHeight)
                            },
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ){
                            itemsIndexed(profileUploadViewModel.paginationState.allItems!!){ index, item ->
                                //  key(state.selectedItems){
                                GridItems(
                                    selectedItems = state.selectedItems,
                                    data = item,
                                    onClick = {
                                        coroutineScope.launch {

                                            onItemChanged = true

//                                            profileUploadViewModel.onPreviewGesture(
//                                                previewIndex,
//                                                offset = Offset(offsetX,offsetY),scale,orgSize,graphicsLayer.toImageBitmap()
//                                            )
//                                            zoomState.reset()

                                            previewIndex = index
//                                            profileUploadViewModel.onGalleryItemClick(ProfileUploadScreenState.GalleryImageVideo(data = item, selectionIndex = 0))
                                            profileUploadViewModel.onGallerySelectionItemClick(item)
                                        }
                                    },
                                    // isSelected = state.selectedItems.contains(item),
                                    height = eachHeight,
                                    modifier = Modifier
                                )

                                // }
                            }

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

    if (state.isLoading){
        CircularLoader(center = true)
    }

}

@Composable
fun VideoPlayerWithButtons(uri:String,isPlaying: Boolean,togglePlayingState:(Boolean)->Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        VideoPlayerView(
            modifier = Modifier.fillMaxSize().noRippleClickable { togglePlayingState(!isPlaying) },
            url = uri,
            thumbnailUrl = "",
            isVideoPausedInitially = true,
            isPlaying = isPlaying,


//        onContentDuration = {
//            totalDuration = it
//        },
//        onCurrentPosition = {
//            currentDuration = it
//        },
            //  clipRange = state.videoClipRange,
            enableAdvancedSettings = false
        )

        if (!isPlaying) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_play_button),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp).clip(CircleShape)
                        .noRippleClickable { togglePlayingState(true) }
                )
            }
        }
    }
}

@Composable
fun LoadingGridItem(height: Dp) {
    Box(modifier = Modifier.fillMaxWidth().height(height), contentAlignment = Alignment.Center) {
        Loader()
    }
}



@Composable
fun GridItems(modifier: Modifier, data: MediaItem, onClick:()->Unit, height:Dp,selectedItems:List<MediaItem>) {

    val isSelected = selectedItems.any { it.id == data.id }

    val image = if (data.type == MediaItem.Type.IMAGE){
        if (isAndroid()) data.image else data.id
    }else {
        if (isAndroid()) data.thumbnail else data.id
    }

//    LaunchedEffect(true){
//        println("the grid item is: for type : ${data.type.name} ${image != null}")
//    }

    var isLoading by remember { mutableStateOf(false) }
    if (image != null) {
        BoxWithConstraints(
            contentAlignment = Alignment.TopEnd,
            modifier = modifier

            // .padding(4.dp)
//            .aspectRatio(122.3f / 108f) // Maintain aspect ratio
        ) {
            val width = maxWidth.toPx().toInt()

            if (!isAndroid()){
                Box(
                    modifier = Modifier.fillMaxWidth().height(height)
                        .noRippleClickable(onClick),
                ) {
                    IosImageView(data.id, size = IntSize(width = width, height = height.toPx().toInt()), aspect = ImageAspect.ASPECT_FILL)
                }
            }
            else if (isAndroid() || image.instanceOf(String::class)) {
//            if (isAndroid() || image?.instanceOf(String::class)) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(height).background(Color(0xffEAECF0))
                        .noRippleClickable(onClick),
                    contentScale = ContentScale.Crop,
                    onLoading = {
                        isLoading = true
                    },
                    onSuccess = {
                        isLoading = false
                    }
                )
            }

            Box(
                modifier = Modifier.padding(top = 8.dp, end = 10.33.dp).size(24.dp)
                    .then(
                        if (isSelected) {
                            Modifier.background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xffF3335D),
                                        Color(0xffF33386)
                                    )
                                ), shape = CircleShape
                            )
                        } else Modifier
                    )
                    .border(1.dp, color = Color.White, shape = CircleShape)
                    .noRippleClickable(onClick),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {

                    Image(
                        imageVector = vectorResource(Res.drawable.ic_tick_mark),
                        modifier = Modifier.width(14.67.dp).height(11.dp),
                        contentDescription = null
                    )

//                Text(
//                    text = image.selectionIndex.toString(),
//                    fontFamily = PoppinsFontFamily(),
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 12.sp,
//                    lineHeight = 12.sp,
//                    letterSpacing = 0.2.sp,
//                    color = Color.White,
//                    textAlign = TextAlign.Center
//                )
                }
            }

//            if (isLoading) {
//                Box(modifier = Modifier.fillMaxWidth().height(height), contentAlignment = Alignment.Center) {
//                    Loader()
//                }
//            }


        }
    }

}


@Composable
fun TopHeaderSection(onBack:()->Unit,onSave: () -> Unit,text:String,btnText:String = stringResource(Res.string.save),isDarkMode: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(Modifier.fillMaxSize().noRippleClickable(onSave), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = btnText,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    color = Color(0xffF33358)
                )
            }

            Box(
                modifier = Modifier.fillMaxHeight().size(24.dp)
                    .noRippleClickable(onBack),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    modifier = Modifier.width(16.dp).height(12.dp),
                    imageVector = vectorResource(Res.drawable.ic_left),
                    contentDescription = "back",
                    tint = if (isDarkMode) Color.White else Color.Black
                )
            }



            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = text,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )
            }



        }
    }
    DropShadow()

}