package com.lovorise.app.libs.camera

import android.annotation.SuppressLint
import android.util.Size
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.impl.UseCaseConfigFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.ExperimentalPersistentRecording
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.datetime.Clock
import java.io.File


@OptIn(ExperimentalPersistentRecording::class)
@SuppressLint("RestrictedApi", "MissingPermission")
@Composable
actual fun CameraPreview(
    modifier: Modifier,
    cameraFacingMode: CameraFacingMode,
    flashMode: FlashMode,
    captureMode: CaptureMode,
    capturePhoto: Boolean,
    startRecording: Boolean,
    onCaptureComplete: (String) -> Unit,
    onRecordingComplete: (String) -> Unit,
    resolution:IntSize
){
    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    val previewView = remember { PreviewView(context) }

    var isRecording by remember { mutableStateOf(false) }

    val recorder = remember {
        Recorder.Builder()
            .setExecutor(ContextCompat.getMainExecutor(context))
            .setQualitySelector(
                QualitySelector.fromOrderedList(
                    listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
                    FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
                )
            )
            .build()
    }

    var recording by remember { mutableStateOf<Recording?>(null) }

    val videoCapture: VideoCapture<Recorder> = remember { VideoCapture.withOutput(recorder) }

    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().apply {
            setCaptureType(UseCaseConfigFactory.CaptureType.IMAGE_CAPTURE)
            setHighResolutionDisabled(false)
            setTargetResolution(Size(resolution.width, resolution.width))
            setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            setJpegQuality(100)
        }.build()

    }

    LaunchedEffect(capturePhoto) {
        if (capturePhoto) {
            val photoFile = File(context.cacheDir, "photo_${Clock.System.now().toEpochMilliseconds()}.jpg")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        onCaptureComplete(photoFile.absolutePath)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        // Handle error appropriately
                    }
                }
            )
        }
    }

    LaunchedEffect(startRecording) {
        if (recording == null && !isRecording && startRecording) {
            val videoFile = File(context.cacheDir, "video_${Clock.System.now().toEpochMilliseconds()}.mp4")
            val outputOptions = FileOutputOptions.Builder(videoFile).build()
            recording = videoCapture.output
                .prepareRecording(context, outputOptions)
                .asPersistentRecording()
                .withAudioEnabled()
                .start(ContextCompat.getMainExecutor(context)) {recordEvent ->
                    when(recordEvent) {
                        is VideoRecordEvent.Start -> {
                            isRecording = true
                        }
                        is VideoRecordEvent.Finalize -> {
                            if (!recordEvent.hasError()){
                                onRecordingComplete(videoFile.absolutePath)
                            } else {
                                recording?.close()
                                recording = null
                                println("Video capture ends with error: " +
                                        "${recordEvent.cause}")
                            }
                        }

                    }
                }
        } else {
            recording?.stop()
            recording = null
            isRecording = false
        }
    }

    AndroidView(
        factory = {
            previewView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                clipToOutline = true
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }


            val cameraSelector = when (cameraFacingMode) {
                CameraFacingMode.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                CameraFacingMode.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
            }


            cameraProvider.unbindAll()
            // Bind the appropriate use cases based on capture mode
            val camera = if (captureMode == CaptureMode.PHOTO) {
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
            }
            else {
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture
                )
            }
            val cameraControl = camera.cameraControl
            cameraControl.setZoomRatio(1f)
            cameraControl.enableTorch(flashMode == FlashMode.ON)
        }, ContextCompat.getMainExecutor(context))
    }



}

@Composable
actual fun IosImageView(imgData:Any,modifier: Modifier,onImageLoaded:(Any)->Unit,size: IntSize?,aspect:ImageAspect?,refreshCache:Boolean){}