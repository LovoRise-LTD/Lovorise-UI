package com.lovorise.app.reels.presentation.viewModels

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import com.lovorise.app.MediaItem
import com.lovorise.app.getImages
import com.lovorise.app.getImagesFromDevice
import com.lovorise.app.getVideoById
import com.lovorise.app.getVideoFrames
import com.lovorise.app.getVideos
import com.lovorise.app.getVideosThumbnailsFromDevice
import com.lovorise.app.libs.camera.CameraFacingMode
import com.lovorise.app.libs.camera.CaptureMode
import com.lovorise.app.libs.camera.FlashMode
import com.lovorise.app.libs.media_player.exportVideoSegment
import com.lovorise.app.libs.permissions.DeniedAlwaysException
import com.lovorise.app.libs.permissions.DeniedException
import com.lovorise.app.libs.permissions.Permission
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions.PermissionsController
import com.lovorise.app.libs.permissions.RequestCanceledException
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.CaptureRecordPromptAction
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelPrivacySetting
import com.lovorise.app.reels.presentation.states.CaptureRecordScreenState
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CaptureRecordScreenModel : ScreenModel {

    private val _state = MutableStateFlow(CaptureRecordScreenState())
    val state = _state.asStateFlow()

    private var controller: PermissionsController? = null
    private var context: PlatformContext? = null


    val imagePaginationState = PaginationState<Int, MediaItem>(
        initialPageKey = 1,
        onRequestPage = { loadImagePage(it) }
    )

    val videoPaginationState = PaginationState<Int, MediaItem>(
        initialPageKey = 1,
        onRequestPage = { loadVideoPage(it) }
    )

    fun onInit(controller: PermissionsController?,context: PlatformContext){
        //if (this.controller != null) return
        if (controller != null) {
            this.controller = controller
        }
        this.context = context
    }


    fun retryFetching(){
        if (imagePaginationState.allItems.isNullOrEmpty()){
            imagePaginationState.refresh(1)
        }

        if (videoPaginationState.allItems.isNullOrEmpty()){
            videoPaginationState.refresh(1)
        }
    }

    private fun loadImagePage(page:Int){
        if (context == null) throw Exception("Without context page cannot be loaded")
        println("the loadImagePage function is called")
        screenModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val items = getImages(context!!, pageNumber = page, pageSize = 20)
                    println("the items is ${items.size}")
                    imagePaginationState.appendPage(
                        items = items,
                        isLastPage = items.isEmpty(),
                        nextPageKey = page + 1
                    )


                }catch (e:Exception){
                    e.printStackTrace()
                    //LoadResult.Error(e)
                }

            }
        }
    }

    fun updatePauseButtonState(value: Boolean){
        _state.update{
            it.copy(
                showPauseButton = value
            )
        }
    }

    fun toggleVideoMutedState(){
        _state.update{
            it.copy(
                isVideoMuted = !state.value.isVideoMuted
            )
        }
    }

    private fun loadVideoPage(page:Int){
        if (context == null) throw Exception("Without context page cannot be loaded")
        println("the loadVideoPage function is called")
        screenModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val items = getVideos(context!!, pageNumber = page, pageSize = 20)
                    println("the items is ${items.size}")
                    videoPaginationState.appendPage(
                        items = items,
                        isLastPage = items.isEmpty(),
                        nextPageKey = page + 1
                    )


                }catch (e:Exception){
                    e.printStackTrace()
                    //LoadResult.Error(e)
                }

            }
        }
    }


    fun toggleFlashMode(){
        _state.update {
            it.copy(
                flashMode = if(state.value.flashMode == FlashMode.OFF) FlashMode.ON else FlashMode.OFF,
            )
        }
    }

    fun updateLoadingState(value: Boolean){
        _state.update{
            it.copy(
                isLoading = value
            )
        }
    }

    fun updateToastMessage(value:String){
        _state.update {
            it.copy(
                toastMessage = value
            )
        }
    }

    fun setCaptureRecordPromptAction(action: CaptureRecordPromptAction){
        _state.update {
            it.copy(
                currentCaptureRecordPromptAction = action
            )
        }
    }

    fun exportClippedVideo(context: PlatformContext,onVideoExported:(String)->Unit){
        if (state.value.isLoading) return
        val data = (if (!state.value.capturedVideoPath.isNullOrBlank()) state.value.capturedVideoPath else videoPaginationState.allItems?.getOrNull(state.value.selectedVideoIndex)?.videoUri) ?: return
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        screenModelScope.launch {
            exportVideoSegment(
                context,
                url = data,
                clipRange = state.value.videoClipRange
            ) { success, path ->
                println("the exportVideoSegment $success $path")
                if (success && !path.isNullOrBlank()) {
                    _state.update {
                        it.copy(
                            exportedVideoPath = path,
                            isLoading = false
                        )
                    }
                    onVideoExported(path)
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun setPrivacyBottomSheetState(value: Boolean){
        _state.update {
            it.copy(
                showPrivacyBottomSheet = value
            )
        }
    }

    fun setReelsPrivacy(setting: ReelPrivacySetting){
        _state.update {
            it.copy(
                reelsPrivacySettings = setting
            )
        }
    }

    fun updateVideoClipRange(range: ClosedFloatingPointRange<Float>){
        _state.update {
            it.copy(
                videoClipRange = range
            )
        }
    }


    fun startRecording(secondsValue:Int){
        _state.update {
            it.copy(
                captureMode = CaptureMode.VIDEO,
                isRecording = true,
                timerValue = flow {
                    for (time in 0..secondsValue) {
                        val minutes = time / 60
                        val seconds = time % 60
                        // Manually format the time as "$MM:SS"
                        val formattedTime = buildString {
                            append(minutes)
                            append(":")
                            if (seconds < 10) append("0") // Add leading zero for single-digit seconds
                            append(seconds)
                        }
                        emit(formattedTime)
                        delay(1000L) // 1 second delay
                    }
                }
            )
        }
    }

    fun stopRecording(){
        _state.update {
            it.copy(
                isRecording = false
            )
        }
    }

    fun captureImage(){
        _state.update {
            it.copy(
                captureMode = CaptureMode.PHOTO,
                captureImage = true,
            )
        }
    }

    fun onVideoRecordCompleted(path:String?){
        _state.update {
            it.copy(
                capturedVideoPath = path,
                selectedImageIndex = -1,
                selectedVideoIndex = -1,
                captureImage = false,
                capturedImagePath = null
            )
        }
    }

    fun onCapturedImage(path:String?){
        println("the capturedPath = $path")
        _state.update {
            it.copy(
                capturedImagePath = path,
                selectedImageIndex = -1,
                selectedVideoIndex = -1,
                captureImage = false
            )
        }
    }

    fun openSettings(){
        controller?.openAppSettings()
    }

    fun loadVideoFrames(context: PlatformContext,path: String){
        println("the loadVodeoframes is called $path")
        screenModelScope.launch {
            val frames = getVideoFrames(context, path).frames
            println("the frames for videos ${frames.size} $frames")
            _state.update {
                it.copy(
                    videoFrames = frames
                )
            }
        }
    }

    fun loadImagesFromGallery(context: PlatformContext, currentRequestIndex:Int){
//        if (isAndroid() && state.value.galleryImages.isNotEmpty()) return
//        else if (!isAndroid() && (state.value.isAllGalleryImagesLoaded || currentRequestIndex <= state.value.lastRequestedAtIndex)) return

        screenModelScope.launch {

            _state.update {
                it.copy(
                //    lastRequestedAtIndex = currentRequestIndex
                )
            }

            getImagesFromDevice(context, startIndex = if (state.value.galleryImages.isEmpty()) 0 else state.value.galleryImages.lastIndex).collectLatest { images ->
//                val imagesList = state.value.galleryImages.toMutableList().apply {
//                    addAll(
//                        images.map {path-> ProfileUploadScreenState.GalleryImageVideo(path,0) }
//                    )
//                }

              //  val isAllLoaded = if (images.isEmpty()) controller.isPermissionGranted(Permission.GALLERY) else false

//                if (!isAndroid() && !isAllLoaded && currentRequestIndex == 0){
//                    _state.update {
//                        it.copy(
//                        //    lastRequestedAtIndex = -1
//                        )
//                    }
//                }

                _state.update {
                    it.copy(
                        //galleryImages = imagesList,
                      //  selectedGalleryImage = ProfileUploadScreenState.GalleryImage(imagesList.firstOrNull()?.imagePath ?: "",0),
                     //   isAllGalleryImagesLoaded = isAllLoaded,
                    )
                }
            }
        }
    }

    fun updateSectionImageIndex(index:Int){
        _state.update {
            it.copy(
                selectedImageIndex = index,
                selectedVideoIndex = -1
            )
        }
    }

    fun onCaptionValueChange(value:String){
        _state.update {
            it.copy(
                selectedContentCaption = value
            )
        }
    }

    private fun updateVideoFrames(value:List<ImageBitmap>){
        _state.update {
            it.copy(
                videoFrames = value
            )
        }
    }

    fun updateSectionVideoIndex(index:Int){
        _state.update {
            it.copy(
                selectedVideoIndex = index,
                selectedImageIndex = -1
            )
        }
    }

    fun loadImageIfRequired(){
        screenModelScope.launch {
            val index = state.value.selectedImageIndex
//            if (index != -1 && !state.value.galleryImages[index].imagePath.instanceOf(String::class)){
//                val imagePath = getPathForUiImage(state.value.galleryImages[index].imagePath)
//                if (imagePath != null) {
//                    _state.update {
//                        it.copy(
//                            galleryImages = state.value.galleryImages.toMutableList().apply {
//                                add(index, removeAt(index).copy(imagePath = imagePath))
//                            }
//                        )
//                    }
//                }
//            }
        }
    }

    fun loadVideoIfRequired(){
        screenModelScope.launch {
            if(state.value.selectedVideoIndex != -1){
                val index = state.value.selectedVideoIndex
                if (videoPaginationState.allItems?.get(index)?.videoUri?.isBlank() == true) {
                    val videoUri = getVideoById(state.value.galleryVideos[index].id)
                    println("the video uri for selected is $videoUri")
                    if (videoUri != null) {
                        _state.update {
                            it.copy(
                                galleryVideos = state.value.galleryVideos.toMutableList().apply {
                                    add(index, removeAt(index).copy(videoUri = videoUri))
                                }
                            )
                        }
                    }
                }
            }
        }

    }

    fun loadVideosFromGallery(context: PlatformContext, currentRequestIndex:Int){
//        if (isAndroid() && state.value.galleryImages.isNotEmpty()) return
//        else if (!isAndroid() && (state.value.isAllGalleryImagesLoaded || currentRequestIndex <= state.value.lastRequestedAtIndex)) return

        screenModelScope.launch {

            _state.update {
                it.copy(
                    //    lastRequestedAtIndex = currentRequestIndex
                )
            }

            getVideosThumbnailsFromDevice(context, startIndex = if (state.value.galleryImages.isEmpty()) 0 else state.value.galleryImages.lastIndex).collectLatest { images ->
                val imagesList = state.value.galleryVideos.toMutableList().apply {
                    addAll(
                        images
                    )
                }
                println("the images list is ${imagesList.size}")

                //  val isAllLoaded = if (images.isEmpty()) controller.isPermissionGranted(Permission.GALLERY) else false

//                if (!isAndroid() && !isAllLoaded && currentRequestIndex == 0){
//                    _state.update {
//                        it.copy(
//                        //    lastRequestedAtIndex = -1
//                        )
//                    }
//                }

                _state.update {
                    it.copy(
                        galleryVideos = imagesList,
                        //  selectedGalleryImage = ProfileUploadScreenState.GalleryImage(imagesList.firstOrNull()?.imagePath ?: "",0),
                        //   isAllGalleryImagesLoaded = isAllLoaded,
                    )
                }
            }
        }
    }

    fun setCaptureMode(mode:CaptureMode){
        _state.update {
            it.copy(
                captureMode = mode
            )
        }
    }

    fun updateRationaleState(value: Boolean){
        if (value == state.value.permissionRationalDialog) return
        _state.update {
            it.copy(
                permissionRationalDialog = value
            )
        }
    }

    fun provideOrRequestMicrophonePermission(){
        if (controller == null) return
        if (state.value.microphonePermissionState == PermissionState.DeniedAlways) {
            updateRationaleState(true)
            return
        }
        if (state.value.microphonePermissionState != PermissionState.NotDetermined){
            _state.update {
                it.copy(microphonePermissionState = PermissionState.NotDetermined)
            }
        }
        screenModelScope.launch {
            _state.update {
                it.copy(
                    microphonePermissionState = try {
                        controller?.providePermission(Permission.RECORD_AUDIO)
                        PermissionState.Granted
                    } catch(e: DeniedAlwaysException) {
                        e.printStackTrace()
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

    fun checkPermissionStatus(){
        if (controller == null) return
        screenModelScope.launch {

            withContext(Dispatchers.Main){
                val cameraState = async {controller?.getPermissionState(Permission.CAMERA) }
                val microphoneState = async {controller?.getPermissionState(Permission.RECORD_AUDIO)}
                val galleryState = async { controller?.getPermissionState(Permission.GALLERY)}

                _state.update {
                    it.copy(
                        cameraPermissionState = cameraState.await()!!,
                        microphonePermissionState = microphoneState.await()!!,
                        galleryPermissionState = galleryState.await()!!,
                        renderUi = true
                    )
                }
            }


        }
    }

    fun openAppSettings(){
        controller?.openAppSettings()
    }

    suspend fun isGalleryPermissionGranted():Boolean{
        if (controller == null) return false
        val permissionState = controller?.getPermissionState(Permission.GALLERY)
        _state.update {
            it.copy(
                galleryPermissionState = permissionState!!
            )
        }
        return  permissionState == PermissionState.Granted
    }

    fun provideOrRequestGalleryPermission(){
        if (state.value.galleryPermissionState == PermissionState.DeniedAlways) {
            updateRationaleState(true)
            return
        }
        if (state.value.galleryPermissionState != PermissionState.NotDetermined){
            _state.update {
                it.copy(galleryPermissionState = PermissionState.NotDetermined)
            }
        }
        screenModelScope.launch {
            _state.update {
                it.copy(
                    galleryPermissionState = try {
                        controller?.providePermission(Permission.GALLERY)
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

    fun provideOrRequestCameraPermission(){
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

    fun resetCaptureAndPickedContent() {
        onCapturedImage(null)
        onVideoRecordCompleted(null)
        updateSectionImageIndex(-1)
        updateSectionVideoIndex(-1)
        updateVideoFrames(emptyList())
        updatePlayingState(false)
    }

    fun updatePlayingState(value: Boolean){
        _state.update {
            it.copy(
                isVideoPlaying = value
            )
        }
    }


}