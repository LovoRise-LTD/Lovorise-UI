package com.lovorise.app

import coil3.PlatformContext
import com.lovorise.app.reels.presentation.states.CaptureRecordScreenState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import platform.AVFoundation.AVAssetImageGenerator
import platform.AVFoundation.AVAssetImageGeneratorApertureModeEncodedPixels
import platform.AVFoundation.AVURLAsset
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSPredicate
import platform.Foundation.NSSortDescriptor
import platform.Foundation.NSURL
import platform.Foundation.NSURLFileSizeKey
import platform.Foundation.temporaryDirectory
import platform.Foundation.writeToURL
import platform.Photos.PHAsset
import platform.Photos.PHAssetMediaTypeImage
import platform.Photos.PHAssetMediaTypeVideo
import platform.Photos.PHFetchOptions
import platform.Photos.PHImageManager
import platform.Photos.PHImageRequestOptions
import platform.Photos.PHImageRequestOptionsDeliveryModeHighQualityFormat
import platform.Photos.PHImageRequestOptionsResizeModeFast
import platform.Photos.PHVideoRequestOptions
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageOrientation
import platform.UIKit.UIImagePNGRepresentation
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resumeWithException


actual suspend fun getImagesAndVideos(context: PlatformContext, pageNumber: Int, pageSize: Int): List<MediaItem> = withContext(Dispatchers.IO) {
    val fetchOptions = PHFetchOptions().apply {
        sortDescriptors = listOf(NSSortDescriptor(key = "creationDate", ascending = false))
    }

    val fetchResult = PHAsset.fetchAssetsWithOptions(fetchOptions)
    val totalCount = fetchResult.count.toInt()

    println("Total media items: $totalCount")

    val startIndex = (pageNumber - 1) * pageSize
    val endIndex = (startIndex + pageSize).coerceAtMost(totalCount)

    if (startIndex >= totalCount) {
        // No more items to fetch
        println("No items to fetch for page $pageNumber")
        return@withContext emptyList<MediaItem>()
    }

    val mediaList = mutableListOf<MediaItem>()
    for (i in startIndex until endIndex) {
        val asset = fetchResult.objectAtIndex(i.toULong()) as PHAsset
        val type = when (asset.mediaType) {
            PHAssetMediaTypeImage -> MediaItem.Type.IMAGE
            PHAssetMediaTypeVideo -> MediaItem.Type.VIDEO
            else -> null
        }

        if (type != null) {
            println("Processing index $i")

            mediaList.add(
                MediaItem(
                    videoUri = null,
                    type = type,
                    image = null,
                    thumbnail = null,
                    id = asset.localIdentifier
                )
            )
        }
    }

    mediaList
}

actual suspend fun getImages(context: PlatformContext, pageNumber: Int, pageSize: Int): List<MediaItem> = withContext(Dispatchers.IO) {
    val fetchOptions = PHFetchOptions().apply {
        sortDescriptors = listOf(NSSortDescriptor(key = "creationDate", ascending = false))
    }

    val fetchResult = PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, fetchOptions)
    val totalCount = fetchResult.count.toInt()

    println("Total media items: $totalCount")

    val startIndex = (pageNumber - 1) * pageSize
    val endIndex = (startIndex + pageSize).coerceAtMost(totalCount)

    if (startIndex >= totalCount) {
        // No more items to fetch
        println("No items to fetch for page $pageNumber")
        return@withContext emptyList<MediaItem>()
    }

    val mediaList = mutableListOf<MediaItem>()
    for (i in startIndex until endIndex) {
        val asset = fetchResult.objectAtIndex(i.toULong()) as PHAsset
        val type = when (asset.mediaType) {
            PHAssetMediaTypeImage -> MediaItem.Type.IMAGE
          //  PHAssetMediaTypeVideo -> MediaItem.Type.VIDEO
            else -> null
        }

        if (type != null) {
            println("Processing index $i")

            mediaList.add(
                MediaItem(
                    videoUri = null,
                    type = type,
                    image = null,
                    thumbnail = null,
                    id = asset.localIdentifier
                )
            )
        }
    }

    mediaList
}

