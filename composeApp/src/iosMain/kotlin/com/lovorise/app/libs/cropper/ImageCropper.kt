package com.lovorise.app.libs.cropper

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.unit.IntSize
import coil3.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import platform.CoreGraphics.CGImageCreateWithImageInRect
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToFile
import platform.Foundation.writeToURL
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation

@OptIn(ExperimentalForeignApi::class)
actual suspend fun cropImageByImageBitmap(
    imagePath: String,
    offsetX: Float,
    offsetY: Float,
    cropWidth: Int,
    cropHeight: Int,
    scale: Float,
    context: PlatformContext,
    imgSize: IntSize
): String = withContext(Dispatchers.IO) {
    // Load the image from the provided path
    val imageURL = NSURL.fileURLWithPath(imagePath.replace("file://", ""))
    println("the imageUrl is $imageURL $imagePath")
    if (imageURL.path.isNullOrBlank()) return@withContext ""
    if (!NSFileManager.defaultManager.fileExistsAtPath(imageURL.path!!)) {
        println("File does not exist at path: ${imageURL.path}")
        return@withContext ""
    }
    println("reached here 0")
    val image = UIImage.imageWithContentsOfFile(imageURL.path!!) ?: return@withContext ""
    println("reached here 1")

    val orgWidth = image.size.useContents { width }.toInt()
    val orgHeight = image.size.useContents { height }.toInt()

    // Scale the offsets and crop dimensions to the original image size
    val scaledOffsetX = scaleToOriginalDimension(offsetX.toInt(), imgSize.width, orgWidth)
    val scaledOffsetY = scaleToOriginalDimension(offsetY.toInt(), imgSize.height, orgHeight)
    val scaledCropWidth = scaleToOriginalDimension(cropWidth, imgSize.width, orgWidth)
    val scaledCropHeight = scaleToOriginalDimension(cropHeight, imgSize.height, orgHeight)

    // Ensure the cropping dimensions are within bounds
    val validOffsetX = scaledOffsetX.coerceIn(0, orgWidth - scaledCropWidth)
    val validOffsetY = scaledOffsetY.coerceIn(0, orgHeight - scaledCropHeight)

    // Define the cropping rectangle
    val cropRect = CGRectMake(
        x = validOffsetX.toDouble(),
        y = validOffsetY.toDouble(),
        width = scaledCropWidth.toDouble(),
        height = scaledCropHeight.toDouble()
    )

    println("reached here 2")

    // Get the CGImage and perform the cropping
    val cgImage = image.CGImage ?: return@withContext ""
    val croppedCGImage = CGImageCreateWithImageInRect(cgImage, cropRect)
    println("reached here 3")


    // Create a UIImage from the cropped CGImage
    val croppedImage = UIImage (croppedCGImage)

    // Save the cropped image to cache
    val cacheDir = NSFileManager.defaultManager.URLsForDirectory(NSCachesDirectory, NSUserDomainMask)[0] as NSURL

//    val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true);
//    val cacheDirectory = paths.first()

    val croppedImageFileURL = cacheDir.URLByAppendingPathComponent("cropped_image_${Clock.System.now().toEpochMilliseconds()}.png")

    val data = UIImagePNGRepresentation(croppedImage)
    data?.writeToURL(croppedImageFileURL!!, true)

    println("the image path is ${croppedImageFileURL?.path}")

    return@withContext croppedImageFileURL?.path ?: ""// Return the path of the saved image
}

fun scaleToOriginalDimension(value: Int, displayedSize: Int, originalSize: Int): Int {
    return (value.toFloat() / displayedSize * originalSize).toInt()
}


@OptIn(ExperimentalForeignApi::class)
actual suspend fun saveBitmapToCache(bitmap: ImageBitmap, context: PlatformContext):String{
    return withContext(Dispatchers.IO) {
        try {
            val encodedBytes = Image.makeFromBitmap(bitmap.asSkiaBitmap()).encodeToData(EncodedImageFormat.PNG, 100)?.bytes ?: return@withContext ""

            val cacheDir = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true).first()
            val filePath = "$cacheDir/CroppedImage_${Clock.System.now().toEpochMilliseconds()}.png"

            encodedBytes.usePinned { pinnedBytes ->
                val data = NSData.dataWithBytes(pinnedBytes.addressOf(0), encodedBytes.size.toULong())
                if (data.writeToFile(filePath, true)) {
                    // Return the file path if writing succeeded
                    return@usePinned filePath
                }else ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: ""
    }
}

