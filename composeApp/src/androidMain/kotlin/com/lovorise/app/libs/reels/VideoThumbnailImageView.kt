package com.lovorise.app.libs.reels

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import coil3.compose.LocalPlatformContext

@Composable
actual fun VideoThumbnailImageView(modifier: Modifier,imgData:Any){

    val context = LocalPlatformContext.current

    val bmp = remember {  createVideoThumb(context, uri = imgData.toString().toUri())?.asImageBitmap()}

//    val model = ImageRequest.Builder(context)
//        .data(imgData)
//        .decoderFactory { result, options, _ ->
//            VideoFrameDecoder(
//                source = result.source,
//                options = options
//            )
//        }
//        .videoFrameMillis(0)
//        .build()

    if (bmp != null) {
        Image(
            bitmap = bmp,
            contentDescription = null,
//        imageLoader = ImageLoader.Builder(context)
//            .components {
//                add(VideoFrameDecoder.Factory())
//            }
//            .build(),
            modifier = Modifier.fillMaxWidth(),
//                .noRippleClickable(if (video.selectionIndex > 0) onClick else onSelectionClick),
            contentScale = ContentScale.Crop
        )
    }

}

fun createVideoThumb(context: Context, uri: Uri): Bitmap? {
    try {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, uri)
        return mediaMetadataRetriever.frameAtTime
    } catch (ex: Exception) {
//        Toast
//            .makeText(context, "Error retrieving bitmap", Toast.LENGTH_SHORT)
//            .show()
    }
    return null

}