actual suspend fun getVideos(context: PlatformContext, pageNumber: Int, pageSize: Int): List<MediaItem> = withContext(Dispatchers.IO) {
    val fetchOptions = PHFetchOptions().apply {
        sortDescriptors = listOf(NSSortDescriptor(key = "creationDate", ascending = false))
    }

    //val fetchResult = PHAsset.fetchAssetsWithOptions(fetchOptions)
    val fetchResult = PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeVideo, fetchOptions)

    val totalCount = fetchResult.count.toInt()

    println("Total media items: $totalCount")

    val startIndex = (pageNumber - 1) * pageSize
    val endIndex = (startIndex + pageSize).coerceAtMost(totalCount)

    if (startIndex >= totalCount) {
        // No more items to fetch
        println("No items to fetch for page $pageNumber")
        return@withContext emptyList<MediaItem>()
    }

    val mediaList = mutableListOf<MediaItem>()
    for (i in startIndex until endIndex) {
        val asset = fetchResult.objectAtIndex(i.toULong()) as PHAsset
        val type = when (asset.mediaType) {
           // PHAssetMediaTypeImage -> MediaItem.Type.IMAGE
            PHAssetMediaTypeVideo -> MediaItem.Type.VIDEO
            else -> null
        }

        if (type != null) {
            println("Processing index $i")

            mediaList.add(
                MediaItem(
                    videoUri = null,
                    type = type,
                    image = null,
                    thumbnail = null,
                    id = asset.localIdentifier
                )
            )
        }
    }

    mediaList
}


private fun createStepList(number: Int): List<Int> {
    val result = mutableListOf<Int>()
    var remaining = number

    while (remaining > 0) {
        if (remaining > 10) {
            result.add(10)
            remaining -= 10
        } else {
            result.add(remaining)
            break
        }
    }
    return result
}

suspend fun getVideoDataById(videoId: String): Result<String?> = suspendCancellableCoroutine { continuation ->
    val fetchResult = PHAsset.fetchAssetsWithLocalIdentifiers(listOf(videoId), null)
    if (fetchResult.count == 0uL) {
        continuation.resumeWithException(Exception("Asset with videoId $videoId not found"))
        return@suspendCancellableCoroutine
    }

    val asset = fetchResult.objectAtIndex(0u) as PHAsset

    val options = PHVideoRequestOptions().apply {
        isNetworkAccessAllowed() // Allow downloading if stored in iCloud
    }

    PHImageManager.defaultManager().requestAVAssetForVideo(asset, options) { avAsset, _, error ->
        if (avAsset == null) {
            continuation.resumeWithException(Exception(error.toString()))
            return@requestAVAssetForVideo
        }

        val urlAsset = avAsset as? AVURLAsset
        if (urlAsset != null) {
            continuation.resume(
                Result.success(urlAsset.URL.path),
                onCancellation = null
            )
        } else {
            continuation.resumeWithException(Exception("Failed to retrieve AVURLAsset"))
        }
    }
}

actual suspend fun getVideoById(id:String) : String?{
    return getVideoDataById(id).fold(
        onSuccess = { it },
        onFailure = {null}
    )
}


actual suspend fun getVideosThumbnailsFromDevice(
    context: PlatformContext,
    startIndex: Int
): Flow<List<CaptureRecordScreenState.VideoData>> = flow {
    println("entered getVideosThumbnailsFromDevice")
    val fetchOptions = PHFetchOptions().apply {
        predicate = NSPredicate.predicateWithFormat("mediaType == %d", PHAssetMediaTypeVideo)
        sortDescriptors = listOf(NSSortDescriptor(key = "creationDate", ascending = false))
    }

    val assets = PHAsset.fetchAssetsWithOptions(fetchOptions)


    for (i in startIndex until assets.count.toInt()) {
        val asset = assets.objectAtIndex(i.toULong()) as PHAsset
        val videoId = asset.localIdentifier
        println("the videoId is $videoId")
        val thumbnail = getVideoThumbnail(asset)
        if (thumbnail != null) {
            println("the videoId is $videoId thumbnail is ready")
            val videoData = CaptureRecordScreenState.VideoData(
                id = videoId,
                videoUri = "",  // Construct a URI-like identifier for PHAsset
                thumbnail = null,
                uiImage = thumbnail
            )
            println("the emitted video data $videoData")
            emit(listOf(videoData))

        }
    }
}

