package com.lovorise.app.photo_capture_pick

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import coil3.PlatformContext
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import org.jetbrains.skia.Image
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToFile
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SharedImage(private val image: UIImage?) {

    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): Flow<Pair<Int, ByteArray?>> = flow {
        if (image != null) {
            emit(0 to null) // Initial progress

            // Simulate progress while creating image data
            for (i in 1..40 step 10) {
                delay(50) // Simulating processing time
                emit(i to null)
            }

            val imageData = UIImageJPEGRepresentation(image, COMPRESSION_QUALITY)
                ?: throw IllegalArgumentException("image data is null")

            emit(50 to null) // After getting imageData

            // Simulate progress for byte extraction
            for (i in 51..80 step 10) {
                delay(50)
                emit(i to null)
            }

            val bytes = imageData.bytes ?: throw IllegalArgumentException("image bytes is null")
            val length = imageData.length
            val data: CPointer<ByteVar> = bytes.reinterpret()
            val byteArray = ByteArray(length.toInt()) { index -> data[index] }

            emit(100 to byteArray) // After byte array creation
        } else {
            emit(100 to null)
        }
    }

    actual fun toImageBitmap(): Flow<Pair<Int, ImageBitmap?>> = flow {
        emit(0 to null) // Initial progress
        toByteArray().collect { (progress, byteArray) ->
            if (progress == 100 && byteArray != null) {
                emit(60 to null) // Start conversion to ImageBitmap

                for (i in 61..90 step 5) {
                    delay(50) // Simulate processing
                    emit(i to null)
                }

                val imageBitmap = Image.makeFromEncoded(byteArray).toComposeImageBitmap()
                emit(100 to imageBitmap) // Final emission
            } else {
                emit(progress to null) // Emit ongoing byte array progress
            }
        }
    }

    actual suspend fun saveToCache(context: PlatformContext) : String? {
        if (image != null) {


            val imageData = UIImagePNGRepresentation(image)
                ?: throw IllegalArgumentException("Image data is null")


            // Get the cache directory path
            val cacheDirectory = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true).firstOrNull() as? String ?: return null

            val filePath = "$cacheDirectory/${"CapturedImage_${Clock.System.now().toEpochMilliseconds()}"}"
            NSURL.fileURLWithPath(filePath)

            // Save the NSData to the file system
            return if (imageData.writeToFile(filePath, atomically = true)) {
                filePath
            } else {
                null // Return null if saving fails
            }
        } else {
            return null // If image is null, emit 100 with null
        }


    }

    private companion object {
        const val COMPRESSION_QUALITY = 0.99
    }
}