package com.lovorise.app.reels.presentation.reels_create_upload_view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_tick_mark
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.next
import coinui.composeapp.generated.resources.photos
import coinui.composeapp.generated.resources.videos
import com.lovorise.app.MediaItem
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.profile_upload.LoadingGridItem
import com.lovorise.app.components.DropShadow
import com.lovorise.app.getImageById
import com.lovorise.app.getVideoById
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.camera.ImageAspect
import com.lovorise.app.libs.camera.IosImageView
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile_visitors.components.CustomTabRow
import com.lovorise.app.reels.presentation.states.CaptureRecordScreenState
import com.lovorise.app.reels.presentation.viewModels.CaptureRecordScreenModel
import com.lovorise.app.swipe.presentation.toPx
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ImageVideoPickerScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        // val reelsScreenModel = navigator.koinNavigatorScreenModel<ReelsScreenModel>()
        val screenModel = navigator.koinNavigatorScreenModel<CaptureRecordScreenModel>()
        val context = LocalPlatformContext.current
        LaunchedEffect(true){
            screenModel.onInit(null,context)
          //  screenModel.retryFetching()
        }


        val state by screenModel.state.collectAsState()

        ImageVideoPickerScreenContent(
            isDarkMode = isDarkMode,
            screenModel = screenModel,
            onBack = {
                navigator.pop()
            },
            onNext = {
                navigator.push(EditUploadSelectedOrCapturedImageVideoScreen())
            },
            state = state
        )
    }
}

@Composable
fun ImageVideoPickerScreenContent(isDarkMode:Boolean, onBack:()->Unit, onNext:()->Unit, screenModel: CaptureRecordScreenModel, state: CaptureRecordScreenState) {

   // var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(Res.string.photos), stringResource(Res.string.videos))

//    val gridState1 = rememberLazyGridState()
//    val gridState2 = rememberLazyGridState()

   // val ctx = LocalPlatformContext.current

    LaunchedEffect(state.selectedVideoIndex){
        screenModel.loadVideoIfRequired()
    }

    LaunchedEffect(state.selectedImageIndex){
        screenModel.loadImageIfRequired()
    }

    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalPlatformContext.current



    Column {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(Modifier
            .background(if (isDarkMode) BASE_DARK else Color.White)
            .fillMaxSize()
            .weight(1f)
        ) {
            HeaderSection(
                isDarkMode = isDarkMode,
                onBack = onBack,
                onNext = {
                    if (state.selectedImageIndex != -1 || state.selectedVideoIndex != -1) {
                       // val path = getPathForUiImage()
                        coroutineScope.launch {
                            if (state.selectedImageIndex != -1){
                                val mediaItem = screenModel.imagePaginationState.allItems?.getOrNull(state.selectedImageIndex)
                                val path = if (!mediaItem?.id.isNullOrBlank()) getImageById(mediaItem!!.id, context = context) else null
                                if (!path.isNullOrBlank()){
                                    screenModel.onCapturedImage(path)
                                    onNext()
                                }
                            }else{
                                val mediaItem = screenModel.videoPaginationState.allItems?.getOrNull(state.selectedVideoIndex)
                                val path = if (isAndroid()) mediaItem?.id else if (!mediaItem?.id.isNullOrBlank()) getVideoById(mediaItem!!.id) else null
                                if (!path.isNullOrBlank()){
                                    screenModel.onVideoRecordCompleted(path)
                                    onNext()
                                }
                            }


                        }
//                        println("the selected index ${screenModel.imagePaginationState.allItems?.get(state.selectedImageIndex)} ${state.selectedImageIndex} ${state.selectedVideoIndex}")


                    }
                },
                isNextButtonEnabled = state.selectedImageIndex != -1 || state.selectedVideoIndex != -1
            )

            CustomTabRow(
                titles = tabs,
                tabIndex = pagerState.currentPage,
                paddingValues = PaddingValues(top = 12.dp, start = 16.dp, end = 16.dp),
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                  //  tabIndex = index
                },
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(11.dp))

            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                val eachHeight = (maxWidth.value - 8).times(0.29).dp

                HorizontalPager(pagerState){ page ->

                    if (page == 0) {
                        PaginatedLazyVerticalGrid(
                            modifier = Modifier.padding(horizontal = 2.dp),
                            paginationState = screenModel.imagePaginationState,
                            newPageProgressIndicator = {
                                LoadingGridItem(height = eachHeight)
                            },
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            itemsIndexed(screenModel.imagePaginationState.allItems!!) { index, item ->
                                //  key(state.selectedItems){
                                GridItems(
                                    isSelected = state.selectedImageIndex == index,
                                    data = item,
                                    onClick = {
                                        screenModel.updateSectionImageIndex(index)
                                    },
                                    // isSelected = state.selectedItems.contains(item),
                                    height = eachHeight,
                                    modifier = Modifier
                                )

                                // }
                            }

                        }
                    }
                    if (page == 1){
                        PaginatedLazyVerticalGrid(
                            modifier = Modifier.padding(horizontal = 2.dp),
                            paginationState = screenModel.videoPaginationState,
                            newPageProgressIndicator = {
                                LoadingGridItem(height = eachHeight)
                            },
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            itemsIndexed(screenModel.videoPaginationState.allItems!!) { index, item ->
                                //  key(state.selectedItems){
                                GridItems(
                                    isSelected = state.selectedVideoIndex == index,
                                    data = item,
                                    onClick = {
                                        screenModel.updateSectionVideoIndex(index)
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

    if (state.isLoading || (screenModel.imagePaginationState.allItems.isNullOrEmpty() && pagerState.currentPage == 0) || (screenModel.videoPaginationState.allItems.isNullOrEmpty() && pagerState.currentPage == 1)){
        CircularLoader(true)
    }



}

@Composable
fun GridItems(modifier: Modifier,data:MediaItem,onClick:()->Unit,isSelected:Boolean,height: Dp) {

    val image = if (data.type == MediaItem.Type.IMAGE){
        if (isAndroid()) data.image else data.id
    }else {
        if (isAndroid()) data.thumbnail else data.id
    }

    LaunchedEffect(true){
        println("the grid item is: for type : $image  ${data.type.name} ${image != null}")
    }

    var isLoading by remember { mutableStateOf(false) }

    if (image != null) {
        BoxWithConstraints(
            contentAlignment = Alignment.TopEnd,
            modifier = modifier
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
                }
            }


        }
    }

}

@Composable
fun HeaderSection(onBack: () -> Unit,onNext: () -> Unit,isNextButtonEnabled:Boolean,isDarkMode: Boolean) {


    Row(
        modifier = Modifier.fillMaxWidth().height(55.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier.fillMaxHeight().size(24.dp)
                .noRippleClickable(onBack),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                tint = if (isDarkMode) Color.White else Color.Black,
                modifier = Modifier.width(16.dp).height(12.dp),
                imageVector = vectorResource(Res.drawable.ic_xmark),
                contentDescription = "back"
            )
        }
        Box(
            modifier = Modifier
                .height(24.dp)
                .wrapContentWidth()
                .background(Color(if (isNextButtonEnabled) 0xffF33358 else 0xffEAECF0), RoundedCornerShape(8.dp))
                .noRippleClickable(onNext),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = stringResource(Res.string.next),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                color = Color(if (isNextButtonEnabled) 0xffFFFFFF else 0xff98A2B3)
            )
        }


    }
    DropShadow()



}