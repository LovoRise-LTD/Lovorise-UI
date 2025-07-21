package com.lovorise.app.libs.file_handler

import androidx.compose.ui.graphics.ImageBitmap

expect fun imageBitmapToByteArray(imageBitmap: ImageBitmap) : ByteArray?
expect suspend fun getByteArrayFromPath(filePath: String): ByteArray?
