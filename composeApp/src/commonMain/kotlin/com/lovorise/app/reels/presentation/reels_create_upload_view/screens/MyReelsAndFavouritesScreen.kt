package com.lovorise.app.reels.presentation.reels_create_upload_view.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import coinui.composeapp.generated.resources.add_to_favorites
import coinui.composeapp.generated.resources.create_reel
import coinui.composeapp.generated.resources.discover_amazing_reels_and_favorite
import coinui.composeapp.generated.resources.explore
import coinui.composeapp.generated.resources.ic_bookmark_filled
import coinui.composeapp.generated.resources.ic_bookmark_outlined
import coinui.composeapp.generated.resources.ic_camera_black
import coinui.composeapp.generated.resources.ic_create_reels
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_no_favorites
import coinui.composeapp.generated.resources.ic_viewers
import coinui.composeapp.generated.resources.my_favourites
import coinui.composeapp.generated.resources.my_reels
import coinui.composeapp.generated.resources.no_favorites_yet
import coinui.composeapp.generated.resources.reel_uploaded_successfully
import coinui.composeapp.generated.resources.reels
import coinui.composeapp.generated.resources.removed_from_favorites
import coinui.composeapp.generated.resources.start_sharing_your_moment_creating_first_reel
import coinui.composeapp.generated.resources.uploading
import coinui.composeapp.generated.resources.you_have_not_created_any_reels_yet
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile_visitors.components.CustomTabRow
import com.lovorise.app.reels.domain.models.MyReelsResponse
import com.lovorise.app.reels.domain.models.ReelContentType
import com.lovorise.app.reels.domain.models.ReelsResponse
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelPrivacySetting
import com.lovorise.app.reels.presentation.states.ReelsApiState
import com.lovorise.app.reels.presentation.viewModels.ReelsScreenModel
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class MyReelsAndFavouritesScreen : Screen {


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val reelsScreenModel = navigator.koinNavigatorScreenModel<ReelsScreenModel>()
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()
        val reelsApiState by reelsScreenModel.state.collectAsState()
        val context = LocalPlatformContext.current
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(true){
            if (reelsApiState.myReels.reels.isNullOrEmpty()){
                reelsScreenModel.getMyReels(context,1)
            }
            if (reelsApiState.favoriteReels?.reels.isNullOrEmpty()){
                reelsScreenModel.getFavoriteReels(context,1)
            }
            val processingReelData = reelsApiState.processingReel
            if (processingReelData != null){
                reelsScreenModel.createReel(context,processingReelData.file,processingReelData.uploadData,processingReelData.createData){
                    coroutineScope.launch {
                        reelsScreenModel.updateUploadingState(false)
                        reelsScreenModel.updateProcessingReel(null)
//                        reelsScreenModel.getMyReels(context,1){
//
//                        }
                    }
                }
            }
        }

        MyReelsAndFavouritesScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                navigator.popUntil { it.instanceOf(CaptureRecordScreen::class) }
            },
            navigateToCreate = {
                navigator.push(CaptureRecordScreen())
            },
            reelsScreenModel = reelsScreenModel,
            reelsApiState = reelsApiState,
            navigateToMyReels = {
                navigator.push(MyReelScreen(reelsScreenModel,it))
            },
            navigateToReels = {
                tabsScreenModel.updateTab(TabsScreenModel.BottomTab.REELS)
                navigator.popUntil {
                    it.instanceOf(HomeScreen::class)
                }
            }
        )
    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun MyReelsAndFavouritesScreenContent(isDarkMode:Boolean,onBack:()->Unit,navigateToCreate:()->Unit,reelsScreenModel: ReelsScreenModel,reelsApiState: ReelsApiState,navigateToMyReels:(ReelData)->Unit,navigateToReels:()->Unit) {


    val tabs = listOf(stringResource(Res.string.my_reels),stringResource(Res.string.my_favourites))
    var tabIndex by remember { mutableStateOf(0) }

    val myReels = (reelsApiState.myReels.reels?.filterNotNull() ?: emptyList()).mapNotNull { it.toReelData(userData = null, originalAudio = true, type = it.reelsContent?.mediaType)}
    val myFavourites = (reelsApiState.favoriteReels?.reels?.filterNotNull() ?: emptyList()).mapNotNull { it.toReelData(userData = it.userData)}

    LaunchedEffect(myFavourites){
        println("the userdata is ${myFavourites.firstOrNull()?.userData}")
    }
    val context = LocalPlatformContext.current

    val addedToFavorites = stringResource(Res.string.add_to_favorites)
    val removedFromFavorites = stringResource(Res.string.removed_from_favorites)

    BackHandler(true){
        onBack()
    }


    Column {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Box(modifier = Modifier
            .background(if (isDarkMode) BASE_DARK else Color.White)
            .fillMaxSize()
            .weight(1f)) {
            Column(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .fillMaxSize()
                   // .weight(1f)
                ,
            ) {

                HeaderSection(
                    isDarkMode = isDarkMode,
                    onBack = onBack,
                    onCameraIconClick = navigateToCreate
                )

                Spacer(Modifier.height(12.dp))

                CustomTabRow(
                    titles = tabs,
                    tabIndex = tabIndex,
                    count1 = myReels.size,
                    count2 = myFavourites.size,
                    paddingValues = PaddingValues(top = 12.dp, start = 16.dp, end = 16.dp),
                    onTabSelected = { index ->
                        tabIndex = index
                    },
                    isDarkMode = isDarkMode
                )


                Spacer(Modifier.height(12.dp))

                when (tabIndex) {
                    0 -> {
                        if (myReels.isEmpty() && !reelsApiState.isLoading) {
                            NoReelsCreated(
                                navigateToCreateRecordScreen = navigateToCreate,
                                isDarkMode = isDarkMode
                            )
                        } else {
                            MyReelsAndMyFavouritesGrid(
                                reels = myReels,
                                onItemClick = {
                                    navigateToMyReels(it)
                                },
                                onUpdateReel = {
                                    reelsScreenModel.markReelFavoriteUnFavorite(
                                        context = context,
                                        reelId = it.id,
                                        value = !it.isFavourite,
                                        removedMsg = removedFromFavorites,
                                        addedMsg = addedToFavorites
                                    )
                                    //  reelsScreenModel.getMyReels(context,1)
//                        val index = myReels.indexOf(it)
//                        myReels = myReels.toMutableList().apply {
//                            add(index,removeAt(index).copy(isFavourite = !it.isFavourite))
//                        }
                                },
                                processingReel = reelsApiState.processingReel
                            )
                        }

                    }

                    1 -> {
                        if (myFavourites.isEmpty() && !reelsApiState.isLoading) {
                            NoFavoriteReels(isDarkMode = isDarkMode) {
                                navigateToReels()
                            }
                        }
                        MyReelsAndMyFavouritesGrid(
                            reels = myFavourites,
                            onItemClick = {
                                navigateToMyReels(it)
                            },
                            onUpdateReel = {
                                reelsScreenModel.markReelFavoriteUnFavorite(
                                    context = context,
                                    reelId = it.id,
                                    value = !it.isFavourite,
                                    removedMsg = removedFromFavorites,
                                    addedMsg = addedToFavorites
                                )
//                        myFavourites = myFavourites.toMutableList().apply {
//                            remove(it)
//                        }
                            },
                            processingReel = null
                        )

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

    if (reelsApiState.isLoading && reelsApiState.processingReel == null){
        CircularLoader(true)
    }
    if (reelsApiState.processingReel != null){
        ReelUploadingAndUploadedState(reelsApiState.isUploading)
    }

}

@Composable
fun ReelUploadingAndUploadedState(isUploading:Boolean) {
    Box(Modifier.fillMaxSize().padding(top = 69.dp).noRippleClickable {  }, contentAlignment = Alignment.TopCenter) {
        Box(Modifier.fillMaxWidth().padding(horizontal = 41.dp).height(35.dp).background(Color(0xff344054).copy(alpha = 0.85f), shape = RoundedCornerShape(4.dp))){
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                if (isUploading){
                    Loader()
                    Spacer(Modifier.width(16.dp))
                }

                Text(
                    text = stringResource(if (isUploading) Res.string.uploading else Res.string.reel_uploaded_successfully),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                 //   lineHeight = 21.sp,
                    color = Color.White,
                    letterSpacing = 0.1.sp,
                    textAlign = TextAlign.Center,
                )

            }

        }


    }

}

@Composable
fun Loader() {
    // Infinite animation for rotating the loader
    val infiniteTransition = rememberInfiniteTransition()
    val angleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Canvas(modifier = Modifier.size(22.dp)) {
        // Static arc (background arc)
        drawArc(
            color = Color(0xffA8A8A9), // Static background color
            startAngle = 0f,
            sweepAngle = 360f, // Full circle for the background
            useCenter = false,
            style = Stroke(width = 3.dp.toPx())
        )

        // Moving arc (foreground)
        drawArc(
            color = Color(0xffF33358), // Moving color
            startAngle = angleOffset,
            sweepAngle = 90f, // Adjust for how much of the arc moves
            useCenter = false,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Composable
fun NoFavoriteReels(isDarkMode: Boolean,navigateToReels:()->Unit) {
    Box(
        Modifier.fillMaxSize().padding(horizontal = 19.dp).noRippleClickable {},
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {

            Spacer(Modifier.weight(1f))

            Box(
                Modifier.size(58.dp)
                    .clip(
                       // Color(0xffFF7791).copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    imageVector = vectorResource(Res.drawable.ic_no_favorites),
                    contentDescription = null,
                //    modifier = Modifier.size(26.1.dp)
                )

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.no_favorites_yet),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = if (isDarkMode) Color.White else Color(0xff101828),
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.discover_amazing_reels_and_favorite),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(0.5f)
                    .background(Color(0xffF33358), shape = RoundedCornerShape(16.dp))
                    .noRippleClickable(navigateToReels),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 31.dp),
                    text = stringResource(Res.string.explore),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
            Spacer(Modifier.weight(1.7f))

        }
    }
}


@Composable
fun NoReelsCreated(navigateToCreateRecordScreen:()->Unit,isDarkMode: Boolean) {
    Box(
        Modifier.fillMaxSize().padding(horizontal = 19.dp).noRippleClickable {},
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {

            Spacer(Modifier.weight(1f))

            Box(
                Modifier.size(58.dp)
                    .background(
                        Color(0xffFF7791).copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    imageVector = vectorResource(Res.drawable.ic_create_reels),
                    contentDescription = null,
                    modifier = Modifier.size(26.1.dp)
                )

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.you_have_not_created_any_reels_yet),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = if (isDarkMode) Color.White else Color(0xff101828),
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.start_sharing_your_moment_creating_first_reel),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(0.5f)
                    .background(Color(0xffF33358), shape = RoundedCornerShape(16.dp))
                    .noRippleClickable(navigateToCreateRecordScreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                   // modifier = Modifier.padding(horizontal = 31.dp),
                    text = stringResource(Res.string.create_reel),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

            Spacer(Modifier.weight(1.7f))

        }
    }
}

@Composable
fun HeaderSection(onBack: () -> Unit,onCameraIconClick:()->Unit,isDarkMode: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().height(55.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier.fillMaxHeight().size(24.dp)
                    .noRippleClickable(onBack),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    tint = if (isDarkMode) Color.White else Color.Black,
                    modifier = Modifier.width(18.dp).height(14.dp),
                    imageVector = vectorResource(Res.drawable.ic_left),
                    contentDescription = "back"
                )
            }



            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(Res.string.reels),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )
            }

            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
                Box(
                    modifier = Modifier.fillMaxHeight().size(24.dp)
                        .noRippleClickable(onCameraIconClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        tint = if (isDarkMode) Color.White else Color.Black,
                        modifier = Modifier.width(24.dp).height(22.77.dp),
                        imageVector = vectorResource(Res.drawable.ic_camera_black),
                        contentDescription = "camera"
                    )
                }
            }



        }


    }
}




