package com.lovorise.app.swipe.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.basics
import coinui.composeapp.generated.resources.bio
import coinui.composeapp.generated.resources.ic_casual
import coinui.composeapp.generated.resources.ic_confused
import coinui.composeapp.generated.resources.ic_dot_menu
import coinui.composeapp.generated.resources.ic_drink
import coinui.composeapp.generated.resources.ic_education
import coinui.composeapp.generated.resources.ic_family_planning
import coinui.composeapp.generated.resources.ic_gender
import coinui.composeapp.generated.resources.ic_heart
import coinui.composeapp.generated.resources.ic_heart_arrow
import coinui.composeapp.generated.resources.ic_height_scale
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_marriage_ring
import coinui.composeapp.generated.resources.ic_nav_1
import coinui.composeapp.generated.resources.ic_new_friend
import coinui.composeapp.generated.resources.ic_pets
import coinui.composeapp.generated.resources.ic_profession
import coinui.composeapp.generated.resources.ic_rollback
import coinui.composeapp.generated.resources.ic_smoke
import coinui.composeapp.generated.resources.interests
import coinui.composeapp.generated.resources.languages
import coinui.composeapp.generated.resources.report_has_been_sent
import com.lovorise.app.MediaItem
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.components.NoInternet
import com.lovorise.app.components.Toast
import com.lovorise.app.filters.FilterScreen
import com.lovorise.app.home.isNull
import com.lovorise.app.libs.connectivity.ConnectivityViewModel
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreen
import com.lovorise.app.swipe.presentation.components.AddMorePhotos
import com.lovorise.app.swipe.presentation.components.BlockConfirmationDialog
import com.lovorise.app.swipe.presentation.components.ChipItem
import com.lovorise.app.swipe.presentation.components.DatingPrefCardInfo
import com.lovorise.app.swipe.presentation.components.DescriptionProfile
import com.lovorise.app.swipe.presentation.components.LookingForSection
import com.lovorise.app.swipe.presentation.components.NoProfilesLeft
import com.lovorise.app.swipe.presentation.components.ReportOrBlockDropDownMenu
import com.lovorise.app.swipe.presentation.components.ReportProfileBottomSheet
import com.lovorise.app.swipe.presentation.components.RippleAnimation
import com.lovorise.app.swipe.presentation.components.SendUnlockRequestDialog
import com.lovorise.app.swipe.presentation.components.SwipeActionButtons
import com.lovorise.app.swipe.presentation.components.SwipeCard
import com.lovorise.app.swipe.presentation.components.SwipeLeftRightGuide
import com.lovorise.app.swipe.presentation.components.SwipeType
import com.lovorise.app.swipe.presentation.components.TopBarWithFilter
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class SwipeLeftRightScreen(private val navigator: Navigator,private val accountsViewModel: AccountsViewModel,private val isAccountPaused:Boolean,private val blurProfile:Boolean): Tab{

    @Composable
    override fun Content() {
        val screenModel = navigator.koinNavigatorScreenModel<SwipeScreenModel>()
        val context = LocalPlatformContext.current

        val connectivityViewModel = navigator.koinNavigatorScreenModel<ConnectivityViewModel>()
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())


        LaunchedEffect(true){
            screenModel.updateShowGuideState(context)
        }

        SwipeLeftRightScreenContent(
            swipeScreenModel = screenModel,
            isDarkMode = isDarkMode,
            navigator = navigator,
            accountsViewModel = accountsViewModel,
            isAccountPaused = isAccountPaused,
            navigateToFiltersScreen = {
                navigator.push(FilterScreen())
            },
            blurProfile = blurProfile,
            connectivityViewModel = connectivityViewModel
        )
    }

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "swipe",
            icon = painterResource(Res.drawable.ic_nav_1)
        )

    companion object{
        val datingPrefsData = listOf(
            DatingPrefCardInfo(
                bgColor = Color(0xffF9E9EC),
                txtColor = Color(0xffF33358),
                drawableResource = Res.drawable.ic_heart_arrow,
                text = "Life partner",
                endPadding = 57.dp
            ),
            DatingPrefCardInfo(
                bgColor= Color(0xffFFEDF5),
                txtColor= Color(0xffC10E5A),
                drawableResource= Res.drawable.ic_heart,
                text = "Long-term relationship",
                endPadding = 14.dp
            ),
            DatingPrefCardInfo(
                bgColor= Color(0xffFFEFEB),
                txtColor= Color(0xffBA2C0C),
                drawableResource= Res.drawable.ic_marriage_ring,
                text = "Marriage",
                endPadding = 72.dp
            ),
            DatingPrefCardInfo(
                bgColor= Color(0xffE9FCFF),
                txtColor= Color(0xff009AB2),
                drawableResource= Res.drawable.ic_casual,
                text = "Casual meets",
                endPadding = 36.dp
            ),
            DatingPrefCardInfo(
                bgColor= Color(0xffE4EDFF),
                txtColor= Color(0xff1962C2),
                drawableResource= Res.drawable.ic_new_friend,
                text = "New friends",
                endPadding = 40.dp
            ),
            DatingPrefCardInfo(
                bgColor= Color(0xffFFF5D8),
                txtColor= Color(0xffA76A00),
                drawableResource= Res.drawable.ic_confused,
                text = "Still figuring it out",
                endPadding = 11.dp
            )
        )
    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SwipeLeftRightScreenContent(
    swipeScreenModel: SwipeScreenModel,
    isDarkMode:Boolean,
    navigator: Navigator,
    accountsViewModel: AccountsViewModel,
    isAccountPaused: Boolean,
    navigateToFiltersScreen:()->Unit,
    blurProfile:Boolean,
    connectivityViewModel: ConnectivityViewModel
) {

    val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()

    val accountsState by accountsViewModel.state.collectAsState()

    val state by swipeScreenModel.state.collectAsState()

    val scrollState = rememberScrollState()

    var showAnimation by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            showAnimation = true
        } else {
            delay(1000) // Adjust delay to match animation smoothness
            showAnimation = false
        }
    }

   // val name by remember { mutableStateOf("Hannah Baker") }

    val coroutineScope = rememberCoroutineScope()

    val addedPhotosCount = (accountsState.user?.medias?.size) ?: 0

    val sheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val animatedSpacing by animateDpAsState(state.spacing)

