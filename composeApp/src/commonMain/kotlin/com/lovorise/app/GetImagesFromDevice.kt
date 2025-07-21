package com.lovorise.app

import coil3.PlatformContext
import com.lovorise.app.reels.presentation.states.CaptureRecordScreenState
import kotlinx.coroutines.flow.Flow

expect fun getImagesFromDevice(context:PlatformContext,startIndex:Int = 0): Flow<List<Any>>

expect suspend fun getVideosThumbnailsFromDevice(context:PlatformContext,startIndex:Int = 0): Flow<List<CaptureRecordScreenState.VideoData>>

expect suspend fun getVideoById(id:String) : String?

expect suspend fun getImageById(id: String,context: PlatformContext) : String?

expect suspend fun getPathForUiImage(data:Any) : String?

expect fun getFileSizeFromUri(uri: String,context: PlatformContext): Long?

expect suspend fun getVideoFrames(context: PlatformContext,path:String) : VideoFrames

expect suspend fun getImagesAndVideos(context: PlatformContext,pageNumber: Int=1,pageSize: Int=20) : List<MediaItem>
expect suspend fun getImages(context: PlatformContext,pageNumber: Int=1,pageSize: Int=20) : List<MediaItem>
expect suspend fun getVideos(context: PlatformContext,pageNumber: Int=1,pageSize: Int=20) : List<MediaItem>


data class VideoFrames(
    val frames:List<Any>,
    val duration:Long
)


data class MediaItem(
    val id:String,
    val type:Type,
    val videoUri:String?,
    val image:Any?,
    val thumbnail:Any?,
    val selectionIndex:Int = 0
){
    enum class Type{
        IMAGE,VIDEO
    }
}


