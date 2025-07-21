package com.lovorise.app.profile.presentation.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import coinui.composeapp.generated.resources.control_your_privacy
import coinui.composeapp.generated.resources.hide_all
import coinui.composeapp.generated.resources.hide_this_one
import coinui.composeapp.generated.resources.ic_chevron_right_light_color
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.only_premium_user_with_photo_verification_can_hide
import coinui.composeapp.generated.resources.review_requests
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.profile_upload.VideoPlayerWithButtons
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.edit_profile.components.UnlockMediaRequestBottomSheetContent
import com.lovorise.app.settings.presentation.components.CustomSwitch
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ProfileImageVideoManagementScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()


        ProfileImageVideoManagementScreenContent(
            isDarkMode = isDarkMode,
            onBack = {
                navigator.pop()
            },
            accountsState = accountsState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileImageVideoManagementScreenContent(
    isDarkMode:Boolean,
    onBack:()->Unit,
    accountsState: AccountsApiCallState,
) {

    val scrollState = rememberScrollState()


    var isLoading by remember { mutableStateOf(false) }
    var isPlaying by rememberSaveable { mutableStateOf(false) }


    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(0){accountsState.user?.medias?.filter { it.url.isNotBlank() }?.size ?: 0}

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var hideAll by rememberSaveable { mutableStateOf(false) }
    var hideThisOne by rememberSaveable { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage){
        isPlaying = false
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
                   // Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(end = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .noRippleClickable(onBack)
                                .size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                tint = if (isDarkMode) Color.White else Color(0xff000000),
                                modifier = Modifier,
                                imageVector = vectorResource(Res.drawable.ic_xmark),
                                contentDescription = "",
                            )
                        }

                    }

                    Box(Modifier.fillMaxWidth().requiredHeight(height.times(0.6f))){
                        HorizontalPager(
                            modifier = Modifier
                                .fillMaxSize().pointerInput(Unit) {
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
                                        Column(Modifier.clipToBounds()) {
                                            VideoPlayerWithButtons(
                                                uri = media.url,
                                                togglePlayingState = { isPlaying = !isPlaying },
                                                isPlaying = isPlaying
                                            )
                                        }
                                    }
                                    if (media.type == SignedUrlMediaItem.Type.IMAGE) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                                .background(if (isLoading) Color(0xffEAECF0) else Color.Black),
                                            model = media.url,
                                            contentScale = ContentScale.FillWidth,
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
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.26.dp, vertical = 12.dp),
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

                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 19.dp),
                        text = stringResource(Res.string.only_premium_user_with_photo_verification_can_hide),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                        fontFamily = PoppinsFontFamily()
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomDivider(isDarkMode = isDarkMode)

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(24.dp)
                            .noRippleClickable{ showBottomSheet = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = stringResource(Res.string.review_requests),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) Color.White else Color(0xff101828),
                            fontFamily = PoppinsFontFamily()
                        )

                        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center){
                            Icon(
                                tint = if (isDarkMode) Color.White else Color.Black,
                                modifier = Modifier.size(24.dp),
                                imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                                contentDescription = null
                            )
                        }

                    }

                    Spacer(Modifier.height(16.dp))

                    CustomDivider(isDarkMode = isDarkMode)

                    Spacer(Modifier.height(24.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(Res.string.control_your_privacy),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828),
                        fontFamily = PoppinsFontFamily()
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .requiredHeight(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(Res.string.hide_this_one),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = if (isDarkMode) Color.White else Color.Black
                        )

                        Box(Modifier.requiredHeight(20.dp).requiredWidth(36.dp)) {
                            CustomSwitch(
                                modifier = Modifier.fillMaxSize(),
                                isChecked = hideThisOne,
                                onCheckChanged = {
                                    hideThisOne = !hideThisOne
                                },
                                isDarkMode = isDarkMode
                            )
                        }


                    }
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .requiredHeight(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = stringResource(Res.string.hide_all),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = if (isDarkMode) Color.White else Color.Black
                        )

                        Box(Modifier.requiredHeight(20.dp).requiredWidth(36.dp)) {
                            CustomSwitch(
                                modifier = Modifier.fillMaxSize(),
                                isChecked = hideAll,
                                onCheckChanged = {
                                    hideAll = !hideAll
                                },
                                isDarkMode = isDarkMode
                            )
                        }


                    }

                    Spacer(Modifier.height(55.dp))

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

    if (showBottomSheet){
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            UnlockMediaRequestBottomSheetContent(
                isDarkMode = isDarkMode,
                onCancel = {
                    sheetState.hideWithCompletion(scope) {
                        showBottomSheet = false
                    }
                },
                data = emptyList()
            )

        }
    }



}