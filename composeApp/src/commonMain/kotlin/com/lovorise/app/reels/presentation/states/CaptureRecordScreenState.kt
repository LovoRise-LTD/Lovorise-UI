package com.lovorise.app.reels.presentation.states

import androidx.compose.ui.graphics.ImageBitmap
import com.lovorise.app.accounts.presentation.signup.profile_upload.ProfileUploadScreenState.GalleryImageVideo
import com.lovorise.app.libs.camera.CameraFacingMode
import com.lovorise.app.libs.camera.CaptureMode
import com.lovorise.app.libs.camera.FlashMode
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.CaptureRecordPromptAction
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelPrivacySetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CaptureRecordScreenState(
    val cameraFacingMode:CameraFacingMode = CameraFacingMode.FRONT,
    val captureMode: CaptureMode = CaptureMode.PHOTO,
    val flashMode: FlashMode = FlashMode.OFF,
    val currentCaptureRecordPromptAction: CaptureRecordPromptAction = CaptureRecordPromptAction.PHOTO,
    val galleryImages:List<GalleryImageVideo> = emptyList(),
    val galleryVideos:List<VideoData> = emptyList(),
    val cameraPermissionState: PermissionState = PermissionState.NotDetermined,
    val galleryPermissionState: PermissionState = PermissionState.NotDetermined,
    val microphonePermissionState: PermissionState = PermissionState.NotDetermined,
    val selectedImageIndex:Int = -1,
    val selectedVideoIndex:Int = -1,
    val selectedContentCaption:String = "",
    val isRecording:Boolean = false,
    val timerValue:Flow<String> = flow {  },
    val captureImage:Boolean = false,
    val capturedImagePath:String? = null,
    val capturedVideoPath:String? = null,
    val renderUi:Boolean = false,
    val showPrivacyBottomSheet:Boolean = false,
    val reelsPrivacySettings: ReelPrivacySetting = ReelPrivacySetting.Everyone,
    val videoFrames:List<Any> = emptyList(),
    val isVideoPlaying:Boolean = false,
    val isVideoMuted:Boolean = false,
    val showPauseButton:Boolean = false,
    val videoClipRange: ClosedFloatingPointRange<Float> = 0f..100f,
    val exportedVideoPath:String? = null,
    val isLoading:Boolean = false,
    val toastMessage:String = "",
    val permissionRationalDialog: Boolean =  false
){
    data class VideoData(
        val id:String,
        val thumbnail:ImageBitmap?,
        val videoUri:String,
        val uiImage:Any?
    )
}