//    val currentIndexState = remember { mutableStateOf(state.profiles.lastIndex) }



    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollPosition ->
                val threshold = (scrollState.maxValue * 0.1).toInt()
                val spacing = when (scrollPosition) {
                    in 0 ..74 -> {
                        (74 - scrollPosition).dp
                    }
                    in 75 .. 158 -> {
                        (158-scrollPosition).dp
                    }
                    else -> {
                        0.dp
                    }
                }
                swipeScreenModel.updateSpacingValue(spacing)
                swipeScreenModel.updateShowTopBarState(scrollPosition > threshold)
            }
    }


    val context = LocalPlatformContext.current

    val pagerStates = state.profiles.map { rememberPagerState(0) { it.user.medias?.size ?: 0 } }

    LaunchedEffect(true){
        if (state.profiles.isNotEmpty() && pagerStates.none { it.currentPage > 0 } && !state.isSwipeGuideShown) {
            delay(200)
            swipeScreenModel.updateShowSwipeGuide(true)
            swipeScreenModel.updateIsSwipeGuideShown(true)
        }
    }

    LaunchedEffect(true){
        swipeScreenModel.getUserProfiles(context)
    }

    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var hideOverlays by rememberSaveable { mutableStateOf(false) }

    var showUnlockDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pagerStates.getOrNull(currentIndex)?.currentPage){
        val page = pagerStates.getOrNull(currentIndex)?.currentPage
        if (page != null && page >= 2){
            showUnlockDialog = true
        }
    }

    BoxWithConstraints {
        val w = maxWidth
        val h = maxHeight
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .verticalScroll(scrollState, enabled = !state.showSwipeGuide || !accountsState.currentLocation.isNull() || blurProfile)
        ) {

            Spacer(Modifier.height(35.dp))

            AnimatedVisibility(
                visible = showAnimation && !isAccountPaused,
                enter = fadeIn(animationSpec = tween(200)), // Smooth fade-in
                exit = fadeOut(animationSpec = tween(300))  // Smooth fade-out
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    RippleAnimation(
                        modifier = Modifier
                            .fillMaxSize()
                            .requiredSize(width = w, height = h-35.dp)
                    )
                }
            }

            if (!state.isLoading && !blurProfile) {

                Box(
                    modifier = Modifier
                        .height(h-45.dp)
                        .fillMaxWidth()
                ) {
                    state.profiles.forEachIndexed { index, profile ->

                        Box {
                            currentIndex = index

                            SwipeCard(
                                swipeEnabled = !state.showTopBar,
                                onSwipeRight = {
                                    swipeScreenModel.removeItem(profile, swipeType = SwipeType.INTERESTED,context)
                                },
                                onSwipeLeft = {
                                    swipeScreenModel.removeItem(profile, swipeType = SwipeType.NOT_INTERESTED,context)
                                },
                                presentedDialogs = state.presentedDialogs,
                                onPresentedDialog = {
                                    swipeScreenModel.updatePresentedDialogsType(it, context)
                                },
                                onSkippedTemporary = {
                                    swipeScreenModel.removeItem(profile,null, context)
                                    swipeScreenModel.updateTempSkipState(false)
                                },
                                skipTemporary = state.skipTemporarily,
                                like = state.likeProfile,
                                dislike = state.dislikeProfile,
                                offsetY = profile.offsetY,
                                globalOffsetX = profile.offsetX,
                                updateOffsetY = { value, id ->
                                    swipeScreenModel.updateOffsetY(value = value, id = id)
                                },
                                onCancelSkip = {
                                    swipeScreenModel.updateTempSkipState(false)
                                },
                                profile = profile,
                                currentlyVisibleId = state.currentlyVisibleProfileId,
                                updateOffsetX = { value, id ->
                                    swipeScreenModel.updateOffsetX(value = value, id = id)
                                }
                            ) { value ->
                                hideOverlays = value
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    val pagerState = pagerStates[index]

                                    HorizontalPager(
                                        modifier = Modifier.pointerInput(Unit) {
                                            detectTapGestures { offset: Offset ->
                                                val boxSize = size.toSize()
                                                when {
                                                    offset.x < boxSize.width / 2 -> {

                                                        if (pagerState.currentPage > 0) {
                                                            coroutineScope.launch {
                                                                pagerState.animateScrollToPage(
                                                                    pagerState.currentPage - 1
                                                                )
                                                            }
                                                        }
                                                    }
                                                    offset.x > boxSize.width / 2 -> {
                                                        if (pagerState.currentPage < pagerState.pageCount && (addedPhotosCount >= 3 || pagerState.currentPage == 0)) {
                                                            coroutineScope.launch {
                                                                pagerState.animateScrollToPage(
                                                                    pagerState.currentPage + 1
                                                                )
                                                            }
                                                        }
                                                    }

                                                    else -> {}
                                                }
                                            }
                                        },
                                        state = pagerState,
                                        userScrollEnabled = false
                                    ) {
                                        if (!blurProfile){
                                            Box {
                                                AsyncImage(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight()
                                                        .then(
                                                            if (isAccountPaused) Modifier.blur(radius = 15.dp) else if (addedPhotosCount >= 3 || pagerState.currentPage == 0) Modifier else Modifier.blur(
                                                                radius = 15.dp
                                                            )
                                                        ),
                                                    model = profile.user.medias?.getOrNull(it)?.url ?: "",
                                                    contentScale = ContentScale.Crop,
                                                    contentDescription = "",
                                                )

                                                if (addedPhotosCount >= 3 || pagerState.currentPage == 0 && !isAccountPaused) {
                                                    // Description Profile
                                                    Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.BottomCenter
                                                    ) {

                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(220.dp)
                                                                .background(
                                                                    brush = Brush.verticalGradient(
                                                                        listOf(
                                                                            Color(0xff424242).copy(alpha = 0f),
                                                                            Color(0xffF33358).copy(alpha = 0.8f)
                                                                        )
                                                                    )
                                                                )
                                                        )

                                                        if (!isAccountPaused) {
                                                            Column(
                                                                modifier = Modifier
                                                                    .align(Alignment.BottomStart)
                                                                    .padding(bottom = 25.dp)
                                                            ) {
                                                                DescriptionProfile(
                                                                    modifier = Modifier
                                                                        .padding(horizontal = 17.dp),
                                                                    showNewBadge = true,
                                                                    name = profile.user.name ?: "",
                                                                    age = profile.user.age ?: 0
                                                                )

                                                                Spacer(Modifier.height(animatedSpacing))
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }


                                    // Content Top Arrow Back, Dot Menu Horizontal Pager
                                    if (!isAccountPaused && !hideOverlays) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                //    .align(Alignment.TopCenter)
                                                .padding(top = 23.3.dp, start = 17.dp, end = 17.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(horizontal = 8.26.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            ) {
                                                repeat(pagerState.pageCount) {
                                                    Box(
                                                        modifier = Modifier
                                                            .height(1.48.dp)
                                                            .background(
                                                                color = if (it == pagerState.currentPage) Color.White else Color(
                                                                    0xff6D657F
                                                                ), shape = RoundedCornerShape(8.dp)
                                                            )
                                                            .weight(1f)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(20.dp))
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                            ) {
                                                // Icon Back
                                                Image(
                                                    modifier = Modifier.size(
                                                        height = 17.dp,
                                                        width = 24.dp
                                                    ),
                                                    imageVector = vectorResource(Res.drawable.ic_rollback),
                                                    contentDescription = "",
                                                )

                                                // Dot Menu
                                                Column(
                                                    modifier = Modifier.wrapContentSize(),
                                                    //   horizontalAlignment = Alignment.End
                                                ) {
                                                    Box(
                                                        Modifier.size(21.dp).noRippleClickable {
                                                            swipeScreenModel.updateShowMenuItem(!state.showMenuItem)
                                                        }, contentAlignment = Alignment.Center
                                                    ) {
                                                        Image(
                                                            modifier = Modifier
                                                                .size(
                                                                    height = 18.dp,
                                                                    width = 20.dp
                                                                ),
                                                            imageVector = vectorResource(Res.drawable.ic_dot_menu),
                                                            contentDescription = "",
                                                        )
                                                    }

                                                    if (state.showMenuItem) {
                                                        Spacer(Modifier.height(10.dp))
                                                        ReportOrBlockDropDownMenu(
                                                            expanded = state.showMenuItem,
                                                            onBlock = {
                                                                swipeScreenModel.updateShowMenuItem(
                                                                    false
                                                                )
                                                                swipeScreenModel.updateShowBlockDialog(
                                                                    true
                                                                )
                                                            },
                                                            onReport = {
                                                                swipeScreenModel.updateShowMenuItem(
                                                                    false
                                                                )
                                                                swipeScreenModel.updateShowReportBottomSheet(
                                                                    true
                                                                )
                                                            },
                                                            onDismissRequest = {
                                                                swipeScreenModel.updateShowMenuItem(
                                                                    false
                                                                )
                                                            }
                                                        )
                                                    }

                                                }
                                            }
                                        }
                                    }


                                    if (!(pagerState.currentPage == 0 || addedPhotosCount >= 3)) {
                                        AddMorePhotos(addedPhotosCount, onAddPhotos = {
                                            if (addedPhotosCount < 3) {
                                                navigator.push(EditProfileScreen)
                                            }
                                        })
                                    }
                                }
                            }
                        }
                    }
                    if (state.profiles.isNotEmpty() && state.showSwipeGuide && !isAccountPaused){
                        SwipeLeftRightGuide(onClick = {
                            swipeScreenModel.updateShowSwipeGuide(false)
                            swipeScreenModel.onSwipeGuidelinesShown(context)
                        })
                    }

                    if (state.profiles.isEmpty() && !state.isLoading && !isAccountPaused){
                        if (isConnected){
                            NoProfilesLeft(
                                onClick = navigateToFiltersScreen,
                                isDarkMode = isDarkMode
                            )
                        }else{
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                                NoInternet(isDarkMode = isDarkMode,onRetry = {
//                                    if (isConnected){ }
                                })
                            }
                        }
                    }

                }

                if (state.profiles.isNotEmpty()) {
                    state.profiles.getOrNull(currentIndex)?.user?.let { user->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)

                        ) {

                            Spacer(Modifier.height(19.dp))
                            if (user.bioVisible == true && !user.bio.isNullOrBlank()) {

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
                                    text = user.bio,
                                    fontSize = 14.sp,
                                    color = if (isDarkMode) Color.White else  Color(0xff101828),
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
                                color = if (isDarkMode) Color.White else  Color(0xff101828),
                                lineHeight = 21.sp,
                                letterSpacing = 0.2.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(11.dp))

                            user.typeOfRelation?.let {items->
                                LookingForSection(infos = items.mapNotNull {item-> SwipeLeftRightScreen.datingPrefsData.firstOrNull { it.text ==  item} }, isDarkMode = isDarkMode)
                            }


                            Spacer(Modifier.height(11.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (user.genderVisible == true && !user.gender.isNullOrBlank()){
                                    ChipItem(icon = Res.drawable.ic_gender, text = user.gender, isDarkMode = isDarkMode)
                                }
                                if (user.heightVisible == true && (user.height ?: 0) > 0){
                                    ChipItem(icon = Res.drawable.ic_height_scale, text = "${user.height}cm",isDarkMode = isDarkMode)
                                }
                                if (user.educationVisible == true && !user.education.isNullOrBlank()){
                                    ChipItem(icon = Res.drawable.ic_education, text = user.education,isDarkMode = isDarkMode)
                                }
                                if (user.profession?.professionVisible == true && !user.profession.jobTitle.isNullOrBlank() && !user.profession.company.isNullOrBlank()){
                                    ChipItem(icon = Res.drawable.ic_profession, text = "${user.profession.jobTitle} at ${user.profession.company}",isDarkMode = isDarkMode)
                                }
                                if (user.familyVisible == true && !user.family.isNullOrBlank()){
                                    ChipItem(icon = Res.drawable.ic_family_planning, text = user.family,isDarkMode = isDarkMode)
                                }
                                if (user.petsVisible == true && !user.pets?.firstOrNull().isNullOrBlank()){
                                    ChipItem(icon = Res.drawable.ic_pets, text = user.pets?.first()!!,isDarkMode = isDarkMode)
                                }
                                if (user.drinkingVisible == true && !user.drinking.isNullOrBlank()){
                                    ChipItem(icon = Res.drawable.ic_drink, text = user.drinking, isDarkMode = isDarkMode)
                                }
                                if (user.smokingVisible == true && !user.smoking.isNullOrBlank()){
                                    ChipItem(icon = Res.drawable.ic_smoke, text = user.smoking,isDarkMode = isDarkMode)
                                }
                            }

                            Spacer(Modifier.height(10.dp))

                            val interests = user.interests?.mapNotNull { it.value }?.flatten()
                            if (!interests.isNullOrEmpty()) {
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
                                    interests.forEach {item->
                                        if (!item.isNullOrBlank()){
                                            ChipItem(text = item, isDarkMode = isDarkMode)
                                        }
                                    }
                                }

                                Spacer(Modifier.height(10.dp))
                            }

                            if (user.languageVisible == true && user.language.isNotEmpty()){
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
                                    user.language.forEach { item ->
                                        if (!item.isNullOrBlank()){
                                            ChipItem(text = item,isDarkMode = isDarkMode)
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(100.dp))
                        }
                    }
                }

            }

        }

        AnimatedVisibility(
            modifier = Modifier.padding(top = 20.dp).safeContentPadding().padding(horizontal = 16.dp).height(36.dp),
            visible = state.showToast,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Toast(text = stringResource(Res.string.report_has_been_sent))

            LaunchedEffect(Unit) {
                delay(2000) // Hide after 2 seconds
                swipeScreenModel.updateShowToast(false)
            }

        }

        Box(Modifier.requiredSize(height = h, width = w), contentAlignment = Alignment.TopCenter){
            Column(modifier = Modifier) {
                Spacer(Modifier.height(10.dp))
                TopBarWithFilter(onFilterClick = {navigator.push(FilterScreen())}, isDarkMode = isDarkMode)
              //  ConnectivityToast()
            }

        }

    }

    if (!showAnimation && !state.isLoading && !isAccountPaused && state.profiles.isNotEmpty() && !hideOverlays && !blurProfile) {


        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SwipeActionButtons(
                onTemporarySkip = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                        swipeScreenModel.updateTempSkipState(true)
                    }
                },
                onDislikeProfile = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                        swipeScreenModel.updateDislikeProfileState(true)
                    }
                },
                onLikeProfile = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                        swipeScreenModel.updateLikeProfileState(true)
                    }
                }
            )
        }
    }

    if (!state.isLoading && state.showTopBar && state.profiles.isNotEmpty())  {
        AnimatedVisibility(
            visible = state.showTopBar,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {

            Row(
                modifier = Modifier.background(if (isDarkMode) BASE_DARK else Color.White).fillMaxWidth().height(58.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier.fillMaxWidth().height(21.75.dp).padding(start = 21.dp)) {

                    Box(
                        modifier = Modifier.fillMaxHeight()
                            .noRippleClickable{
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(0)
                                }
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            modifier = Modifier.width(14.67.dp).height(13.29.dp),
                            imageVector = vectorResource(Res.drawable.ic_left),
                            contentDescription = "back",
                            tint = if (!isDarkMode) Color(0xff101828) else Color.White
                        )
                    }



                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.profiles.getOrNull(currentIndex)?.user?.name ?: "",
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.2.sp,
                            color = if (!isDarkMode) Color(0xff101828) else Color.White
                        )
                    }


                }


            }
        }
    }

    if (state.showBlockDialog){
        BlockConfirmationDialog(
            name = state.profiles.getOrNull(currentIndex)?.user?.name ?: "",
            onBlock = {
                swipeScreenModel.updateShowBlockDialog(false)
            },
            onCancel = {
                swipeScreenModel.updateShowBlockDialog(false)
            },
            isDarkMode = isDarkMode
        )
    }


    if (state.showReportBottomSheet){
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                swipeScreenModel.updateShowReportBottomSheet(false)
                //screenModel.hideSheet()
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            ReportProfileBottomSheet(isDarkMode = isDarkMode, onReportButtonClick = {
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    swipeScreenModel.updateShowReportBottomSheet(false)
                    swipeScreenModel.updateShowToast(true)
                }

            })
        }
    }

    if (showUnlockDialog){
        SendUnlockRequestDialog(
            type = MediaItem.Type.IMAGE,
            onCancel = {
                showUnlockDialog = false
            },
            onSend = {
                showUnlockDialog = false
            }
        )
    }


}

@Composable
fun Dp.toPx():Float{
    return with(LocalDensity.current){
        this@toPx.toPx()
    }
}