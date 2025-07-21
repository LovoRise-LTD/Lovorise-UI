package com.lovorise.app.profile.presentation.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.back_of_document
import coinui.composeapp.generated.resources.camera
import coinui.composeapp.generated.resources.camera_permissions_required
import coinui.composeapp.generated.resources.continue_txt
import coinui.composeapp.generated.resources.front_of_document
import coinui.composeapp.generated.resources.ic_check
import coinui.composeapp.generated.resources.ic_cross_small
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_scan_document
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.identity_verification
import coinui.composeapp.generated.resources.identity_verification_requirements
import coinui.composeapp.generated.resources.permissions_in_your_settings
import coinui.composeapp.generated.resources.please_grant
import coinui.composeapp.generated.resources.scan_document
import coinui.composeapp.generated.resources.take_photo_of_id
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.AlertMessageDialog
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.dashedBorder
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.libs.permissions_compose.BindEffect
import com.lovorise.app.libs.permissions_compose.rememberPermissionsControllerFactory
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class IdentityVerificationScreen: Screen {

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


        IdentityVerificationScreenContent(
            isDarkMode = isDarkMode,
            state = state,
            screenModel = screenModel,
            onBack = {navigator.pop();screenModel.resetState()},
            navigateToScanDocumentScreen = {navigator.push(ScanDocumentScreen())},
            navigateToImageVerification = {navigator.push(ImageVerificationScreen())}
        )

    }
}

@Composable
fun IdentityVerificationScreenContent(isDarkMode:Boolean,onBack:()->Unit,state: IDProfileVerificationScreenState,screenModel: IDProfileVerificationScreenModel,navigateToScanDocumentScreen: ()->Unit,navigateToImageVerification:()->Unit) {

    val idRequirements = stringArrayResource(Res.array.identity_verification_requirements)

    val isButtonEnabled by remember(state.documentFrontCapturedPath,state.documentBackCapturedPath){derivedStateOf {
        if (state.selectedVerificationMethod == IDProfileVerificationScreenState.VerificationMethod.GOVERNMENT_ISSUED_ID) state.documentFrontCapturedPath.isNotBlank() && state.documentBackCapturedPath.isNotBlank()
        else state.documentFrontCapturedPath.isNotBlank()
    }}

    Column(Modifier.fillMaxSize()) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(Modifier.background(if (isDarkMode) BASE_DARK else Color.White).fillMaxSize().weight(1f).padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
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
                        modifier = Modifier.width(18.dp).height(14.dp),
                        imageVector = vectorResource(Res.drawable.ic_left),
                        contentDescription = null
                    )
                }


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

            Spacer(Modifier.height(21.dp))

            Text(
                text = stringResource(Res.string.identity_verification),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if(isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.take_photo_of_id),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(16.dp))


            idRequirements.forEachIndexed { index, item ->
                IDRequirementsItem(
                    showTickIcon = index in 0..3,
                    text = item,
                    isDarkMode = isDarkMode
                )
                if (index != idRequirements.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                }
            }
            Spacer(Modifier.weight(1.36f))


            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {

                if (state.selectedVerificationMethod == IDProfileVerificationScreenState.VerificationMethod.GOVERNMENT_ISSUED_ID) {
                    ScanDocumentButton(
                        modifier = Modifier.weight(1f).aspectRatio(159 / 135f)
                            .noRippleClickable {
                                screenModel.updateCurrentDocCapturingFaceState(IDProfileVerificationScreenState.DocumentFace.FRONT)
                                screenModel.checkCameraPermissionStatusAndNavigate(navigate = navigateToScanDocumentScreen)
                            },
                        text = stringResource(Res.string.front_of_document),
                        path = state.documentFrontCapturedPath,
                        isDarkMode = isDarkMode
                    )
                    ScanDocumentButton(
                        modifier = Modifier.weight(1f).aspectRatio(159 / 135f)
                            .noRippleClickable {
                                screenModel.updateCurrentDocCapturingFaceState(IDProfileVerificationScreenState.DocumentFace.BACK)
                                screenModel.checkCameraPermissionStatusAndNavigate(navigate = navigateToScanDocumentScreen)
                            },
                        text = stringResource(Res.string.back_of_document),
                        path = state.documentBackCapturedPath,
                        isDarkMode = isDarkMode
                    )
                } else {
                    ScanDocumentButton(
                        modifier = Modifier
                            .aspectRatio(344 / 308.22f)
                            .noRippleClickable {
                                screenModel.updateCurrentDocCapturingFaceState(IDProfileVerificationScreenState.DocumentFace.FRONT)
                                screenModel.checkCameraPermissionStatusAndNavigate(navigate = navigateToScanDocumentScreen)
                            },
                        text = stringResource(Res.string.scan_document),
                        path = state.documentFrontCapturedPath,
                        isDarkMode = isDarkMode
                    )
                }
            }
            

            Spacer(Modifier.weight(1f))


            ButtonWithText(
                text = stringResource(Res.string.continue_txt),
                bgColor = if (isButtonEnabled) Color(0xffF33358) else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (isButtonEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (isButtonEnabled) {
                        navigateToImageVerification()
                    }
                }
            )

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
        AlertMessageDialog(title =  stringResource(Res.string.camera_permissions_required),
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

@Composable
fun ScanDocumentButton(modifier: Modifier,text: String,path:String,isDarkMode: Boolean) {

    Box(modifier.fillMaxWidth().then(if (path.isBlank()) Modifier.dashedBorder(1.dp,16.dp, Color(0xffD0D5DD)) else Modifier.clip(RoundedCornerShape(16.dp)))) {
        if (path.isNotBlank()) {
            CapturedImagePreview(modifier = Modifier.fillMaxSize(), path = path)
        } else {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_scan_document),
                    contentDescription = null
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = text,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )
            }
        }
    }
    
}


@Composable
fun IDRequirementsItem(showTickIcon:Boolean,text:String,isDarkMode: Boolean) {
    Row(
        Modifier.height(21.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.requiredSize(16.dp),
            imageVector = vectorResource(if (showTickIcon) Res.drawable.ic_check else Res.drawable.ic_cross_small),
            contentDescription = null
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )


    }
}