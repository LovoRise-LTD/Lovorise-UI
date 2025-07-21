package com.lovorise.app.profile.presentation.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.copy_the_pose_in_photo
import coinui.composeapp.generated.resources.ic_capture_image
import coinui.composeapp.generated.resources.ic_cross_white
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.libs.camera.CameraFacingMode
import com.lovorise.app.libs.camera.CameraPreview
import com.lovorise.app.libs.camera.CaptureMode
import com.lovorise.app.libs.camera.FlashMode
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class CaptureImageScreen: Screen {

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


        CaptureImageScreenContent(
            isDarkMode = isDarkMode,
            state = state,
            screenModel = screenModel,
            onBack = {navigator.pop()}
        )

    }
}

@Composable
fun CaptureImageScreenContent(isDarkMode:Boolean,onBack:()->Unit,state: IDProfileVerificationScreenState,screenModel: IDProfileVerificationScreenModel) {



    Column(Modifier.fillMaxSize()) {

//        Spacer(
//            modifier = Modifier
//                .background(if (isDarkMode) BASE_DARK else Color.White)
//                .windowInsetsTopHeight(WindowInsets.statusBars)
//                .fillMaxWidth()
//        )

        Column(Modifier.fillMaxSize().weight(1f)) {

            Box(Modifier.fillMaxSize().background(Color(0xff111111))){
                CameraPreview(
                    modifier = Modifier.matchParentSize(),
                    captureMode = CaptureMode.VIDEO,
                    flashMode = FlashMode.OFF,
                    cameraFacingMode = CameraFacingMode.FRONT,
                    startRecording = state.startRecording,
                    capturePhoto = false,
                   // onCaptureComplete = { screenModel.updateCapturedProfilePath(it); onBack() },
                    onRecordingComplete = { screenModel.updateCapturedProfilePath(it); onBack() }
                    //resolution = IntSize(344,308)
                )

                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(Modifier.fillMaxWidth()) {
                        Box(
                            Modifier.padding(start = 16.dp, top = 50.dp).size(24.dp).noRippleClickable(onBack),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                imageVector = vectorResource(Res.drawable.ic_cross_white),
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.weight(1f))

                    if (state.startRecording) {
                        Box(
                            modifier = Modifier.size(56.dp).noRippleClickable{screenModel.onStopRecording()}
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                Modifier.size(16.dp)
                                    .background(Color(0xffF33358), RoundedCornerShape(2.dp))
                            )
                        }
                    }else {
                        Image(
                            imageVector = vectorResource(Res.drawable.ic_capture_image),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp).noRippleClickable {
                                screenModel.onStartRecording()
                            }
                        )
                    }
                    Spacer(Modifier.height(17.dp))

                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(Res.string.copy_the_pose_in_photo),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        letterSpacing = 0.2.sp
                    )

                    Spacer(Modifier.height(70.dp))
                }
            }



        }

//        Spacer(
//            modifier = Modifier
//                .windowInsetsBottomHeight(WindowInsets.navigationBars)
//                .fillMaxWidth()
//                .background(if (isDarkMode) BASE_DARK else Color.White)
//        )
    }




}

