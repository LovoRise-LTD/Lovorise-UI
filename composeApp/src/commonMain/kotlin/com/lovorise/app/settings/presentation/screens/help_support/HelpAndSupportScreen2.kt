package com.lovorise.app.settings.presentation.screens.help_support

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.enter_problem_description
import coinui.composeapp.generated.resources.ic_plus
import coinui.composeapp.generated.resources.problem_description
import coinui.composeapp.generated.resources.report_a_problem
import coinui.composeapp.generated.resources.screenshots_with_size_less_than
import coinui.composeapp.generated.resources.submit
import coinui.composeapp.generated.resources.support_categories
import coinui.composeapp.generated.resources.thanks_for_letting_us_know
import coinui.composeapp.generated.resources.upload_ss_optional
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.HelpSupportRequestData
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.dashedBorder
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.components.Toast
import com.lovorise.app.noRippleClickable
import com.lovorise.app.onboarding_info.DescriptionTextField
import com.lovorise.app.photo_capture_pick.rememberGalleryManager
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

class HelpAndSupportScreen2 : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()

        LaunchedEffect(true){
            accountsViewModel.updateLoadingState(false)
        }

        HelpAndSupportScreen2Content(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            accountsViewModel = accountsViewModel
        )
    }
}

@Composable
fun HelpAndSupportScreen2Content(isDarkMode:Boolean,goBack:()->Unit,accountsViewModel: AccountsViewModel) {

    val supportCategories = stringArrayResource(Res.array.support_categories)
    val thanksMsg = stringResource(Res.string.thanks_for_letting_us_know)
    var selectedIndex by remember { mutableIntStateOf(0) }
    val categories by remember { mutableStateOf(supportCategories) }
    var description by remember { mutableStateOf("") }

    val accountsState by accountsViewModel.state.collectAsState()

    var toastMessage by remember { mutableStateOf("") }

    var isEnabled by remember { mutableStateOf(false) }

    var images by rememberSaveable { mutableStateOf(listOf<String?>(null,null,null)) }

    var clickedImageIndex by remember { mutableIntStateOf(0) }
    val scope  = rememberCoroutineScope()

    //var isLoading by remember { mutableStateOf(false) }
    val context = LocalPlatformContext.current

    val galleryLauncher = rememberGalleryManager { result ->
        scope.launch {
            accountsViewModel.updateLoadingState(true)
            val img = result?.saveToCache(context)
            images = images.toMutableList().apply {
                removeAt(clickedImageIndex)
                add(clickedImageIndex, img)
            }
            accountsViewModel.updateLoadingState(false)
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(description){
        isEnabled = description.length >= 2 && selectedIndex >= 0
    }

    Column(modifier = Modifier.noRippleClickable {
        keyboardController?.hide()
        focusManager.clearFocus()
    })
    {

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

            HeaderWithTitleAndBack(title = stringResource(Res.string.report_a_problem), onBack = goBack, isDarkMode = isDarkMode)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(16.dp))


                categories.forEachIndexed { index, item ->
                    FeedBackCategoryItem(item,index == selectedIndex, onClick = { selectedIndex = index }, isDarkMode = isDarkMode)
                    Spacer(Modifier.height(16.dp))
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.problem_description),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Spacer(Modifier.height(6.dp))


                DescriptionTextField(
                    value = description,
                    onValueChange = {
                        if (it.length <= 500) {
                            description = it
                        }
                    },
                    label = stringResource(Res.string.enter_problem_description),
                    height = 168.dp,
                    textStyle = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828)
                    ),
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    bgColor = if (isDarkMode) BASE_DARK else Color.White,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${description.length}/500",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(Res.string.upload_ss_optional),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Spacer(Modifier.height(6.dp))
                
                UploadImageSection(
                    images = images,
                    onItemClick = {
                        val index = images.filterNotNull().size
                        if (images[it] == null) {
                            clickedImageIndex = if (it > index) index else it
                            galleryLauncher.launch()
                        }

                    }

                )
                


                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(Res.string.screenshots_with_size_less_than),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )

                Spacer(Modifier.height(50.dp))


                ButtonWithText(
                    text = stringResource(Res.string.submit),
                    bgColor = if (isEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
                            accountsViewModel.sendSupportRequest(context, HelpSupportRequestData(type = listOf(categories[selectedIndex]), description = description, images = images.filterNotNull())){
                                toastMessage = thanksMsg
                                scope.launch {
                                    delay(100L)
                                    goBack()
                                }
                            }
                        }
                    }
                )
                Spacer(Modifier.height(10.dp))
            }

        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )
    }

    if (accountsState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularLoader()
            //ReelsLoader()
        }
    }


    AnimatedVisibility(
        modifier = Modifier.padding(top = 80.dp).padding(horizontal = 60.dp).height(36.dp),
        visible = toastMessage.isNotBlank(),
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Toast(text = toastMessage)

        LaunchedEffect(Unit) {
            delay(2000) // Hide after 2 seconds
            toastMessage = ""
        }

    }


}


@Composable
fun UploadImageSection(images:List<String?>,onItemClick:(Int)->Unit) {


    BoxWithConstraints {
        val itemWidth = (maxWidth - 16.dp*3)/3
        val itemHeight =  (itemWidth * 4) / 3
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            images.forEachIndexed { index,image->
                ImageHolder(modifier = Modifier.height(itemHeight).width(itemWidth),photo = image, onClick = {onItemClick(index)})
            }

        }
    }
}


@Composable
fun FeedBackCategoryItem(title:String,isChecked:Boolean,onClick:()->Unit,isDarkMode: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){

        Box(modifier = Modifier.size(24.dp).noRippleClickable(onClick), contentAlignment = Alignment.Center) {
            Checkbox(
                checked = isChecked,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xffF33358),
                    checkmarkColor = if (isDarkMode) BASE_DARK else Color.White,
                    uncheckedColor = Color(0xff98A2B3)
                ),
                onCheckedChange = null,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 24.sp,
            //  textAlign = TextAlign.Center,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )

    }
}

@Composable
fun ImageHolder(modifier: Modifier = Modifier,photo:String?,onClick: () -> Unit) {
    if (photo != null) {
        Box(
            modifier = modifier
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(percent = 16))
                    .border(
                        0.5.dp, Color.LightGray.copy(alpha = 0.3f),
                        RoundedCornerShape(percent = 16)
                    )
                    .noRippleClickable(onClick),
                model = photo,
                contentScale = ContentScale.FillBounds,
                contentDescription = ""
            )
//            AsyncImage(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .clip(RoundedCornerShape(percent = 16))
//                    .noRippleClickable(if (canRemove) onRemove else onEdit),
//                model = photo.image,
//                contentScale = ContentScale.FillBounds,
//                contentDescription = ""
//            )



        }
    } else{
        Box(
            modifier = modifier
                .height(130.dp)
                .dashedBorder(width = 1.dp, radius = 16.dp, color = Color(0xffA6ABB4)),
        ) {
            Box(modifier = Modifier
                .size(24.dp)
                .background(color = Color(0xffF33358), shape = CircleShape)
                .align(Alignment.Center)
                .noRippleClickable {
                    onClick()
                },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(Res.drawable.ic_plus),
                    contentDescription = ""
                )
            }
        }
    }
}