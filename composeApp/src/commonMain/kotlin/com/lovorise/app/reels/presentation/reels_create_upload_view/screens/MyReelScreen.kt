package com.lovorise.app.reels.presentation.reels_create_upload_view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_to_favorites
import coinui.composeapp.generated.resources.are_you_sure_you_want_to_delete_this_reel
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.delete
import coinui.composeapp.generated.resources.delete_post
import coinui.composeapp.generated.resources.edit
import coinui.composeapp.generated.resources.ic_back_white
import coinui.composeapp.generated.resources.ic_bookmark_filled
import coinui.composeapp.generated.resources.ic_bookmark_outlined
import coinui.composeapp.generated.resources.ic_dot_menu
import coinui.composeapp.generated.resources.ic_double_checkmark_red
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_reels_settings
import coinui.composeapp.generated.resources.ic_share_outlined
import coinui.composeapp.generated.resources.ic_spotlight_message
import coinui.composeapp.generated.resources.ic_viewers
import coinui.composeapp.generated.resources.my_reel
import coinui.composeapp.generated.resources.no_view_yet
import coinui.composeapp.generated.resources.once_your_reel_gains_attention
import coinui.composeapp.generated.resources.removed_from_favorites
import coinui.composeapp.generated.resources.views
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.libs.media_player.VideoPlayerView
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.reels.domain.models.ReelContentType
import com.lovorise.app.reels.domain.models.UpdateReelRequest
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.OriginalAudio
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelPrivacySetting
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelSettingsBottomSheetContent
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ShareReelBottomSheetContent
import com.lovorise.app.reels.presentation.viewModels.ReelsScreenModel
import com.lovorise.app.settings.presentation.components.CustomDialogWithTextAndBodyAndActions
import com.lovorise.app.swipe.presentation.components.DescriptionProfile
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class MyReelScreen(private val reelsScreenModel: ReelsScreenModel,private val reelData: ReelData) : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()

        MyReelScreenContent(
            isDarkMode = isDarkMode,
            reelsScreenModel = reelsScreenModel,
            data = reelData,
            onBack = {
                navigator.pop()
            },
            navigateToUpdateReel = {
                navigator.push(UpdateReelScreen(reelsScreenModel,it))
            },
            profileScreenModel = profileScreenModel,
            navigateToSubscription = {
                navigator.push(PurchaseSubscriptionScreen(SubscriptionType.WEEKLY))
            }
//            navigateToCreateRecord = {
//                navigator.pop()
//            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReelScreenContent(navigateToSubscription:()->Unit,profileScreenModel: ProfileScreenModel,isDarkMode:Boolean,reelsScreenModel: ReelsScreenModel,data: ReelData,onBack:()->Unit,navigateToUpdateReel:(UpdateReelData)->Unit) {

    val shareReelBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reelsActionBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showReelsEditOptionsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showViewersBottomSheetSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showShareReelBottomSheet by remember { mutableStateOf(false) }
    var showViewersBottomSheet by remember { mutableStateOf(false) }
    var reelsActionBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var reelsPrivacySettings by remember { mutableStateOf(ReelPrivacySetting.Everyone) }
    var showReelsEditOptions by remember { mutableStateOf(false) }
    var showDeletePostConfirmationDialog by remember { mutableStateOf(false) }

    val reelsApiState by reelsScreenModel.state.collectAsState()

    var reelData by remember { mutableStateOf(data) }
    val context = LocalPlatformContext.current

    val addedToFavorites = stringResource(Res.string.add_to_favorites)
    val removedFromFavorites = stringResource(Res.string.removed_from_favorites)

    val profileState by profileScreenModel.state.collectAsState()

    Column {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) Color.Black else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .weight(1f)
        ) {
            if (reelData.mediaUrl != null) {
                Box {
                    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                        if (reelData.mediaType == ReelContentType.VIDEO) {
                            VideoPlayerView(
                                modifier = Modifier.fillMaxSize(),
                                url = reelData.mediaUrl!!,
                                thumbnailUrl = reelData.thumbnail ?: ""
                            )
                        }else{
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = reelData.mediaUrl,
                                contentScale = ContentScale.FillWidth,
                                contentDescription = null
                            )
                        }
                    }

                    Box(Modifier.fillMaxSize().padding(top = 21.dp)){
                        Row(Modifier.height(40.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {

                            Image(
                                modifier = Modifier.noRippleClickable(onBack),
                                imageVector = vectorResource(Res.drawable.ic_back_white),
                                contentDescription = null,
                            )

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = stringResource(Res.string.my_reel),
                                fontFamily = PoppinsFontFamily(),
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.2.sp,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 18.sp,
                                color = Color.White
                            )

                            Spacer(Modifier.weight(1f))

                            Image(
                                imageVector = vectorResource(Res.drawable.ic_reels_settings),
                                contentDescription = null,
                                modifier = Modifier.noRippleClickable {
                                    if (reelData.isMyReels) {
                                        showReelsEditOptions = true
                                    }
                                }
                            )

                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize().padding(bottom = 15.dp),
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
                            if(reelData.originalAudio) {
                                OriginalAudio()
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(bottom = 15.dp, end = 16.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {

                        Column(
                            Modifier.width(38.dp),
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

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    imageVector = vectorResource(Res.drawable.ic_viewers),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp).noRippleClickable {
                                        showViewersBottomSheet = true
                                    }
                                )
                                Text(
                                    text = data.views,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    lineHeight = 18.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.2.sp
                                )
                            }

                            if (!reelData.isMyReels) {
                                Image(
                                    imageVector = vectorResource(Res.drawable.ic_spotlight_message),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Image(
                                    imageVector = vectorResource(if (reelData.isFavourite) Res.drawable.ic_bookmark_filled else Res.drawable.ic_bookmark_outlined),
                                    contentDescription = null,
                                    modifier = Modifier.width(19.66.dp).height(25.67.dp)
                                        .noRippleClickable {

                                            reelsScreenModel.markMyReelFavoriteUnFavorite(
                                                context,
                                                reelData.id,
                                                !reelData.isFavourite,
                                                addedMsg = addedToFavorites,
                                                removedMsg = removedFromFavorites
                                            ) {
                                                reelData = reelData.copy(
                                                    isFavourite = !reelData.isFavourite
                                                )
                                            }
                                            //reelsScreenModel.updateFavoriteReels(reelData.id)
//                                                val newReels = reels.toMutableList().apply {
//                                                    val item = removeAt(index)
//
//                                                    add(
//                                                        index,
//                                                        item.copy(isFavourite = !item.isFavourite)
//                                                    )
//                                                    toastMessage = ""
//                                                    toastMessage = if (!item.isFavourite) {
//                                                        addedToFavorites
//                                                    } else {
//                                                        removedFromFavorites
//                                                    }
//                                                }
//                                                if (page == 0) {
//                                                    reelsForYou = newReels
//                                                } else {
//                                                    reelsFromMatches = newReels
//                                                }
                                        }
                                )
                                if (data.favCounter != null) {
                                    Text(
                                        text = data.favCounter.toString(),
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        lineHeight = 18.sp,
                                        fontFamily = PoppinsFontFamily(),
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = 0.2.sp
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    imageVector = vectorResource(Res.drawable.ic_share_outlined),
                                    contentDescription = null,
                                    modifier = Modifier.width(21.27.dp).height(19.63.dp)
                                        .noRippleClickable {
                                            showShareReelBottomSheet = true
                                        }
                                )
                                if (data.shareCounter != null) {
                                    Text(
                                        text = data.shareCounter.toString(),
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        lineHeight = 18.sp,
                                        fontFamily = PoppinsFontFamily(),
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = 0.2.sp
                                    )
                                }
                            }

                            Image(
                                imageVector = vectorResource(Res.drawable.ic_dot_menu),
                                contentDescription = null,
                                modifier = Modifier.width(24.dp).height(18.91.dp)
                                    .noRippleClickable {
                                        if (reelData.isMyReels) {
                                            reelsActionBottomSheet = true
                                        }
                                    }
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
                .background(if (isDarkMode) Color.Black else Color.White)
        )

    }

    if (reelsActionBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = reelsActionBottomSheetState,
            onDismissRequest = {
                reelsActionBottomSheet = false
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ReelsActionBottomSheetContent(
                isDarkMode = isDarkMode,
                onEditClick = {
                    coroutineScope.launch {
                        reelsActionBottomSheetState.hide()
                    }.invokeOnCompletion {
                        navigateToUpdateReel(UpdateReelData(reelId = data.id, privacySetting = data.privacySetting!!, caption = data.caption ?: "", mediaType = data.mediaType!!, mediaUrl = data.mediaUrl!!))
                    }
                },
                onDelete = {
                    coroutineScope.launch {
                        reelsActionBottomSheetState.hide()
                    }.invokeOnCompletion {
                        reelsActionBottomSheet = false
                        showDeletePostConfirmationDialog = true
                    }
                }
            )
        }
    }

    if (showShareReelBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = shareReelBottomSheetState,
            onDismissRequest = {
                showShareReelBottomSheet = false
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ){

            ShareReelBottomSheetContent(
                isDarkMode = isDarkMode,
                onToastMessage = {
                   // tabsScreenModel.updateToastMessage(it)
                },
                hideBottomSheet = {
                    coroutineScope.launch {
                        shareReelBottomSheetState.hide()
                    }.invokeOnCompletion {
                        showShareReelBottomSheet = false
                    }
                },
                onShareButtonClick = {
                    coroutineScope.launch {
                        shareReelBottomSheetState.hide()
                    }.invokeOnCompletion {
                        showShareReelBottomSheet = false
                    }
                }
            )
        }
    }

    if (showReelsEditOptions){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = showReelsEditOptionsSheetState,
            onDismissRequest = {
                showReelsEditOptions = false
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ReelSettingsBottomSheetContent(
                isDarkMode = isDarkMode,
                privacySettings = reelsPrivacySettings,
                onDone = {
                    coroutineScope.launch {
                        showReelsEditOptionsSheetState.hide()
                        if (reelData.privacySetting != null && reelsPrivacySettings != reelData.privacySetting) {
                            reelsScreenModel.updateReel(
                                context,
                                UpdateReelRequest(
                                    reelData.id,
                                    caption = reelData.caption ?: "",
                                    privacySetting = reelsPrivacySettings.formatted()
                                )
                            )
                        }
                    }.invokeOnCompletion {
                        showReelsEditOptions = false
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

    if (showViewersBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = showViewersBottomSheetSheetState,
            onDismissRequest = {
                showViewersBottomSheet = false
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ViewersBottomSheetContent(
                isDarkMode = isDarkMode,
//                data = List(20){ViewersData(
//                    imageUrl = "https://files.lovorise.org/reels/1733077039503625349_medium_new_thumbnail.jpg",
//                    name = "Random name $it",
//                    date = "Today at 6:59"
//                )},
                data = emptyList(),
                onBack = {
                    coroutineScope.launch {
                        showViewersBottomSheetSheetState.hide()
                    }.invokeOnCompletion {
                        showViewersBottomSheet = false
                    }
                }
            )
        }
    }

    if (showDeletePostConfirmationDialog){
        CustomDialogWithTextAndBodyAndActions(
            onAction1 = {
                showDeletePostConfirmationDialog = false
            },
            onAction2 = {
                showDeletePostConfirmationDialog = false
                reelsScreenModel.deleteReel(context, reelId = reelData.id){
                    onBack()
                }
            },
            title = stringResource(Res.string.delete_post),
            actionText2 = stringResource(Res.string.delete),
            actionText1 = stringResource(Res.string.cancel),
            onCancel = {
                showDeletePostConfirmationDialog = false
            },
            body = buildAnnotatedString { append(stringResource(Res.string.are_you_sure_you_want_to_delete_this_reel))}
        )
    }

    if (reelsApiState.isLoading){
        CircularLoader()
    }


}

@Composable
fun ViewersBottomSheetContent(isDarkMode: Boolean,data: List<ViewersData>,onBack:()->Unit) {


    @Composable
    fun ViewerItem(data: ViewersData) {

        Row(Modifier.height(41.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = data.imageUrl,
                contentScale = ContentScale.Crop,
                clipToBounds = true,
                modifier = Modifier.size(40.dp).clip(CircleShape),
                contentDescription = null
            )

            Column {
                Text(
                    text = data.name,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.weight(1f))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_double_checkmark_red),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = data.date,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color(0xff475467)
                    )
                }
            }
        }


    }


    BoxWithConstraints {
        Column(
            modifier = Modifier.fillMaxHeight(if (data.isEmpty()) 0.5f else 0.9f)
        ) {

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .height(2.dp)
                            .width(40.dp)
                            .background(Color(0xff667085))
                    )
                }

                Spacer(Modifier.height(8.dp))


                Row(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(24.dp).noRippleClickable(onBack), contentAlignment = Alignment.CenterStart){
                        Image(
                            imageVector = vectorResource(Res.drawable.ic_left),
                            contentDescription = null,
                            modifier = Modifier.height(12.dp).width(16.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (data.isEmpty()) stringResource(Res.string.views) else "Total ${data.size} views",
                        fontFamily = PoppinsFontFamily(),
                        color = Color(0xff101828),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.2.sp,
                        fontSize = 16.sp
                    )
                }

                if (data.isEmpty()){
                    Spacer(Modifier.height(40.dp))

                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(Res.string.no_view_yet),
                            fontFamily = PoppinsFontFamily(),
                            letterSpacing = 0.2.sp,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color(0xff101828),
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = stringResource(Res.string.once_your_reel_gains_attention),
                            fontFamily = PoppinsFontFamily(),
                            letterSpacing = 0.2.sp,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color(0xff344054),
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )

                    }
                }else{
                    Spacer(Modifier.height(8.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(data){
                            ViewerItem(it)
                        }
                        item {
                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }





            }

            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(if (isDarkMode) Color.Black else Color.White)
            )


        }
    }
}





@Composable
fun ReelsActionBottomSheetContent(isDarkMode: Boolean,onEditClick:()->Unit,onDelete:()->Unit) {
    Column {

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .background(Color(0xff667085))
                )
            }

            Spacer(Modifier.height(16.dp))


            Text(
                modifier = Modifier.noRippleClickable(onEditClick),
                text = stringResource(Res.string.edit),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp,
                color = Color(0xff101828),
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.noRippleClickable(onDelete),
                text = stringResource(Res.string.delete),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp,
                color = Color(0xffF33358),
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
            )





            Spacer(Modifier.height(16.dp))




        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )


    }

}


data class ViewersData(
    val imageUrl:String,
    val name:String,
    val date:String
)