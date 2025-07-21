package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lovorise.app.libs.camera.ImageAspect

@Composable
fun ReelsLoader(modifier: Modifier = Modifier,thumbnailUrl:String,aspect: ImageAspect,enableLoader:Boolean = true) {

    var showLoader by remember { mutableStateOf(false) }

    if (thumbnailUrl.isNotBlank()) {
        AsyncImage(
            model = thumbnailUrl,
            modifier = Modifier.fillMaxSize(),
            contentScale = if (aspect == ImageAspect.ASPECT_FIT) ContentScale.Fit else ContentScale.FillWidth,
            contentDescription = null,
            onError = {
                if (it.result.image == null) {
                    showLoader = true
                }
            }
        )
    }


    if (showLoader && enableLoader) {
        // Infinite animation for rotating the loader
        val infiniteTransition = rememberInfiniteTransition()
        val angleOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1100, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        // Drawing the circular loader
        Canvas(modifier = modifier.size(50.dp)) {
            // Static arc (background arc)
            drawArc(
                color = Color(0xffF33358).copy(alpha = 0.5f), // Static background color
                startAngle = 0f,
                sweepAngle = 360f, // Full circle for the background
                useCenter = false,
                style = Stroke(width = 6.dp.toPx())
            )

            // Moving arc (foreground)
            drawArc(
                color = Color(0xffF33358), // Moving color
                startAngle = angleOffset,
                sweepAngle = 90f, // Adjust for how much of the arc moves
                useCenter = false,
                style = Stroke(width = 6.dp.toPx())
            )
        }
    }
}
