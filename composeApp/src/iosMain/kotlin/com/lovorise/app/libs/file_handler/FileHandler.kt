package com.lovorise.app.libs.file_handler

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import platform.Foundation.*

actual fun imageBitmapToByteArray(imageBitmap: ImageBitmap): ByteArray?{
    return null
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getByteArrayFromPath(filePath: String): ByteArray?{
    return withContext(Dispatchers.Default) {
        try {
            val fileUrl = NSURL.fileURLWithPath(filePath)
            val data = NSData.dataWithContentsOfURL(fileUrl)

            if (data != null) {
                val byteArray = ByteArray(data.length.toInt())
                val dataBytes = data.bytes?.reinterpret<ByteVar>()
                for (i in byteArray.indices) {
                    byteArray[i] = dataBytes?.get(i) ?: 0
                }
                byteArray
            } else {
                null // Return null if data is null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null if an error occurs
        }
    }
}