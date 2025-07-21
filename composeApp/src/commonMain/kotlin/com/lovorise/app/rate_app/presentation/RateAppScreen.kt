package com.lovorise.app.rate_app.presentation

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_rating_1_active
import coinui.composeapp.generated.resources.ic_rating_1_inactive
import coinui.composeapp.generated.resources.ic_rating_2_active
import coinui.composeapp.generated.resources.ic_rating_2_inactive
import coinui.composeapp.generated.resources.ic_rating_3_active
import coinui.composeapp.generated.resources.ic_rating_3_inactive
import coinui.composeapp.generated.resources.ic_rating_4_active
import coinui.composeapp.generated.resources.ic_rating_4_inactive
import coinui.composeapp.generated.resources.ic_rating_5_active
import coinui.composeapp.generated.resources.ic_rating_5_inactive
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.components.Toast
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.noRippleClickable
import com.lovorise.app.onboarding_info.DescriptionTextField
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class RateAppScreen(private val prefs: SharedPrefs?) : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())


        RateAppScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            onRate = {
                prefs?.setBoolean(PreferencesKeys.IS_RATING_DIALOG_PRESENTED,true)
                navigator.pop()
            }
        )
    }
}

@Composable
fun RateAppScreenContent(isDarkMode:Boolean,goBack:()->Unit,onRate:()->Unit) {

    var description by remember { mutableStateOf("") }

    val feedbackThanksMsg = stringResource(Res.string.thanks_for_your_feedback)

    val items = listOf(
        RateItem(stringResource(Res.string.great),Res.drawable.ic_rating_1_active,Res.drawable.ic_rating_1_inactive),
        RateItem(stringResource(Res.string.nice),Res.drawable.ic_rating_2_active,Res.drawable.ic_rating_2_inactive),
        RateItem(stringResource(Res.string.satisfied),Res.drawable.ic_rating_3_active,Res.drawable.ic_rating_3_inactive),
        RateItem(stringResource(Res.string.average_txt),Res.drawable.ic_rating_4_active,Res.drawable.ic_rating_4_inactive),
        RateItem(stringResource(Res.string.bad),Res.drawable.ic_rating_5_active,Res.drawable.ic_rating_5_inactive),
    )

    var toastMessage by remember { mutableStateOf("") }

    val isEnabled by remember { mutableStateOf(true) }

    var selectedRateIndex by remember { mutableIntStateOf(0) }


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

//    var spacingValue by remember { mutableStateOf(0.dp) }

    var statusBarPadding by remember { mutableStateOf(0.dp) }

    var navigationBarPadding by remember { mutableStateOf(0.dp) }


    val density = LocalDensity.current


    BoxWithConstraints{
        val screenHeight = maxHeight
        Column(modifier = Modifier
            .fillMaxSize()
            .noRippleClickable {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
            .imePadding()
        ) {
            Spacer(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .fillMaxWidth()
            )

            if (statusBarPadding == 0.dp){
                statusBarPadding = with(density){ WindowInsets.statusBars.getTop(density).toDp() }
            }



            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    //.weight(1f)
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .height(screenHeight - statusBarPadding - navigationBarPadding)
                    .fillMaxWidth()

                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                HeaderWithTitleAndBack(title = stringResource(Res.string.rate_app), onBack = goBack, isDarkMode = isDarkMode)

                Column(
                    modifier = Modifier

                        //   .imePadding()
                        // .verticalScroll(rememberScrollState())
                        .fillMaxSize()
//                      .weight(1f)
                       // .verticalScroll(rememberScrollState())

                        .padding(horizontal = 16.dp)
                ) {

                    Spacer(Modifier.height(16.dp))


                    Text(
                        text = stringResource(Res.string.let_us_know),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkMode) Color.White else Color(0xff101828),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(Res.string.we_would_love_to_hear_about_your_experience),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(Res.string.rate_your_experience),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkMode) Color.White else Color(0xff101828),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                    )

                    Spacer(Modifier.height(16.dp))

                    RateSection(
                        selectedIndex = selectedRateIndex,
                        rateItems = items,
                        onSelectionChange = {
                            selectedRateIndex = it
                        },
                        isDarkMode = isDarkMode
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(Res.string.write_a_comment_optional),
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
                        label = stringResource(Res.string.describe_here_your_personal_experience),
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${description.length}/500",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.2.sp
                    )

//                    if (spacingValue == 0.dp) {
//                        BoxWithConstraints(Modifier.weight(1f)) {
//                            spacingValue = maxHeight - 64.dp
//                            Spacer(Modifier.height(spacingValue))
//                            println("the spacing value is $spacingValue")
//                        }
//                    } else {
//                        Spacer(Modifier.height(spacingValue))
//                    }

                    Spacer(Modifier.height(40.dp))

                    Box(Modifier.height(40.dp)) {
                        ButtonWithText(
                            text = stringResource(Res.string.send_your_feedback),
                            bgColor = Color(if (isEnabled) 0xffF33358 else 0xffEAECF0),
                            textColor = Color(if (isEnabled) 0xffffffff else 0xff98A2B3),
                            onClick = {
                                if (isEnabled) {
                                    toastMessage = feedbackThanksMsg
                                    onRate()
                                }
                            }
                        )
                    }
                    Spacer(Modifier.height(24.dp))

                }



            }


            if (navigationBarPadding == 0.dp){
                navigationBarPadding = with(density){ WindowInsets.navigationBars.getBottom(density).toDp()}
            }



            Spacer(
                modifier = Modifier
                    //  .windowInsetsBottomHeight(WindowInsets.ime)
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(if (isDarkMode) BASE_DARK else Color.White)
            )
            //    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.ime))

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
fun RateSection(selectedIndex:Int,onSelectionChange:(Int)->Unit,rateItems:List<RateItem>,isDarkMode: Boolean) {

    BoxWithConstraints {
        val spacing = 8.dp
        val itemSize = (maxWidth - (spacing*4))/5

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            rateItems.forEachIndexed { index,item ->
                RateItems(
                    item = item.copy(isSelected = selectedIndex == index),
                    iconSize = itemSize,
                    onClick = {
                        onSelectionChange(index)
                    },
                    isDarkMode = isDarkMode
                )
            }



        }

    }

}


@Composable
fun RateItems(item: RateItem,modifier: Modifier = Modifier,onClick:()->Unit,iconSize:Dp,isDarkMode: Boolean) {
    Column(modifier = modifier.noRippleClickable(onClick),horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(iconSize).background(
                brush = Brush.linearGradient(
                    colors = if (item.isSelected) listOf(
                        Color(0xffF3335D),
                        Color(0xffF33386)
                    ) else {
                        if (isDarkMode) listOf(BASE_DARK, BASE_DARK) else listOf(Color.White, Color.White)
                    }
                ), shape = CircleShape
            ).then(if (!item.isSelected) Modifier.border(width = 0.87.dp, color = Color(0xffEAECF0), shape = CircleShape) else Modifier), contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(iconSize / 2),
                imageVector = vectorResource(if (item.isSelected) item.activeIcon else item.inactiveIcon),
                contentDescription = null
            )
        }
        Spacer(Modifier.height(6.dp))

        Text(
            text = item.text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            fontSize = 12.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp
        )

    }
}

data class RateItem(
    val text:String,
    val activeIcon:DrawableResource,
    val inactiveIcon:DrawableResource,
    val isSelected:Boolean = false
)