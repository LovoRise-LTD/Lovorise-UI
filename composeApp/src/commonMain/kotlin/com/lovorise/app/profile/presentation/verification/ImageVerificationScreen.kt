package com.lovorise.app.profile.presentation.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.camera
import coinui.composeapp.generated.resources.camera_permissions_required
import coinui.composeapp.generated.resources.for_more_info_how_we_use_data
import coinui.composeapp.generated.resources.ic_record_verification_selfie
import coinui.composeapp.generated.resources.ic_warning_red
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.permissions_in_your_settings
import coinui.composeapp.generated.resources.photo_verification_requirements
import coinui.composeapp.generated.resources.please_grant
import coinui.composeapp.generated.resources.privacy_policy
import coinui.composeapp.generated.resources.retake
import coinui.composeapp.generated.resources.submit
import coinui.composeapp.generated.resources.submit_your_verification_video
import coinui.composeapp.generated.resources.take_a_video_selfie
import coinui.composeapp.generated.resources.take_my_photo
import coinui.composeapp.generated.resources.to_verify_successfully
import coinui.composeapp.generated.resources.we_wont_use_this_photo_it_is_just_to_verify
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.AlertMessageDialog
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.libs.permissions_compose.BindEffect
import com.lovorise.app.libs.permissions_compose.rememberPermissionsControllerFactory
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import io.ktor.util.reflect.instanceOf
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ImageVerificationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = navigator.koinNavigatorScreenModel<IDProfileVerificationScreenModel>()
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        LaunchedEffect(true){
            screenModel.onInit(controller)
        }

        val state by screenModel.state.collectAsState()

        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()



        ImageVerificationScreenContent(
            isDarkMode = isDarkMode,
            state = state,
            screenModel = screenModel,
            onBack = {navigator.pop()},
            navigateToCaptureImageScreen = {
                navigator.push(CaptureImageScreen())
            },
            navigateToProfileScreen = {
                val canPop = navigator.items.any { it.instanceOf(HomeScreen::class) }
                profileScreenModel.updateVerificationUnderReviewDialogState(true)
                tabsScreenModel.updateTab(TabsScreenModel.BottomTab.PROFILE)
                if (canPop){
                    println("Pop to older home screen")
                    navigator.popUntil { it.instanceOf(HomeScreen::class) }
                }else{
                    println("Creating new home screen")
                    navigator.push(HomeScreen())
                }
            }
        )
    }
}

@Composable
fun ImageVerificationScreenContent(isDarkMode:Boolean,onBack:()->Unit,state: IDProfileVerificationScreenState,screenModel: IDProfileVerificationScreenModel,navigateToCaptureImageScreen: ()->Unit,navigateToProfileScreen:()->Unit) {

    val context = LocalPlatformContext.current
    val allItems = stringArrayResource(Res.array.photo_verification_requirements)
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }



    Column(Modifier.fillMaxSize()) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(Modifier.fillMaxSize().background(if (isDarkMode) BASE_DARK else Color.White).weight(1f).padding(horizontal = 16.dp).verticalScroll(
            rememberScrollState()
        )) {
            Spacer(Modifier.height(13.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    Modifier.size(24.dp).noRippleClickable(onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        tint = if (isDarkMode) Color.White else Color(0xff475467),
                        imageVector = vectorResource(Res.drawable.ic_xmark),
                        contentDescription = null
                    )
                }

            }

            Spacer(Modifier.height(70.dp))

            Image(
                imageVector = vectorResource(Res.drawable.ic_record_verification_selfie),
                contentDescription = null,
                modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(if(state.capturedProfilePath.isBlank()) Res.string.take_a_video_selfie else Res.string.submit_your_verification_video),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = stringResource(Res.string.we_wont_use_this_photo_it_is_just_to_verify),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.to_verify_successfully),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(7.dp))

            allItems.forEach { item ->
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(Modifier.height(21.dp).width(3.dp), contentAlignment = Alignment.Center) {
                        Box(Modifier.size(3.dp).background(if (isDarkMode) DISABLED_LIGHT else Color(0xff344054), CircleShape))
                    }
                    Spacer(Modifier.width(10.dp))
                    androidx.compose.material3.Text(
                        text = item,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 21.sp,
                        textAlign = TextAlign.Start,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                    )

                }
            }



            Spacer(Modifier.weight(1f))


            val text = buildAnnotatedString {
                append(stringResource(Res.string.for_more_info_how_we_use_data))
                pushStringAnnotation(tag = "privacy_policy", annotation = "privacy_policy")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFF33358),
                    )
                ) {
                    append(stringResource(Res.string.privacy_policy))
                }
                pop()
                append(".")
            }

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        textLayoutResult?.let { textLayoutResult ->
                            // Get the character position from the tap offset
                            val position = textLayoutResult.getOffsetForPosition(tapOffset)

                            // Check for annotations at the clicked position
                            val privacyAnnotation = text.getStringAnnotations("privacy_policy", position, position)

                            // Handle based on annotation
                            if (privacyAnnotation.isNotEmpty()) {
                                openUrlInCustomTab(AppConstants.PRIVACY_POLICY_URL,context)
                            }
                        }
                    }
                },
                text = text,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 18.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff667085),
                onTextLayout = { textLayoutResult = it },
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))
            ButtonWithText(
                text = stringResource(if (state.capturedProfilePath.isNotBlank()) {if (state.profileMatched) Res.string.submit else Res.string.retake} else Res.string.take_my_photo),
                bgColor = Color( 0xffF33358),
                textColor = Color(0xffffffff),
                onClick = {
                    if (state.capturedProfilePath.isNotBlank()) {
                        navigateToProfileScreen()
                    }else{
                        screenModel.checkCameraAndAudioPermissionStatusAndNavigate(
                            navigateToCaptureImageScreen
                        )
                    }
                }
            )

            if (state.capturedProfilePath.isNotBlank() && state.profileMatched) {
                Spacer(Modifier.height(10.dp))
                ButtonWithText(
                    text = stringResource(Res.string.retake),
                    bgColor = if (isDarkMode) CARD_BG_DARK else DISABLED_LIGHT,
                    textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    onClick = {
                        screenModel.checkCameraAndAudioPermissionStatusAndNavigate(
                            navigateToCaptureImageScreen
                        )
                    }
                )
            }

            Spacer(Modifier.height(35.dp))




        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }

    if (state.showPermissionRationale) {
        AlertMessageDialog(
            title =  stringResource(Res.string.camera_permissions_required),
            message = "${stringResource(Res.string.please_grant)} ${stringResource(Res.string.camera)} ${stringResource(Res.string.permissions_in_your_settings)}",
            onPositiveClick = {
                screenModel.openSettings()
                screenModel.updateRationaleState(false)
            },
            onNegativeClick = {
                screenModel.updateRationaleState(false)
            },
            isDarkMode = isDarkMode
        )

    }
}