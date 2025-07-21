package com.lovorise.app.libs.file_handler

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

actual fun imageBitmapToByteArray(imageBitmap: ImageBitmap): ByteArray?{

    val bitmap: Bitmap = imageBitmap.asAndroidBitmap()

    // Convert Bitmap to ByteArray using ByteArrayOutputStream
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

actual suspend fun getByteArrayFromPath(filePath: String): ByteArray? {
    println("getting stream for $filePath")
    return withContext(Dispatchers.IO) {
        try {
            val file = File(filePath)
            val fileInputStream = FileInputStream(file)
            val byteArray = fileInputStream.readBytes() // Read file content as byte array
            fileInputStream.close()
            println("stream prepared")
            byteArray
        } catch (e: Exception) {
            println("error occurred while getting stream")
            e.printStackTrace()
            null // Return null if an error occurs
        }
    }

}