@Composable
fun MyReelsAndMyFavouritesGrid(modifier: Modifier = Modifier,reels:List<ReelData>,onUpdateReel:(ReelData)->Unit,onItemClick: (ReelData) -> Unit,processingReel: ReelsApiState.ProcessingReel?) {

    val gridState = rememberLazyGridState()

    LaunchedEffect(true){
        println("the reel data is $reels my reels processing $processingReel")
    }

    Box {
        LazyVerticalGrid(
            state = gridState,
            modifier = modifier.padding(horizontal =  20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            if (processingReel != null){
//                item {
//                    ReelInfoCard(
//                        modifier = Modifier.animateItem(),
//                        ReelData(
//                            id = 0, isMyReels = true, isFavourite = false,
//                            views = "0",
//                            thumbnail = processingReel.file,
//                            mediaUrl = processingReel.file,
//                            caption = processingReel.createData.caption,
//                            userData = null,
//                            originalAudio = false,
//                            mediaType = processingReel.uploadData.mediaType,
//                            privacySetting = null,
//                            watchCounter = 0,
//                            favCounter = 0,
//                            shareCounter = 0
//                        ),
//                        onFavouriteIconClick = {},
//                        onItemClick = {},
//                        isProcessing = true
//                    )
//                }
//            }
            items(reels) { reel ->
                ReelInfoCard(
                    modifier = Modifier.animateItem(),
                    reel,
                    onFavouriteIconClick = {onUpdateReel(reel)},
                    onItemClick = { onItemClick(reel) },
                    isProcessing = reel.id == processingReel?.createData?.contentId
                )
            }

            item {
                Spacer(Modifier.height(10.dp))
            }

        }

    }
}

@Composable
fun ReelInfoCard(modifier: Modifier,data: ReelData,onFavouriteIconClick:()->Unit,onItemClick:()->Unit,isProcessing:Boolean) {
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier.height(196.dp).fillMaxWidth().background(Color(0xffEAECF0),RoundedCornerShape(8.dp)).clip(RoundedCornerShape(8.dp)).noRippleClickable(onItemClick)) {
        Box(Modifier.fillMaxSize()){
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(Color.Red)
                  //  .clip(RoundedCornerShape(8.dp))
                ,
                model = data.thumbnail,
                contentScale = ContentScale.Crop,
                contentDescription = "",
                onLoading = {
                    isLoading = true
                },
                onSuccess = {
                    isLoading = false
                },
                onError = {
                    isLoading = false
                }
            )


        }

        if (isLoading && !isProcessing){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Loader()
            }
        }

        Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)), contentAlignment = Alignment.BottomCenter){
            Box(
                Modifier.fillMaxWidth().height(41.dp).background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
//                            Color(0xff000000).copy(alpha = 0f),
//                            Color(0xff000000).copy(alpha = 0.05f),
//                            Color(0xff000000).copy(alpha = 0.10f),
//                            Color(0xff000000).copy(alpha = 0.25f),
//                            Color(0xff000000).copy(alpha = 0.50f),
                            Color(0xff000000).copy(alpha = 0.70f),
                        ),
                     //   startY = 70f
                    )
                )
            )
            Row(Modifier.padding(bottom = 8.5.dp, start = 8.dp, end = 8.dp).height(28.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_viewers),
                    contentDescription = null,
                    modifier = Modifier.width(25.67.dp).height(19.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = data.views,
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 18.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.weight(1f))

                Image(
                    imageVector = vectorResource(if (data.isFavourite) Res.drawable.ic_bookmark_filled else Res.drawable.ic_bookmark_outlined),
                    contentDescription = null,
                    modifier = Modifier.width(18.dp).height(23.5.dp).noRippleClickable(onFavouriteIconClick)
                )

            }
        }




        if (isProcessing){
            Box(Modifier.fillMaxSize().background(Color(0xffD9D9D9).copy(alpha = 0.5f),RoundedCornerShape(8.dp)))
        }
    }


}

