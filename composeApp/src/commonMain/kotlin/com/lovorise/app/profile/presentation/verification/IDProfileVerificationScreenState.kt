package com.lovorise.app.profile.presentation.verification

import com.lovorise.app.libs.camera.CameraFacingMode
import com.lovorise.app.libs.camera.FlashMode
import com.lovorise.app.libs.permissions.PermissionState

data class IDProfileVerificationScreenState(
    val selectedCountryIndex:Int = 0,
    val selectedVerificationMethod:VerificationMethod = VerificationMethod.GOVERNMENT_ISSUED_ID,
    val documentFrontCapturedPath:String = "",
    val documentBackCapturedPath:String = "",
    val flashMode: FlashMode = FlashMode.ON,
    val capturedProfilePath:String = "",
    val cameraPermissionState:PermissionState = PermissionState.NotDetermined,
    val audioPermissionState:PermissionState = PermissionState.NotDetermined,
    val showPermissionRationale:Boolean = false,
    val cameraFacingMode:CameraFacingMode = CameraFacingMode.BACK,
    val currentDocCapturingFace:DocumentFace = DocumentFace.FRONT,
    val captureImage:Boolean = false,
    val startRecording:Boolean = false,
    val capturedImagePath:String = "",
    val profileMatched:Boolean = true
){
    enum class VerificationMethod{
        GOVERNMENT_ISSUED_ID, DRIVING_LICENSE, PASSPORT
    }

    enum class DocumentFace{
        FRONT,BACK
    }
}
