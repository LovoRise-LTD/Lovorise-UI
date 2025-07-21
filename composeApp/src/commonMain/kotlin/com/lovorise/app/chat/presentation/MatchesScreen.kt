package com.lovorise.app.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.matches
import coinui.composeapp.generated.resources.see_your_matches
import coinui.composeapp.generated.resources.tab_options
import com.lovorise.app.chat.domain.model.User
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.ProfileScreensState
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.profile_visitors.VisitorsLikesAndMatchesGrid
import com.lovorise.app.profile_visitors.components.CustomTabRow
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

class MatchesScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profileScreensState by profileScreenModel.state.collectAsState()

        MatchesScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                navigator.pop()
            },
            profileScreensState = profileScreensState,
            navigateToPurchase = {
                navigator.push(PurchaseSubscriptionScreen(SubscriptionType.WEEKLY))
            }
        )
    }
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun MatchesScreenContent(
    isDarkMode: Boolean,
    onBack: () -> Unit,
    profileScreensState: ProfileScreensState,
    navigateToPurchase:()->Unit
) {

    BackHandler(true) {
        onBack()
    }

   // var tabIndex by remember { mutableStateOf(0) }
    val tabs = stringArrayResource(Res.array.tab_options)

    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    val users = List(50) {
        User(
            imageUrl = if (it % 2 == 0) "https://img.freepik.com/free-photo/portrait-outdoors-business-man-smiles_23-2148763856.jpg?ga=GA1.1.710363456.1728270703&semt=ais_hybrid" else if(it % 3 == 0) "https://img.freepik.com/free-photo/isolated-shot-joyful-brenette-young-cute-woman-laughs-joyfully-as-hears-funny-anecdote-from-friend_496169-2540.jpg?size=626&ext=jpg&ga=GA1.1.710363456.1728270703&semt=ais_hybrid"  else "https://img.freepik.com/free-photo/executive-blue-suit-arms-crossed_1139-200.jpg?size=626&ext=jpg&ga=GA1.1.710363456.1728270703&semt=ais_hybrid",
            name = "Test ${it + 1} ",
            isVerified = it % 3 == 0,
            id = it + 137
        )
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
            HeaderWithTitleAndBack(title = stringResource(Res.string.matches), onBack = onBack, addShadow = false, isDarkMode = isDarkMode)


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

            HorizontalPager(pagerState){page ->
                when (page) {
                    0 -> VisitorsLikesAndMatchesGrid(
                        isBlur = profileScreensState.subscriptionType == SubscriptionType.FREE,
                        onClickShowLiked = navigateToPurchase,
                        btnText = stringResource(Res.string.see_your_matches),
                        users = users
                    )

                    1 -> VisitorsLikesAndMatchesGrid(btnText = stringResource(Res.string.see_your_matches), users = users)
                }
            }

            //  Spacer(modifier = Modifier.padding(top = 12.dp))

            // Tab Screen by Index


        }


        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )

    }

}

