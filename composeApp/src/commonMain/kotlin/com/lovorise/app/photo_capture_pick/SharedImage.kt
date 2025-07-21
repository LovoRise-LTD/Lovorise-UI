package com.lovorise.app.photo_capture_pick

import androidx.compose.ui.graphics.ImageBitmap
import coil3.PlatformContext
import kotlinx.coroutines.flow.Flow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SharedImage {
    fun toByteArray(): Flow<Pair<Int, ByteArray?>>
    fun toImageBitmap(): Flow<Pair<Int, ImageBitmap?>>
    suspend fun saveToCache(context: PlatformContext) : String?
}