actual suspend fun getVideoFrames(context: PlatformContext, path: String): VideoFrames {
    return withContext(Dispatchers.Default) {
        val frames = mutableListOf<Any>()
        val totalFrames = 9 // Number of frames you want to capture
        val duration = getVideoDuration(path) // Function to fetch the video duration
        val interval = duration / totalFrames

        println("the duration is $duration , the interval is $interval")

        for (i in 0 until totalFrames) {
            val timeSeconds = i * interval
            imageFromVideo(url = path, timeSeconds = timeSeconds) { frame ->
                frame?.let { frames.add(it) }
            }
        }
        VideoFrames(frames,duration.toLong())
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun getFileSizeFromUri(uri: String,context: PlatformContext): Long? {
    val url = NSURL(string = uri)
    return try {
        // Using NSURL's resource values to fetch file size
        val fileSize = url.resourceValuesForKeys(listOf(NSURLFileSizeKey), error = null)?.get(NSURLFileSizeKey) as? Long
        fileSize
    } catch (e: Exception) {
        null
    }
}


@OptIn(ExperimentalForeignApi::class)
fun imageFromVideo(
    url: String,
    timeSeconds: Double,
    completion: (UIImage?) -> Unit
) {
    dispatch_async(dispatch_get_main_queue()) {
        val nsUrl = NSURL.fileURLWithPath(url)
       // val fileExists = NSFileManager.defaultManager.fileExistsAtPath(nsUrl.path!!)
//        if (!fileExists) {
//            throw IllegalArgumentException("File does not exist at the given path: ${nsUrl.path}")
//        }
        if (!NSFileManager.defaultManager.fileExistsAtPath(nsUrl.path!!)) {
            println("File does not exist at path: ${nsUrl.path}")
            completion(null)
            return@dispatch_async
        }
        val asset = AVURLAsset(nsUrl, options = null)
        val assetImageGenerator = AVAssetImageGenerator(asset)
        assetImageGenerator.appliesPreferredTrackTransform = true
        assetImageGenerator.apertureMode = AVAssetImageGeneratorApertureModeEncodedPixels

        val cmTime = CMTimeMakeWithSeconds(timeSeconds, preferredTimescale = 600)
        try {
            val cgImage = assetImageGenerator.copyCGImageAtTime(cmTime, actualTime = null, error = null)
                ?: throw IllegalArgumentException("Failed to generate image from video.")

            val uiImage = UIImage.imageWithCGImage(cgImage)
            completion(uiImage)
        } catch (e: Throwable) {
            println("Error: ${e.message}")
            completion(null)
        }
    }
}


@OptIn(ExperimentalForeignApi::class)
fun getVideoDuration(url: String): Double {
    val nsUrl = NSURL.fileURLWithPath(url)
    val asset = AVURLAsset(uRL = nsUrl, options = null)
    return CMTimeGetSeconds(asset.duration)
}

@OptIn(ExperimentalForeignApi::class)
private suspend fun getVideoThumbnail(asset: PHAsset): NSData? = withContext(Dispatchers.IO) {suspendCancellableCoroutine { continuation ->
    val imageManager = PHImageManager.defaultManager()
    val requestOptions = PHImageRequestOptions().apply {
        !isSynchronous()
        networkAccessAllowed = true
        resizeMode = PHImageRequestOptionsResizeModeFast
        deliveryMode = PHImageRequestOptionsDeliveryModeHighQualityFormat
    }


    imageManager.requestImageForAsset(
        asset = asset,
        targetSize = CGSizeMake(asset.pixelWidth.toDouble(),asset.pixelHeight.toDouble()),
        contentMode = PHImageRequestOptionsResizeModeFast,
        options = requestOptions
    ) { image, _ ->
        if (image != null) {
            continuation.resume(
                UIImagePNGRepresentation(image)
            ) {}
        } else {
            continuation.resume(null,{})
        }
    }
}}


actual fun getImagesFromDevice(context: PlatformContext, startIndex: Int): Flow<List<Any>> = flow {
    println("called getImagesFromDevice for index $startIndex")
    val images = mutableListOf<NSData>()
    val fetchOptions = PHFetchOptions().apply {
        sortDescriptors = listOf(NSSortDescriptor("creationDate", false))
    }

    val assets = PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, fetchOptions)

    val assetCount = assets.count.toInt()
    val batchSize = 4
    val endIndex = minOf(startIndex + batchSize, assetCount)

    println("the start index: $startIndex end: $endIndex total: $assetCount")

    if (startIndex >= assetCount) {
        emit(emptyList())
        return@flow
    }

    // Move the heavy lifting to a background thread
    for (index in startIndex..< endIndex) {
        withContext(Dispatchers.IO) {
            val asset = assets.objectAtIndex(index.toULong()) as? PHAsset
            if (asset != null) {
                val resultUrl = getLocalUrlForAssetStandardImages(asset, "")
                println("file processed for index $index")
                resultUrl?.let { emit(listOf(it)) }
            }
        }
    }

  //  emit(images) // Emit the batch of images after processing
}.flowOn(Dispatchers.IO)

suspend fun saveUiImageToPath(data:Any):String? = withContext(Dispatchers.IO){
    suspendCancellableCoroutine { continuation ->
      //  if (!data.instanceOf(NSData::class)) continuation.resume(null,onCancellation = null)
        val uiImage = UIImage(data = data as NSData).fixOrientation()
        if (uiImage == null) continuation.resume(null, onCancellation = null)
        if (uiImage != null) {
            val pngData = UIImagePNGRepresentation(uiImage)

            val fileManager = NSFileManager.defaultManager()
            val tempDirectory = fileManager.temporaryDirectory
            val tempFileURL = NSURL.fileURLWithPath(tempDirectory.path + "/" + "${Clock.System.now().toEpochMilliseconds()}_Cached_Image.png")

            val success = pngData?.writeToURL(tempFileURL, atomically = true) ?: false

            continuation.resume(if (success) tempFileURL.absoluteString else null,onCancellation = null)
        }
    }
}

actual suspend fun getPathForUiImage(data:Any) : String?{
    return saveUiImageToPath(data)
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getImageById(id: String,context: PlatformContext): String? = withContext(Dispatchers.IO) {
    suspendCancellableCoroutine { continuation ->
        val fetchResult = PHAsset.fetchAssetsWithLocalIdentifiers(listOf(id), null)
        if (fetchResult.count == 0uL) {
            continuation.resumeWithException(Exception("Asset with imageId $id not found"))
            return@suspendCancellableCoroutine
        }


        val asset = fetchResult.objectAtIndex(0u) as PHAsset

        val requestOptions = PHImageRequestOptions().apply {
            !isSynchronous()
            networkAccessAllowed = true
            resizeMode = PHImageRequestOptionsResizeModeFast
            deliveryMode = PHImageRequestOptionsDeliveryModeHighQualityFormat
        }

        val imageManager = PHImageManager.defaultManager()


        imageManager.requestImageForAsset(
            asset = asset,
            targetSize = CGSizeMake(asset.pixelWidth.toDouble(), asset.pixelHeight.toDouble()),
            contentMode = PHImageRequestOptionsResizeModeFast,
            options = requestOptions
        ) { image, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO){
                    if (image == null){
                        continuation.resume(null,onCancellation = null)
                    }else {
                        val pngData = UIImagePNGRepresentation(image)

                        val fileManager = NSFileManager.defaultManager()
                        val tempDirectory = fileManager.temporaryDirectory
                        val tempFileURL = NSURL.fileURLWithPath(
                            tempDirectory.path + "/" + "${
                                Clock.System.now().toEpochMilliseconds()
                            }_Cached_Image.png"
                        )

                        val success = pngData?.writeToURL(tempFileURL, atomically = true) ?: false

                        continuation.resume(
                            if (success) tempFileURL.absoluteString else null,
                            onCancellation = null
                        )
                    }
                }
            }
        }
    }
}



