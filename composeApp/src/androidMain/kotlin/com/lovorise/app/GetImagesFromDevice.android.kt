package com.lovorise.app

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.PlatformContext
import com.lovorise.app.reels.presentation.states.CaptureRecordScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File


actual suspend fun getImagesAndVideos(
    context: PlatformContext,
    pageNumber: Int,
    pageSize: Int
): List<MediaItem> = withContext(Dispatchers.IO) {
    val medias = mutableListOf<MediaItem>()

    val uri = MediaStore.Files.getContentUri("external")

    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.DATE_ADDED
    )

    // Filter for images and videos only
    val selection =
        "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
    val selectionArgs = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    )

    val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

    val cursor = context.contentResolver.query(
        uri,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
        val mediaTypeColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

        // Calculate the starting point and limit for pagination
        val startIndex = (pageNumber - 1) * pageSize
        val endIndex = startIndex + pageSize

        var currentIndex = 0

        while (it.moveToNext() && currentIndex < endIndex) {
            // Skip items until the starting index is reached
            if (currentIndex++ < startIndex) continue

            val id = it.getLong(idColumn)
            val mediaType = it.getInt(mediaTypeColumn)

            val contentUri = when (mediaType) {
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                else -> null
            }

            contentUri?.let {
                val newUri = ContentUris.withAppendedId(contentUri, id)

                val type = when (mediaType) {
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaItem.Type.IMAGE
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaItem.Type.VIDEO
                    else -> null
                }

                val isVideo = type == MediaItem.Type.VIDEO
                val thumbnail = if (isVideo) createVideoThumbnail(context, newUri) else null

                type?.let {
                    medias.add(
                        MediaItem(
                            videoUri = if (isVideo) newUri.toString() else null,
                            type = type,
                            image = if (!isVideo) newUri.toString() else null,
                            thumbnail = thumbnail,
                            id = newUri.toString(),
                        )
                    )
                }
            }
        }
    }

    medias
}


actual suspend fun getImages(context: PlatformContext,pageNumber: Int,pageSize: Int) : List<MediaItem> = withContext(Dispatchers.IO) {
    val medias = mutableListOf<MediaItem>()

    val uri = MediaStore.Files.getContentUri("external")

    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.DATE_ADDED
    )

    // Filter for images and videos only
    val selection =
        "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
    val selectionArgs = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
    )

    val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

    val cursor = context.contentResolver.query(
        uri,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
        val mediaTypeColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

        // Calculate the starting point and limit for pagination
        val startIndex = (pageNumber - 1) * pageSize
        val endIndex = startIndex + pageSize

        var currentIndex = 0

        while (it.moveToNext() && currentIndex < endIndex) {
            // Skip items until the starting index is reached
            if (currentIndex++ < startIndex) continue

            val id = it.getLong(idColumn)
            val mediaType = it.getInt(mediaTypeColumn)

            val contentUri = when (mediaType) {
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                else -> null
            }

            contentUri?.let {
                val newUri = ContentUris.withAppendedId(contentUri, id)

                val type = when (mediaType) {
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaItem.Type.IMAGE
//                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaItem.Type.VIDEO
                    else -> null
                }

//                val isVideo = type == MediaItem.Type.VIDEO
//                val thumbnail = if (isVideo) createVideoThumbnail(context, newUri) else null

                type?.let {
                    medias.add(
                        MediaItem(
                            videoUri = null,
                            type = type,
                            image =  newUri.toString(),
                            thumbnail = null,
                            id = newUri.toString(),
                        )
                    )
                }
            }
        }
    }

    medias
}
actual suspend fun getVideos(context: PlatformContext,pageNumber: Int,pageSize: Int) : List<MediaItem> = withContext(Dispatchers.IO) {
    val medias = mutableListOf<MediaItem>()

    val uri = MediaStore.Files.getContentUri("external")

    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.DATE_ADDED
    )

    // Filter for images and videos only
    val selection =
        "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
    val selectionArgs = arrayOf(
//        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    )

    val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

    val cursor = context.contentResolver.query(
        uri,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
        val mediaTypeColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

        // Calculate the starting point and limit for pagination
        val startIndex = (pageNumber - 1) * pageSize
        val endIndex = startIndex + pageSize

        var currentIndex = 0

        while (it.moveToNext() && currentIndex < endIndex) {
            // Skip items until the starting index is reached
            if (currentIndex++ < startIndex) continue

            val id = it.getLong(idColumn)
            val mediaType = it.getInt(mediaTypeColumn)

            val contentUri = when (mediaType) {
//                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                else -> null
            }

            contentUri?.let {
                val newUri = ContentUris.withAppendedId(contentUri, id)

                val type = when (mediaType) {
//                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaItem.Type.IMAGE
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaItem.Type.VIDEO
                    else -> null
                }

//                val isVideo = type == MediaItem.Type.VIDEO
                val thumbnail = createVideoThumbnail(context, newUri)

                type?.let {
                    medias.add(
                        MediaItem(
                            videoUri = newUri.toString(),
                            type = type,
                            image = null,
                            thumbnail = thumbnail,
                            id = newUri.toString(),
                        )
                    )
                }
            }
        }
    }

    medias
}




