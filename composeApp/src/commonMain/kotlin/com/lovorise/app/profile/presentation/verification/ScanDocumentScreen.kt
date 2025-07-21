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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.done
import coinui.composeapp.generated.resources.ic_camera_guide_bottom_left
import coinui.composeapp.generated.resources.ic_camera_guide_bottom_right
import coinui.composeapp.generated.resources.ic_camera_guide_top_left
import coinui.composeapp.generated.resources.ic_camera_guide_top_right
import coinui.composeapp.generated.resources.ic_capture_image
import coinui.composeapp.generated.resources.ic_flash_off
import coinui.composeapp.generated.resources.ic_flash_on
import coinui.composeapp.generated.resources.ic_retake
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.scan_document
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.libs.camera.CameraPreview
import com.lovorise.app.libs.camera.CaptureMode
import com.lovorise.app.libs.camera.FlashMode
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ScanDocumentScreen: Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = navigator.koinNavigatorScreenModel<IDProfileVerificationScreenModel>()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(true){
            screenModel.updateCapturedPath("")
        }


        ScanDocumentScreenContent(
            isDarkMode = isDarkMode,
            state = state,
            screenModel = screenModel,
            onBack = {navigator.pop()}
        )

    }
}

@Composable
fun ScanDocumentScreenContent(isDarkMode:Boolean,onBack:()->Unit,state: IDProfileVerificationScreenState,screenModel: IDProfileVerificationScreenModel) {



    Column(Modifier.fillMaxSize()) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(Modifier.fillMaxSize().background(if (isDarkMode) BASE_DARK else Color.White).weight(1f).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(13.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(26.dp),
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

                Spacer(Modifier.weight(1f))

                Text(
                    text = stringResource(Res.string.scan_document),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Spacer(Modifier.weight(1f))

            }

            Spacer(Modifier.weight(1f))


            Box(Modifier.fillMaxWidth().aspectRatio(344/308.22f).background(Color(0xff111111), RoundedCornerShape(16.dp))){
                CameraPreview(
                    modifier = Modifier.matchParentSize().clip(RoundedCornerShape(16.dp)),
                    captureMode = CaptureMode.PHOTO,
                    flashMode = state.flashMode,
                    cameraFacingMode = state.cameraFacingMode,
                    startRecording = false,
                    capturePhoto = state.captureImage,
                    onCaptureComplete = screenModel::updateCapturedPath,
                    resolution = IntSize(344,308)
                )
                CameraPreviewGuide()
                if (state.capturedImagePath.isNotBlank()){
                    CapturedImagePreview(path = state.capturedImagePath)
                }
            }


            Spacer(Modifier.weight(1.26f))



            CameraControls(
                flashMode = state.flashMode,
                onCancel = {screenModel.updateCapturedPath("")},
                onDone = { screenModel.onDoneCapturing(); onBack() },
                toggleCameraFace = screenModel::toggleCameraFacingMode,
                toggleFlashMode = screenModel::toggleFlashMode,
                onCapture = screenModel::onCaptureImage,
                isDarkMode = isDarkMode
            )




            Spacer(Modifier.height(36.dp))




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
fun CapturedImagePreview(modifier: Modifier = Modifier,path:String) {

    Box(modifier = modifier.fillMaxSize().background(Color.Black,RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center){
        AsyncImage(
            model = path,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }

}

@Composable
fun CameraControls(flashMode: FlashMode,onCancel:()->Unit,onDone:()->Unit,toggleFlashMode:()->Unit,toggleCameraFace:()->Unit,onCapture:()->Unit,isDarkMode: Boolean) {

    Box(Modifier.fillMaxWidth().height(92.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffAA98B3).copy(alpha = 0.58f), shape = RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center){
        Row(Modifier.fillMaxWidth().padding(horizontal = 11.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            CancelDoneButton(
                text = stringResource(Res.string.cancel),
                onClick = onCancel,
                isDarkMode = isDarkMode
            )

            Image(
                modifier = Modifier.height(32.dp).width(29.dp).noRippleClickable(toggleCameraFace),
                imageVector = vectorResource(Res.drawable.ic_retake),
                contentDescription = null
            )

            Image(
                imageVector = vectorResource(Res.drawable.ic_capture_image),
                contentDescription = null,
                modifier = Modifier.size(56.dp).noRippleClickable(onCapture)
            )

            Image(
                imageVector = vectorResource(if (flashMode == FlashMode.ON) Res.drawable.ic_flash_on else Res.drawable.ic_flash_off),
                contentDescription = null,
                modifier = Modifier.height(28.dp).width(17.23.dp).noRippleClickable(toggleFlashMode)
            )


            CancelDoneButton(
                text = stringResource(Res.string.done),
                onClick = onDone,
                isDarkMode = isDarkMode
            )
            

        }
    }
}

@Composable
fun CancelDoneButton(text:String,onClick: () -> Unit,isDarkMode: Boolean) {
    Box(Modifier.size(width = 74.dp, height = 32.dp).background(if (isDarkMode) BASE_DARK else Color.White, RoundedCornerShape(16.dp)).noRippleClickable(onClick), contentAlignment = Alignment.Center){
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )
    }
}


@Composable
fun CameraPreviewGuide() {
    Box(Modifier.fillMaxSize().padding(11.dp)){
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_camera_guide_top_left),
                    contentDescription = null
                )
                Image(
                    imageVector = vectorResource(Res.drawable.ic_camera_guide_top_right),
                    contentDescription = null
                )
            }

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_camera_guide_bottom_left),
                    contentDescription = null
                )
                Image(
                    imageVector = vectorResource(Res.drawable.ic_camera_guide_bottom_right),
                    contentDescription = null
                )
            }
        }
    }

}