suspend fun getLocalUrlForAssetNonStandardImage(asset: PHAsset, fileName: String): NSData? =
    suspendCancellableCoroutine { continuation ->
        val imageManager = PHImageManager.defaultManager()
        val imageRequestOptions = PHImageRequestOptions().apply {
            networkAccessAllowed = true
            synchronous = false
        }
        imageManager.requestImageDataForAsset(asset, imageRequestOptions) { data, _, _, _ ->
            if (data != null) {
                continuation.resume(data,onCancellation = null)
            } else {
                println("Image data is null.")
                continuation.resume(null, onCancellation = null)
            }
        }
    }



suspend fun getLocalUrlForAssetStandardImages(asset: PHAsset, fileName: String): String? =
    suspendCancellableCoroutine { continuation ->
        val imageManager = PHImageManager.defaultManager()
        val imageRequestOptions = PHImageRequestOptions().apply {
            networkAccessAllowed = true
            synchronous = false
        }
        imageManager.requestImageDataForAsset(asset, imageRequestOptions) { data, _, _, _ ->
            if (data != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val savedPath = saveUiImageToPath(data)
                    continuation.resume(savedPath, onCancellation = null)
                }
            } else {
                println("Image data is null.")
                continuation.resume(null, onCancellation = null)
            }

        }
    }


