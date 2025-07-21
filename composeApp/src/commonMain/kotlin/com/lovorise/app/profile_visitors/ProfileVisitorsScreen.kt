package com.lovorise.app.profile_visitors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_navigate_to_top
import coinui.composeapp.generated.resources.see_who_visited_your_profile
import coinui.composeapp.generated.resources.tab_options
import coinui.composeapp.generated.resources.visitors
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.domain.model.User
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.profile_visitors.components.CustomTabRow
import com.lovorise.app.profile_visitors.components.MatchAndLikeCard
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ProfileVisitorsScreen : Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()

        ProfileVisitorsScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                navigator.pop()
            },
            profileScreenModel = profileScreenModel,
            navigateToSubscriptionScreen = {
                navigator.push(PurchaseSubscriptionScreen(SubscriptionType.WEEKLY))
            }

        )
    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun ProfileVisitorsScreenContent(
    isDarkMode:Boolean,
    onBack:()->Unit,
    profileScreenModel: ProfileScreenModel,
    navigateToSubscriptionScreen:()->Unit
) {

    BackHandler(true) {
        onBack()
    }

    val tabs = stringArrayResource(Res.array.tab_options)
    val profileScreensState by profileScreenModel.state.collectAsState()

    //var isShowLiked by remember { mutableStateOf(true) }

    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    val users = List(50){
        User(imageUrl = "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg?auto=compress&cs=tinysrgb&w=800", isVerified = false, name = "Nancy", age = 20+(it%2),id = it + 137)
    }


    Column(
        modifier = Modifier
    ) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .weight(1f),
        ) {
            HeaderWithTitleAndBack(title = stringResource(Res.string.visitors), onBack = onBack, addShadow = false, isDarkMode = isDarkMode)


            CustomTabRow(
                titles = tabs,
                tabIndex = pagerState.currentPage,
                count1 = 40,
                count2 = 40,
                paddingValues = PaddingValues(top = 12.dp, start = 16.dp, end = 16.dp),
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                isDarkMode = isDarkMode
            )

          //  Spacer(modifier = Modifier.padding(top = 12.dp))

            HorizontalPager(pagerState) { page ->
                // Tab Screen by Index
                when (page) {
                    0 -> VisitorsLikesAndMatchesGrid(
                        isBlur = profileScreensState.subscriptionType == SubscriptionType.FREE,
                        onClickShowLiked = navigateToSubscriptionScreen,
                        users = users,
                        btnText = stringResource(Res.string.see_who_visited_your_profile)
                    )

                    1 -> VisitorsLikesAndMatchesGrid(
                        users = users,
                        btnText = stringResource(Res.string.see_who_visited_your_profile)
                    )
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
fun VisitorsLikesAndMatchesGrid(modifier: Modifier = Modifier, isBlur: Boolean=false, onClickShowLiked : ()->Unit={}, users:List<User>, btnText:String) {

    val lazyGridState = rememberLazyGridState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true){
        lazyGridState.scrollToItem(0)
    }


    // Observe the first scroll
    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                if (index > 0) { // Detect any scrolling, even slight movement
                    if (isBlur) {
                      //  lazyGridState.animateScrollToItem(0)
                        onClickShowLiked()
                        delay(100)
                        lazyGridState.animateScrollToItem(0)
                    }
                }
            }
    }

    Box {
        LazyVerticalGrid(
            state = lazyGridState,
            modifier = modifier.padding(horizontal =  22.dp).padding(top = 10.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                MatchAndLikeCard(isBlur = isBlur, user = user)
            }

            item {
                // This box will be the padding element
                Spacer(modifier = Modifier.height(10.dp)) // Adjust the padding height as needed
            }

        }

        if (isBlur) {
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, bottom = 28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffF33358)),
                contentPadding = PaddingValues(vertical = 8.dp),
                onClick = { onClickShowLiked() }
            ) {
                Text(
                    text = btnText,
                    fontSize = 16.sp,
                    color = Color.White,
                    lineHeight = 18.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                )
            }
        }
        AnimatedVisibility(
            visible = !isBlur && lazyGridState.firstVisibleItemIndex > 1,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(), // Enter from bottom
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut() // Exit to top
        ){
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
                NavigateToTop { coroutineScope.launch {
                    lazyGridState.animateScrollToItem(0)
                } }
            }
        }
    }
}

@Composable
fun NavigateToTop(onClick:()->Unit) {

    Box(Modifier.fillMaxWidth().height(145.dp).background(
        brush = Brush.verticalGradient(
            listOf(
                Color(0xff999999).copy(alpha = 0f),
                Color(0xff999999),
            )
        )
    ), contentAlignment = Alignment.Center){
        Image(
            imageVector = vectorResource(Res.drawable.ic_navigate_to_top),
            contentDescription = null,
            modifier = Modifier.height(40.56.dp).width(40.dp).noRippleClickable(onClick)
        )
    }

}