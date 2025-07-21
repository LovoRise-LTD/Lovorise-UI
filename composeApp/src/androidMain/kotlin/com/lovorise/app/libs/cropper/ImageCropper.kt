package com.lovorise.app.libs.cropper

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.IntSize
import coil3.PlatformContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


actual suspend fun cropImageByImageBitmap(
    imagePath: String,
    offsetX: Float,
    offsetY: Float,
    cropWidth: Int,
    cropHeight: Int,
    scale: Float, // Include scale parameter
    context: PlatformContext,
    imgSize: IntSize // This represents the displayed size of the image
): String = withContext(Dispatchers.IO) {
    val contentResolver: ContentResolver = context.contentResolver
    var inputStream: InputStream? = null
    val bitmap: Bitmap?

    try {
        // Open the InputStream from the content URI
        inputStream = contentResolver.openInputStream(android.net.Uri.parse(imagePath))
        // Decode the bitmap from the InputStream
        bitmap = BitmapFactory.decodeStream(inputStream)

        bitmap?.let {
            // Step 1: Adjust the offsets and crop dimensions to the original image size based on the zoom scale
            // Scale the offset and crop dimensions back to the original image size (scaled by zoom)
            val scaledOffsetX = scaleToOriginalDimension(offsetX.toInt(), imgSize.width, it.width, scale)
            val scaledOffsetY = scaleToOriginalDimension(offsetY.toInt(), imgSize.height, it.height, scale)
            val scaledCropWidth = scaleToOriginalDimension(cropWidth, imgSize.width, it.width, scale)
            val scaledCropHeight = scaleToOriginalDimension(cropHeight, imgSize.height, it.height, scale)

            // Step 2: Ensure the crop area is within the bounds of the original image
            val validOffsetX = scaledOffsetX.coerceIn(0, it.width - scaledCropWidth)
            val validOffsetY = scaledOffsetY.coerceIn(0, it.height - scaledCropHeight)

            // Step 3: Crop the original bitmap using the calculated dimensions
            val croppedBitmap = Bitmap.createBitmap(
                it,
                validOffsetX,
                validOffsetY,
                scaledCropWidth,
                scaledCropHeight
            )

            // Step 4: Optionally, scale the cropped bitmap to the requested crop size (if you need fixed output size)
            val finalCroppedBitmap = Bitmap.createScaledBitmap(croppedBitmap, cropWidth, cropHeight, false)

            // Step 5: Save the cropped bitmap to cache
            val cacheDir = context.cacheDir
            val croppedImageFile = File(cacheDir, "cropped_image_${System.currentTimeMillis()}.png")
            var outputStream: FileOutputStream? = null

            try {
                outputStream = FileOutputStream(croppedImageFile)
                finalCroppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Save as PNG
            } finally {
                outputStream?.close()
            }

            return@withContext croppedImageFile.absolutePath // Return the path of the saved image
        } ?: return@withContext ""
    } catch (e: IOException) {
        e.printStackTrace() // Handle the error case
        null
    } finally {
        inputStream?.close()
    } ?: ""
}

// Updated scaleToOriginalDimension function to include zoom scale
fun scaleToOriginalDimension(value: Int, displayedSize: Int, originalSize: Int, scale: Float): Int {
    return ((value / displayedSize.toFloat()) * originalSize / scale).toInt()
}

actual suspend fun saveBitmapToCache(bitmap: ImageBitmap, context: PlatformContext):String{
    return withContext(Dispatchers.IO) {
        try {
            // Get the cache directory
            val cacheDir = context.cacheDir

            // Create a file in the cache directory
            val file = File(cacheDir, "CroppedImage_${System.currentTimeMillis()}.png")

            // Open a file output stream
            val outputStream = FileOutputStream(file)

            // Compress the bitmap as PNG and write it to the output stream
            bitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            // Flush and close the output stream
            outputStream.flush()
            outputStream.close()

            // Return the file path as a string
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } ?: ""
    }
}