@OptIn(ExperimentalForeignApi::class)
fun UIImage.fixOrientation(): UIImage? {
    if (this.imageOrientation == UIImageOrientation.UIImageOrientationUp) {
        return this
    }

    UIGraphicsBeginImageContextWithOptions(this.size, false, this.scale)
    this.drawInRect(CGRectMake(0.0, 0.0, this.size.useContents { width }, this.size.useContents { height }))
    val normalizedImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return normalizedImage
}




/*
@OptIn(ExperimentalForeignApi::class)
fun getImagesFromDevice(
    context: PlatformContext,
    pageSize: Int,
    currentPage: Int
): Flow<List<String>> {

    return callbackFlow {
        val images = mutableListOf<String>()
        val fetchOptions = PHFetchOptions()

        // Set the fetch limit to control the number of images fetched
        fetchOptions.fetchLimit = pageSize.toULong()

        // Fetch assets with the specified media type
        val assets = PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, fetchOptions)
        val imageManager = PHImageManager.defaultManager()
        val imageRequestOptions = PHImageRequestOptions()
        imageRequestOptions.networkAccessAllowed = true

        // Calculate the offset for paging
        val offset = currentPage * pageSize
        val totalAssets = assets.count.toInt()

        // Skip assets to get to the correct page
        if (offset < totalAssets) {
            val maxIndex = minOf(offset + pageSize, totalAssets)
            val jobs = mutableListOf<Deferred<Unit>>()

            for (i in offset until maxIndex) {
                val asset = assets.objectAtIndex(i.toULong())
                val job = CoroutineScope(Dispatchers.IO).async {
                    val id = imageManager.requestImageDataForAsset(
                        asset as PHAsset,
                        imageRequestOptions
                    ) { data, _, _, _ ->

                        if (data != null) {
                            val resources = PHAssetResource.assetResourcesForAsset(asset)
                            val resource = resources.firstOrNull() as? PHAssetResource
                            val fileName = resource?.originalFilename ?: "temp_image_$i.png"

                            val resultCallback: (String?) -> Unit = { path ->
                                if (path != null) {
                                    trySend(listOf(path))
                                    images.add(path)
                                }
                            }

                            if (fileName.lowercase().endsWith("png") || fileName.lowercase().endsWith("jpg") || fileName.lowercase().endsWith("jpeg")) {
                                getLocalUrlForAssetStandardImages(asset, resultCallback, fileName)
                            } else {
                                getLocalUrlForAssetNonStandardImage(asset, resultCallback, fileName)
                            }
                        }
                    }
                }
                jobs.add(job)
            }

            // Await all jobs to complete
            jobs.awaitAll()
        }

        awaitClose()
    }
}

private suspend fun requestPhotoLibraryAccess(): PHAuthorizationStatus {
    return suspendCancellableCoroutine { continuation ->
        PHPhotoLibrary.requestAuthorization { newStatus ->
            continuation.resume(newStatus)
        }
    }
}

private fun imageToBase64(image: UIImage): String {
    val imageData = UIImagePNGRepresentation(image) ?: return ""
    return imageData.base64EncodedStringWithOptions(0u)
}
*/