data class ReelData(
    val id:Int,
    val views: String,
    val isFavourite:Boolean = false,
    val thumbnail:String?,
    val mediaUrl:String?,
    val caption:String?,
    val userData:ReelsResponse.ReelDataItem.UserData?,
    val originalAudio:Boolean,
    val mediaType:ReelContentType?,
    val privacySetting:ReelPrivacySetting?,
    val isMyReels:Boolean,
    val watchCounter:Int?,
    val favCounter:Int?,
    val shareCounter:Int?
)

//"https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg?auto=compress&cs=tinysrgb&w=800"

fun MyReelsResponse.MyReelItem.toReelData(userData: ReelsResponse.ReelDataItem.UserData?,originalAudio: Boolean,type: ReelContentType?):ReelData?{
    val reelId = id ?: return null
    val thumbnail = reelsContentProcessed?.firstOrNull { it?.quality == "high" || it?.quality == "medium" }?.thumbnailUrl ?: reelsContent?.mediaUrl ?: return null
    val mediaUrl = reelsContentProcessed?.firstOrNull { it?.quality == "high" || it?.quality == "medium" }?.mediaUrl ?: reelsContent?.mediaUrl ?: return null

    return ReelData(
        id = reelId,
        thumbnail = thumbnail,
        isFavourite = false,
        views = (watchCounter ?: 0).toString(),
        userData = userData,
        originalAudio = originalAudio,
        caption = caption,
        mediaUrl = mediaUrl,
        mediaType = type,
        privacySetting = if (!privacySetting.isNullOrBlank()) try { ReelPrivacySetting.valueOf(privacySetting) }catch (e:Exception){ReelPrivacySetting.Everyone} else ReelPrivacySetting.Everyone,
        isMyReels = true,
        watchCounter = watchCounter ?: 0,
        shareCounter = shareCounter ?: 0,
        favCounter = favoriteCounter ?: 0
    )
}

fun ReelsResponse.ReelDataItem.toReelData(userData: ReelsResponse.ReelDataItem.UserData?):ReelData?{
    val id = reelId ?: return null
    val thumbnail = content?.thumbnailUrl ?: return null
    val mediaUrl = content.mediaUrl ?: return null

    return ReelData(
        id = id,
        thumbnail = thumbnail,
        isFavourite = isFavorite,
        views = 0.toString(),
        userData = userData,
        mediaUrl = mediaUrl,
        caption = caption,
        originalAudio = originalAudio ?: false,
        mediaType = mediaType,
        privacySetting = null,
        isMyReels = false,
        watchCounter = null,
        favCounter = null,
        shareCounter = null
    )
}