actual fun getImagesFromDevice(context: PlatformContext,startIndex:Int): Flow<List<Any>> {

    val images = mutableListOf<String>()
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"


    val cursor = context.contentResolver.query(
        uri, projection, null, null, sortOrder
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val imageUri = ContentUris.withAppendedId(uri, id)
            images.add(imageUri.toString())
        }
    }
    return flow { emit(images)}
}

actual suspend fun getVideoById(id:String) : String?{
    return null
}

actual suspend fun getPathForUiImage(data:Any) : String?{
    return null
}

actual fun getFileSizeFromUri(uri: String,context: PlatformContext): Long? {
    val contentUri = Uri.parse(uri)
    val contentResolver = context.contentResolver
    var size: Long? = null

    val cursor = contentResolver.query(contentUri, null, null, null, null)

    cursor?.use {
        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
        if (it.moveToFirst() && sizeIndex != -1) {
            size = it.getLong(sizeIndex) // Get the file size in bytes
        }
    }
    return size
}

actual suspend fun getImageById(id: String,context: PlatformContext) : String? = withContext(Dispatchers.IO){
    // Get the cache directory
    val cacheDir = context.cacheDir

    // Create a file in the cache directory
    val file = File(cacheDir, "CroppedImage_${System.currentTimeMillis()}.png")
    context.contentResolver.openInputStream(Uri.parse(id))?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    file.absolutePath
}



actual suspend fun getVideosThumbnailsFromDevice(context:PlatformContext,startIndex:Int): Flow<List<CaptureRecordScreenState.VideoData>>{
    return callbackFlow {
        val projection = arrayOf(MediaStore.Video.Media._ID)
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"


        val cursor = context.contentResolver.query(
            uri, projection, null, null, sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val videoUri = ContentUris.withAppendedId(uri, id)
                val thumbnail = createVideoThumbnail(context, videoUri)
                if (thumbnail != null) {
                    send(
                        listOf(
                            CaptureRecordScreenState.VideoData(
                                id.toString(),
                                videoUri = videoUri.toString(),
                                thumbnail = thumbnail.asImageBitmap(),
                                uiImage = null
                            )
                        )
                    )
                }
            }

        }

        awaitClose()
    }
}

private suspend fun createVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        try {
            mediaMetadataRetriever.setDataSource(context, uri)
            mediaMetadataRetriever.frameAtTime
        } catch (ex: Exception) {
            null
        } finally {
            mediaMetadataRetriever.release()
        }

    }

}

actual suspend fun getVideoFrames(context: PlatformContext,path:String) : VideoFrames{
    return withContext(Dispatchers.IO) {
        val mediaMetadataRetriever = MediaMetadataRetriever()

        mediaMetadataRetriever.setDataSource(context, Uri.parse(path))
        val totalDuration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: return@withContext VideoFrames(emptyList(),1L)
        val interval = totalDuration / 9
        val framesTime = (0 until 9).map { it * interval * 1000}
        val result = mutableListOf<ImageBitmap>()

        println("the framesTime is $framesTime")

        framesTime.forEach {
            val data = mediaMetadataRetriever.getFrameAtTime(it)
            if (data != null) {
                result.add(data.asImageBitmap())
            }
        }
        if (result.size == framesTime.size) {
            mediaMetadataRetriever.release()
        }
        VideoFrames(frames = result, duration = totalDuration)
    }
}



private  fun getVideoThumbnailFromMediaMetadataRetriever(context: PlatformContext, uri: Uri): Bitmap? {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(context, uri)
    val thumbnailBytes = mediaMetadataRetriever.embeddedPicture
//    val resizer = Resizer(size, null)
    // use a built-in thumbnail if the media file has it
    if (Build.VERSION.SDK_INT >= 31) {
        ImageDecoder.createSource(context.contentResolver, uri)
        thumbnailBytes?.let {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(it));
        }
        return null
    }else{
        return null
    }

}