package com.lovorise.app.chat.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_heart_shape_white
import coinui.composeapp.generated.resources.liked_you
import coinui.composeapp.generated.resources.see_who_liked_you
import coinui.composeapp.generated.resources.when_you_like_someone_you_find_here
import coinui.composeapp.generated.resources.your_interests
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.domain.model.User
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.profile_visitors.VisitorsLikesAndMatchesGrid
import com.lovorise.app.profile_visitors.components.CustomTabRow
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class LikesScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()

        LikesScreenContent(
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
fun LikesScreenContent(
    isDarkMode:Boolean,
    onBack:()->Unit,
    profileScreenModel: ProfileScreenModel,
    navigateToSubscriptionScreen:()->Unit
) {

    BackHandler(true) {
        onBack()
    }

    val tabs = listOf("Likes", "I like")

    val profileScreenState by profileScreenModel.state.collectAsState()
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

    //var isShowLiked by remember { mutableStateOf(true) }

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
            HeaderWithTitleAndBack(title = stringResource(Res.string.liked_you), onBack = onBack, addShadow = false, isDarkMode = isDarkMode)


            CustomTabRow(
                titles = tabs,
                tabIndex = pagerState.currentPage,
                count1 =  40,
                count2 = 0,
                paddingValues = PaddingValues(top = 12.dp, start = 16.dp, end = 16.dp),
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                isDarkMode = isDarkMode
            )

            //  Spacer(modifier = Modifier.padding(top = 12.dp))

            HorizontalPager(pagerState){page->
                // Tab Screen by Index
                when (page) {
                    0 -> VisitorsLikesAndMatchesGrid(
                        isBlur = profileScreenState.subscriptionType == SubscriptionType.FREE,
                        onClickShowLiked = navigateToSubscriptionScreen,
                        btnText = stringResource(Res.string.see_who_liked_you),
                        users = users
                    )

                    1 -> EmptyLikesState(titleText = stringResource(Res.string.your_interests), bodyText = stringResource(Res.string.when_you_like_someone_you_find_here), isDarkMode = isDarkMode)
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
fun EmptyLikesState(titleText:String,bodyText:String,isDarkMode: Boolean) {
   Box(Modifier.fillMaxWidth().fillMaxHeight(0.85f), contentAlignment = Alignment.Center) {

       Column(Modifier.fillMaxHeight().width(235.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

           Box(Modifier.size(57.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0), CircleShape), contentAlignment = Alignment.Center) {
               Image(
                   imageVector = vectorResource(Res.drawable.ic_heart_shape_white),
                   contentDescription = null,
                   modifier = Modifier.size(24.dp)
               )
           }


           Spacer(Modifier.height(16.dp))

           Text(
               modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
               text = titleText,
               fontFamily = PoppinsFontFamily(),
               fontWeight = FontWeight.SemiBold,
               fontSize = 16.sp,
               letterSpacing = 0.2.sp,
               lineHeight = 24.sp,
               textAlign = TextAlign.Center,
               color = if (isDarkMode) Color.White else Color(0xff101828)
           )

           Spacer(Modifier.height(16.dp))

           Text(
               modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
               text = bodyText,
               fontFamily = PoppinsFontFamily(),
               fontWeight = FontWeight.Normal,
               fontSize = 12.sp,
               letterSpacing = 0.2.sp,
               lineHeight = 16.sp,
               textAlign = TextAlign.Center,
               color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
           )
       }
   }
}