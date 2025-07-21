package com.lovorise.app.libs.cropper

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import coil3.PlatformContext

expect suspend fun cropImageByImageBitmap(
    imagePath: String,
    offsetX: Float,
    offsetY: Float,
    cropWidth: Int,
    cropHeight: Int,
    scale: Float,
    context: PlatformContext,
    imgSize: IntSize
):String


expect suspend fun saveBitmapToCache(bitmap: ImageBitmap, context: PlatformContext):String