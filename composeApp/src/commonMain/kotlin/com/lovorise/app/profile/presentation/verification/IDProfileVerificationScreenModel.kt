package com.lovorise.app.profile.presentation.verification

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lovorise.app.libs.camera.CameraFacingMode
import com.lovorise.app.libs.camera.FlashMode
import com.lovorise.app.libs.permissions.DeniedAlwaysException
import com.lovorise.app.libs.permissions.DeniedException
import com.lovorise.app.libs.permissions.Permission
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions.PermissionsController
import com.lovorise.app.libs.permissions.RequestCanceledException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IDProfileVerificationScreenModel : ScreenModel {


    private val _state = MutableStateFlow(IDProfileVerificationScreenState())
    val state = _state.asStateFlow()

    private var controller: PermissionsController? = null

    fun onInit(controller: PermissionsController){
       // if (this.controller != null) return
        this.controller = controller
    }

    fun updateVerificationMethod(method: IDProfileVerificationScreenState.VerificationMethod){
        _state.update {
            it.copy(
                selectedVerificationMethod = method
            )
        }
    }

    fun updateRationaleState(value:Boolean){
        _state.update {
            it.copy(
                showPermissionRationale = value
            )
        }
    }

    fun openSettings(){
        controller?.openAppSettings()
    }

    fun onCaptureImage(){
        if (state.value.capturedImagePath.isNotBlank()) return
        _state.update {
            it.copy(
                captureImage = true
            )
        }
    }

    fun onStartRecording(){
        if (state.value.capturedImagePath.isNotBlank()) return
        _state.update {
            it.copy(
                startRecording = true
            )
        }
    }

    fun onStopRecording(){
        if (state.value.capturedImagePath.isNotBlank()) return
        _state.update {
            it.copy(
                startRecording = false
            )
        }
    }

    fun updateCapturedPath(value:String){
        _state.update {
            it.copy(
                capturedImagePath = value,
                captureImage = false
            )
        }
    }

    fun updateCapturedProfilePath(value:String){
        _state.update {
            it.copy(
                capturedProfilePath = value,
                captureImage = false
            )
        }
    }

    fun onDoneCapturing(){
        val path = state.value.capturedImagePath
        if (path.isBlank()) return
        _state.update {
            it.copy(
                documentFrontCapturedPath = if (state.value.currentDocCapturingFace == IDProfileVerificationScreenState.DocumentFace.FRONT) path else state.value.documentFrontCapturedPath,
                documentBackCapturedPath = if (state.value.currentDocCapturingFace == IDProfileVerificationScreenState.DocumentFace.BACK) path else state.value.documentBackCapturedPath,
            )
        }
    }

    fun updateCurrentDocCapturingFaceState(value:IDProfileVerificationScreenState.DocumentFace){
        _state.update {
            it.copy(
                currentDocCapturingFace = value
            )
        }
    }

    fun checkCameraPermissionStatusAndNavigate(navigate:()->Unit){
        screenModelScope.launch {
            val isPermissionGranted = controller?.isPermissionGranted(Permission.CAMERA)
            if (isPermissionGranted == true){
                navigate()
            } else if (isPermissionGranted == false){
                provideOrRequestCameraPermission()
            }
        }
    }

    fun checkCameraAndAudioPermissionStatusAndNavigate(navigate:()->Unit){
        screenModelScope.launch {
            val isCameraPermissionGranted = controller?.isPermissionGranted(Permission.CAMERA)
            val isAudioPermissionGranted = controller?.isPermissionGranted(Permission.RECORD_AUDIO)
            println("the audio permission granted $isAudioPermissionGranted")
            if (isAudioPermissionGranted == true && isCameraPermissionGranted == true){
                navigate()
            }else{
                if (isAudioPermissionGranted != true){
                    provideOrRequestAudioPermission { provideOrRequestCameraPermission(navigate) }
                }
                else if (isCameraPermissionGranted != true){
                    provideOrRequestCameraPermission { provideOrRequestAudioPermission(navigate) }
                }
            }

        }
    }

    private fun provideOrRequestCameraPermission(onGranted:()->Unit = {}){
        if (state.value.cameraPermissionState == PermissionState.DeniedAlways) {
            updateRationaleState(true)
            return
        }

        if (state.value.cameraPermissionState != PermissionState.NotDetermined){
            _state.update {
                it.copy(cameraPermissionState = PermissionState.NotDetermined)
            }
        }
        screenModelScope.launch {
            _state.update {
                it.copy(
                    cameraPermissionState = try {
                        controller?.providePermission(Permission.CAMERA)
                        onGranted()
                        PermissionState.Granted
                    } catch(e: DeniedAlwaysException) {
                        PermissionState.DeniedAlways
                    } catch(e: DeniedException) {
                        PermissionState.Denied
                    } catch(e: RequestCanceledException) {
                        PermissionState.NotDetermined
                    }
                )
            }
        }
    }

    private fun provideOrRequestAudioPermission(onGranted:()->Unit = {}){
        if (state.value.audioPermissionState == PermissionState.DeniedAlways) {
            updateRationaleState(true)
            return
        }

        if (state.value.audioPermissionState != PermissionState.NotDetermined){
            _state.update {
                it.copy(audioPermissionState = PermissionState.NotDetermined)
            }
        }
        screenModelScope.launch {
            _state.update {
                it.copy(
                    audioPermissionState = try {
                        controller?.providePermission(Permission.RECORD_AUDIO)
                        onGranted()
                        PermissionState.Granted
                    } catch(e: DeniedAlwaysException) {
                        PermissionState.DeniedAlways
                    } catch(e: DeniedException) {
                        PermissionState.Denied
                    } catch(e: RequestCanceledException) {
                        PermissionState.NotDetermined
                    }
                )
            }
        }
    }

    fun toggleCameraFacingMode(){
        _state.update {
            it.copy(
                cameraFacingMode = if(state.value.cameraFacingMode == CameraFacingMode.BACK) CameraFacingMode.FRONT else CameraFacingMode.BACK,
            )
        }
    }

    fun toggleFlashMode(){
        _state.update {
            it.copy(
                flashMode = if(state.value.flashMode == FlashMode.ON) FlashMode.OFF else FlashMode.ON,
            )
        }
    }

    fun resetState(){
        _state.update {
            it.copy(
                capturedImagePath = "",
                documentBackCapturedPath = "",
                documentFrontCapturedPath = ""
            )
        }
    }

}