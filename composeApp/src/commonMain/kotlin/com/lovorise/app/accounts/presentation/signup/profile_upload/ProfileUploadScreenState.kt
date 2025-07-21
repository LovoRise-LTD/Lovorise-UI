package com.lovorise.app.accounts.presentation.signup.profile_upload

import androidx.compose.ui.graphics.ImageBitmap
import com.lovorise.app.MediaItem
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.Photo
import com.lovorise.app.libs.permissions.PermissionState

data class ProfileUploadScreenState(
    val images:List<Photo> = List(6){Photo(id=it,orderNum = it+1)},
    val imageSourceOptionDialog: Boolean =  false,
    val launchCamera: Boolean =  false,
    val launchGallery: Boolean =  false,
    val launchSetting: Boolean =  false,
    val permissionRationalDialog: Boolean =  false,
    val isButtonEnabled: Boolean = false,
    val galleryImages: List<GalleryImageVideo> = emptyList(),
    val selectedGalleryImages: List<GalleryImageVideo> = emptyList(),
    val selectedGalleryImage:GalleryImageVideo = GalleryImageVideo(data = null, selectionIndex = 0),
    val clickedIndex:Int = -1,
    val progress:Float = 1f,
    val cameraPermissionState: PermissionState = PermissionState.NotDetermined,
    val galleryPermissionState: PermissionState = PermissionState.NotDetermined,
    val isAllGalleryImagesLoaded:Boolean = false,
    val lastRequestedAtIndex:Int = -1,
    val aspectRatio:Float = 4f/5f,
    val isLoading:Boolean = false,
    val hasImageReordered:Boolean = false,
    val isProfileUpdated:Boolean = false,
    val isPlaying:Boolean = false,
    val isPageStateUpdated:Boolean = false,
    val selectedItems:List<MediaItem> = emptyList(),
    val currentMediaItem:MediaItem? = null,
    val loadedVideoUris:List<VideoUri> = emptyList(),
    val pickedMediaItems: List<PickedMediaItem> = emptyList(),
    val showPauseButton:Boolean = false,
    val capturedImage:String? = null
){

    data class PickedMediaItem(
        val id:String,
        val type: MediaItem.Type,
        val path: String,
        val imageInfo:ImageInfo? = null,
        val videoInfo:VideoInfo = VideoInfo()
    ){
        data class ImageInfo(
            val bitmap:ImageBitmap?
        )

        data class VideoInfo(
            val currentPosition:Long = 0,
            val videoFrames:List<Any> = emptyList(),
            val fileSize:Long = 0,
            val totalDuration:Long = 1,
            val clipRange: ClosedFloatingPointRange<Float> = 0f..100f
        )
    }

    data class VideoUri(
        val id:String,
        val uri:String
    )


    data class GalleryImageVideo(
        val data:MediaItem?,
        val selectionIndex:Int,
    )

}



