package com.lovorise.app.libs.camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize


@Composable
expect fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraFacingMode: CameraFacingMode = CameraFacingMode.BACK,
    flashMode: FlashMode = FlashMode.OFF,
    captureMode: CaptureMode = CaptureMode.PHOTO,
    capturePhoto: Boolean,
    startRecording: Boolean,
    onCaptureComplete: (String) -> Unit = {},
    onRecordingComplete: (String) -> Unit = {},
    resolution:IntSize = IntSize(1080, 1920)
)

@Composable
expect fun IosImageView(imgData:Any,modifier: Modifier = Modifier.fillMaxSize(),onImageLoaded:(Any)->Unit={},size: IntSize? = null,aspect: ImageAspect? = null,refreshCache:Boolean = false)