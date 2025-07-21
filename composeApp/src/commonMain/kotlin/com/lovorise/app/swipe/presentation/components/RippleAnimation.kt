package com.lovorise.app.swipe.presentation.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_ripple_logo
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RippleAnimation(
    modifier: Modifier = Modifier,
    imageSize: Dp = 65.dp,
    rippleColor: Color = Color(0xffF33358).copy(alpha = 0.5f),
    rippleCount: Int = 3,
    rippleMaxRadius: Dp = 192.dp,
    rippleDurationMillis: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()

    val ripples = List(rippleCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = rippleDurationMillis,
                    delayMillis = index * rippleDurationMillis / rippleCount,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(modifier = modifier.size(rippleMaxRadius), contentAlignment = Alignment.Center) {
        for (i in ripples.indices) {
            val scale = ripples[i].value
            Canvas(modifier = Modifier.size(rippleMaxRadius)) {
                scale(scale) {
                    drawIntoCanvas {
                        drawCircle(
                            color = rippleColor.copy(alpha = 1f - scale),
                            radius = size.minDimension / 2
                        )
                    }
                }
            }
        }

        Image(
            imageVector = vectorResource(Res.drawable.ic_ripple_logo),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .alpha(1f)
        )
    }
}