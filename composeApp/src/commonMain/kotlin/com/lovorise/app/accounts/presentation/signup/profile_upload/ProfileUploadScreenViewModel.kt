package com.lovorise.app.accounts.presentation.signup.profile_upload

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import com.lovorise.app.MediaItem
import com.lovorise.app.accounts.domain.model.GetMediaResponse
import com.lovorise.app.accounts.domain.model.ReorderImageItem
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.ImageUploadData
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.Photo
import com.lovorise.app.getFileSizeFromUri
import com.lovorise.app.getImageById
import com.lovorise.app.getImagesAndVideos
import com.lovorise.app.getVideoById
import com.lovorise.app.getVideoFrames
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.cropper.saveBitmapToCache
import com.lovorise.app.libs.media_player.exportVideoSegment
import com.lovorise.app.libs.permissions.DeniedAlwaysException
import com.lovorise.app.libs.permissions.DeniedException
import com.lovorise.app.libs.permissions.Permission
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions.PermissionsController
import com.lovorise.app.libs.permissions.RequestCanceledException
import com.lovorise.app.photo_capture_pick.SharedImage
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.coroutines.resume

class ProfileUploadScreenViewModel (
    private val accountsViewModel: AccountsViewModel
): ScreenModel {

    private val _state = MutableStateFlow(ProfileUploadScreenState())
    val state = _state.asStateFlow()

    private var controller: PermissionsController? = null
    private var context: PlatformContext? = null


    val paginationState = PaginationState<Int, MediaItem>(
        initialPageKey = 1,
        onRequestPage = { loadPage(it) }
    )

    fun onInit(controller: PermissionsController?,context: PlatformContext){
       // if (this.controller != null) return
        if (controller != null) {
            this.controller = controller
        }
        this.context = context
    }

    fun retryFetching(){
        if (paginationState.allItems.isNullOrEmpty()){
            paginationState.refresh(1)
        }
    }

    fun deletePickedMediaItem(item: ProfileUploadScreenState.PickedMediaItem){
        _state.update {
            it.copy(
                pickedMediaItems = state.value.pickedMediaItems.toMutableList().apply {
                    remove(item)
                },
                selectedItems = state.value.selectedItems.toMutableList().apply {
                    state.value.selectedItems.firstOrNull {it1-> it1.id == item.id }?.let { m->
                        remove(m)
                    }
                }
            )
        }
    }

    private fun loadPage(page:Int){
        if (context == null) throw Exception("Without context page cannot be loaded")
        screenModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val items = getImagesAndVideos(context!!, pageNumber = page, pageSize = 20)
                    if (page == 1){
                        _state.update {
                            it.copy(
                                currentMediaItem = items.firstOrNull()
                            )
                        }
                    }
                    paginationState.appendPage(
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

    fun resetState(){
        _state.update{
            ProfileUploadScreenState()
        }
    }


    fun loadImages(images:List<GetMediaResponse.MediaData>?){
        if (images.isNullOrEmpty()) return
        val updatedImages = state.value.images.toMutableList()
        val imagesList = images.filter { it.url.isNotBlank() || it.localPath?.isNotBlank() == true }
        state.value.images.forEachIndexed { index, _ ->
            val image = imagesList.getOrNull(index)
            if (image != null) {
                updatedImages.apply {
                    add(
                        index,
                        removeAt(index).copy(
                            imgId = image.id,
                            image = image.url,
                            placeHolder = image.localPath ?: "",
                            orderNum = image.orderNum,
                            type = image.type,
                            thumbnailUrl = image.thumbnail
                        )
                    )
                }
            }
        }

        _state.update {
            it.copy(
                images = updatedImages
            )
        }
        println("updated the images $updatedImages")
    }


    fun updateButtonState(){
        _state.update {
            it.copy(
                isButtonEnabled = state.value.images.any { item -> item.image != null }
            )
        }
    }

    fun markPageStateUpdated(){
        _state.update {
            it.copy(
                isPageStateUpdated = true
            )
        }
    }


    fun toggleVideoPlayingState(value:Boolean){
        _state.update {
            it.copy(
                isPlaying = value
            )
        }
    }

    fun updateVideoInfoForCurrentMedia(index:Int,info:ProfileUploadScreenState.PickedMediaItem.VideoInfo){
        _state.update {
            it.copy(
                pickedMediaItems = state.value.pickedMediaItems.toMutableList().apply {
                    add(index,removeAt(index).copy(videoInfo = info))
                }
            )
        }
    }

    fun reloadGalleryImages(){

        _state.update {
            it.copy(
                selectedItems = emptyList(),
                currentMediaItem = paginationState.allItems?.firstOrNull()
            )
        }
    }


    fun getAllowedSectionLength():Int{
        return getListOfIndexesToBeChanged().size
    }

    fun loadVideoFrames(index:Int,context: PlatformContext){
        val mediaItems = state.value.pickedMediaItems.toMutableList()
        val mediaItem = mediaItems.getOrNull(index) ?: return
        if (mediaItem.type != MediaItem.Type.VIDEO || (mediaItem.videoInfo.videoFrames.isNotEmpty() && mediaItem.videoInfo.fileSize > 0)) return
        screenModelScope.launch {
            val videoMetaData = getVideoFrames(context,mediaItem.path)
            val fileSize = getFileSizeFromUri(mediaItem.path,context)
            val videoInfo = mediaItem.videoInfo.copy(videoFrames = videoMetaData.frames, totalDuration = videoMetaData.duration, fileSize = fileSize ?: 0)
            _state.update {
                it.copy(
                    pickedMediaItems = mediaItems.apply {
                        add(index,removeAt(index).copy(videoInfo = videoInfo))
                    }
                )
            }
        }
    }

    fun onImageCropped(index: Int,bitmap: ImageBitmap,context: PlatformContext){
        screenModelScope.launch {
            val mediaItems = state.value.pickedMediaItems.toMutableList()
            val mediaItem = mediaItems.getOrNull(index) ?: return@launch

            val path = saveBitmapToCache(bitmap,context)

            val imageInfo = mediaItem.imageInfo?.copy(bitmap = bitmap) ?: ProfileUploadScreenState.PickedMediaItem.ImageInfo(bitmap)
            _state.update {
                it.copy(
                    pickedMediaItems = mediaItems.apply {
                        add(index,removeAt(index).copy(imageInfo = imageInfo, path = path))
                    }
                )
            }
        }
    }

    fun updatePauseButtonState(value: Boolean){
        _state.update {
            it.copy(
                showPauseButton = value,
            )
        }
    }

    fun processMediaItems(performNavigation: () -> Unit, context: PlatformContext) {
        if (state.value.selectedItems.isEmpty()) return
        screenModelScope.launch {
            withContext(Dispatchers.IO) {
                _state.update {
                    it.copy(isLoading = true)
                }
                val items = state.value.selectedItems.sortedBy { it.selectionIndex }.map { media ->
                    val existingMedia =
                        state.value.pickedMediaItems.firstOrNull { it.id == media.id }
                    val path = if (media.type == MediaItem.Type.IMAGE) {

                        val image = existingMedia?.path
                        image ?: getImageById(media.id, context)
                    } else {
                        if (isAndroid()) {
                            media.videoUri
                        } else {
                            var uri =
                                state.value.loadedVideoUris.firstOrNull { it.id == media.id }?.uri
                            if (uri == null) {
                                uri = getVideoById(media.id)
                                println("the uri is $uri")

                            }
                            println("the uri is $uri")
                            uri
                        }
                    }

                    if (path != null) {
                        ProfileUploadScreenState.PickedMediaItem(
                            type = media.type,
                            path = path,
                            id = media.id,
                            videoInfo = existingMedia?.videoInfo?.copy(
                                currentPosition = 0,
                                clipRange = 0f..100f
                            ) ?: ProfileUploadScreenState.PickedMediaItem.VideoInfo()
                        )
                    } else {
                        null
                    }
                    //   }
                }
                _state.update {
                    it.copy(
                        pickedMediaItems = items.filterNotNull(),
                        isLoading = false
                    )
                }

                if (items.isNotEmpty()) {
                    performNavigation()
                }
            }
        }
    }

    fun onGallerySelectionItemClick(item:MediaItem){
        screenModelScope.launch {
            val list = state.value.selectedItems.toMutableList()
            val currentSelection = list.size
            val existingItem = state.value.selectedItems.firstOrNull { it.id == item.id }

            val isSelected = existingItem != null

            if (!isSelected && currentSelection == getAllowedSectionLength()) return@launch

            val updatedSelectionLength = if (isSelected) currentSelection - 1 else currentSelection + 1

            if (existingItem != null) {
                list.apply {
                    remove(existingItem)
                }
            } else {
                list.apply {
                    add(item.copy(selectionIndex = updatedSelectionLength))
                }
            }

            val currentMediaItem = if(!isSelected) list.firstOrNull { it.id == item.id } else { (list.maxByOrNull { it.selectionIndex }
                ?: paginationState.allItems?.firstOrNull())}

            val videoUri = if (currentMediaItem?.type == MediaItem.Type.VIDEO) {
                if (isAndroid()) currentMediaItem.videoUri else {
                    state.value.loadedVideoUris.firstOrNull { it.id == currentMediaItem.id }?.uri
                        ?: withContext(Dispatchers.IO) { getVideoById(currentMediaItem.id) }
                }
            } else null

            _state.update {
                it.copy(
                    selectedItems = list,
                    currentMediaItem = currentMediaItem?.copy(videoUri = videoUri),
                    isPlaying = false
                )
            }
        }
    }


    private fun updateProgress(progress:Float){
        _state.update {
            it.copy(
                progress = progress
            )
        }
    }

    private fun provideOrRequestGalleryPermission(){
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

    private fun provideOrRequestCameraPermission(){
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


    fun updateClickedIndex(index:Int){
        _state.update {
            it.copy(
                clickedIndex = index
            )
        }

    }



    fun updateCapturedImage(image: SharedImage,context: PlatformContext,onComplete: () -> Unit){
        updateLoadingState(true)
        screenModelScope.launch {
            updateProgress(.2f)
            delay(100L)
            updateProgress(.4f)
            val path = image.saveToCache(context)
            updateProgress(.6f)
            updateProgress(.8f)


            if (!path.isNullOrBlank()){
                val item = ProfileUploadScreenState.PickedMediaItem(
                    type = MediaItem.Type.IMAGE,
                    path = path,
                    id = path,
                    imageInfo = null
                )
                _state.update {
                    it.copy(
                        pickedMediaItems = listOf(item)
                    )
                }
                onComplete()
                updateProgress(1f)
            }

            updateLoadingState(false)
        }
    }



    fun updateLoadingState(value: Boolean){
        _state.update {
            it.copy(
                isLoading = value
            )
        }
    }



    fun uploadMedias(context: PlatformContext,onComplete: () -> Unit){
        screenModelScope.launch {
            val uploadDatas = mutableListOf<ImageUploadData>()

            val indexesList = getListOfIndexesToBeChanged().take(state.value.pickedMediaItems.size)



            val mergedList = state.value.pickedMediaItems.zip(indexesList)
            mergedList.forEachIndexed {i,mergedData ->
                val data = mergedData.first
                val orderNum = mergedData.second + 1

                if (data.type == MediaItem.Type.VIDEO) {
                    val videoPath = exportVideoSegmentSuspend(context, url = data.path, clipRange = data.videoInfo.clipRange)
                    println("the exportedVideoPath = $videoPath")
                    if (!videoPath.isNullOrBlank()) {
                        uploadDatas.add(
                            ImageUploadData(
                                fileName = "${
                                    Clock.System.now().toEpochMilliseconds()
                                }_profile_video_$orderNum.mp4",
                                type = SignedUrlMediaItem.Type.VIDEO,
                                path = videoPath,
                                orderNum = orderNum
                            )
                        )

                    }
                }
                else if (data.type == MediaItem.Type.IMAGE){
                    val path = data.imageInfo?.bitmap?.let { saveBitmapToCache(it,context) } ?: data.path
                    println("the image path is $path")
                    uploadDatas.add(
                        ImageUploadData(
                            //byteArray = byteArray,
                            fileName = "${
                                Clock.System.now().toEpochMilliseconds()
                            }_profile_image_$orderNum.png",
                            type = SignedUrlMediaItem.Type.IMAGE,
                            path = path,
                            orderNum = orderNum
                        )
                    )

                }
                if (mergedList.lastIndex == i){
                    println("the uploadData is ${uploadDatas.size} ${uploadDatas.map { it.orderNum }} ${accountsViewModel.state.value.user?.medias}")
                    accountsViewModel.uploadMedias(uploadDatas,context,uploadDatas.size, onComplete = onComplete)
                }
            }

        }


    }

    private suspend fun exportVideoSegmentSuspend(
        context: PlatformContext,
        url: String,
        clipRange: ClosedFloatingPointRange<Float>
    ): String? {
        return suspendCancellableCoroutine { continuation ->
            exportVideoSegment(context, url, clipRange) { success, path ->
                if (success && path != null) {
                    continuation.resume(path)
                } else {
                    continuation.resume(null)
                }
            }
        }
    }



    fun deleteItem(context: PlatformContext){
        val newList = _state.value.images.toMutableList()
        val imageId = state.value.images.getOrNull(state.value.clickedIndex)?.imgId
        println("the imageId is $imageId")
        val index = state.value.clickedIndex
        if (index == -1) return
        newList.apply {
            add(index,removeAt(index).copy(image = null, imgId = null))
            sortWith(compareBy<Photo> { it.image == null }.thenBy { it.timestamp })
        }
        if (imageId != null) {
            _state.update {
                it.copy(
                    hasImageReordered = true
                )
            }
            screenModelScope.launch {
                val user = accountsViewModel.state.value.user
                val medias = user?.medias?.toMutableList()?.apply {
                    val itemIndex = user.medias.indexOfFirst { it.id == imageId }
                    println("the imageId is $imageId item: $itemIndex")
                    if (itemIndex != -1) {
//                        add(itemIndex,removeAt(itemIndex).copy(id = "", url = ""))
                         add(itemIndex,removeAt(itemIndex).copy(id = "", url = "", localPath = null, thumbnail = null))
                    }
                }
                user?.copy(medias = medias)?.let { accountsViewModel.updateUserRes(it) }
                accountsViewModel.deleteImage(
                    imageId = imageId,
                    context
                ){
                    reorderImages(context, deletedImg = imageId, refreshUser = true)
                }
            }
        }
        _state.update {
            it.copy(
                images = newList,
                clickedIndex = -1
            )
        }

    }

    private fun getListOfIndexesToBeChanged():List<Int>{

        val changeAt = state.value.clickedIndex

        val result = mutableListOf<Int>()

        if (changeAt != -1){
            result.add(changeAt)
        }

        state.value.images.forEachIndexed { index, _ ->
            if (state.value.images[index].image == null){
                result.add(index)
            }
        }

        return result.toSet().toList()
    }

    fun movePhoto(from:Int,to:Int){
        val newList = _state.value.images.toMutableList()
        newList.apply {
            add(to,removeAt(from).copy(canEdit = to == 0))
        }
        _state.update {
            it.copy(
                images = newList,
                hasImageReordered = true
            )
        }
    }

    fun reorderImages(context: PlatformContext,refreshUser:Boolean = false,updateOnlyLocally:Boolean=false,deletedImg:String = ""){
        if (!state.value.hasImageReordered) return
        val filteredImages = state.value.images.filter { it.imgId != null && it.imgId != deletedImg}.mapIndexed { index, photo -> ReorderImageItem(mediaId = photo.imgId!!, orderNum = index + 1) }
       // println("the deleted img is ${deletedImg}")
        println("the filtered images == $filteredImages ")
        if (filteredImages.isNotEmpty()) {
            screenModelScope.launch {
                withContext(Dispatchers.IO) {
                    accountsViewModel.reorderImages(context,filteredImages,refreshUser,updateOnlyLocally)
                }
            }
        }
        _state.update {
            it.copy(
                hasImageReordered = false
            )
        }
    }

    fun onToggleImageSourceOptionDialog(){
        _state.update {
            it.copy(
                imageSourceOptionDialog = !_state.value.imageSourceOptionDialog
            )
        }
    }

    fun onToggleCamera(){
        _state.update {
            it.copy(
                launchCamera = !_state.value.launchCamera
            )
        }
    }

    fun onToggleGallery(){
        _state.update {
            it.copy(
                launchGallery = !_state.value.launchGallery
            )
        }
    }

    fun resetPickedMedias(){
        _state.update {
            it.copy(
                selectedItems = emptyList(),
                pickedMediaItems = emptyList(),
                clickedIndex = -1
            )
        }
    }

    fun resetSelectedMedias(){
        _state.update {
            it.copy(
                selectedItems = emptyList(),
            )
        }
    }


    fun openSettings(){
        controller?.openAppSettings()
    }

    fun onTogglePermissionRationale(){
        _state.update {
            it.copy(
                permissionRationalDialog = !_state.value.permissionRationalDialog
            )
        }
    }

    fun launchGallery(navigate:()->Unit){
        screenModelScope.launch {
            if (controller?.isPermissionGranted(Permission.GALLERY) == true){
                navigate()
            }else{
                onToggleGallery()
                provideOrRequestGalleryPermission()
            }
        }
    }

    fun launchCamera(navigate:()->Unit){
        screenModelScope.launch {
            if (controller?.isPermissionGranted(Permission.CAMERA) == true){
                navigate()
            }else{
                onToggleCamera()
                provideOrRequestCameraPermission()
            }
        }
    }




}