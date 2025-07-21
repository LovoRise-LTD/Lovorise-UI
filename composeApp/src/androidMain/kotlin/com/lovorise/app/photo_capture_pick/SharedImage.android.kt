package com.lovorise.app.photo_capture_pick

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.PlatformContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


actual class SharedImage(private val bitmap: Bitmap?) {

    actual fun toByteArray(): Flow<Pair<Int, ByteArray?>> = flow {
        if (bitmap != null) {
            emit(0 to null) // Initial progress

            // Simulate progress during bitmap compression
            for (i in 1..40 step 10) {
                delay(50) // Simulate processing time
                emit(i to null)
            }

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

            emit(50 to null) // Midway progress after compression

            // Simulate more progress for byte conversion
            for (i in 51..80 step 10) {
                delay(50)
                emit(i to null)
            }

            val byteArray = byteArrayOutputStream.toByteArray()
            emit(100 to byteArray) // Final progress with byte array ready
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

                val imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
                emit(100 to imageBitmap) // Final progress
            } else {
                emit(progress to null) // Ongoing progress during byte array conversion
            }
        }
    }

    actual suspend fun saveToCache(context: PlatformContext): String? =
        withContext(Dispatchers.IO) {
            val bitmap = this@SharedImage.bitmap ?: return@withContext null
            try {
                // Get the cache directory
                val cacheDir: File = context.cacheDir

                // Create the file in the cache directory
                val file =
                    File(cacheDir, "Captured_Image_${Clock.System.now().toEpochMilliseconds()}")


                // Write the bitmap to the file
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                return@withContext file.absolutePath

            } catch (e: Exception) {
                return@withContext null
            }
        }

}