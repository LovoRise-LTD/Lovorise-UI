package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_swipe_left_right
import com.lovorise.app.noRippleClickable
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SwipeLeftRightGuide(onClick: () -> Unit) {


    var animation by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        animation = Res.readBytes("files/swipe_left_to_right.json").decodeToString()
    }

    val animationComposition =  rememberLottieComposition {
        LottieCompositionSpec.JsonString(animation)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xffEAEAEA).copy(alpha = 0.56f))
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                imageVector = vectorResource(Res.drawable.ic_swipe_left_right),
                contentDescription = null,
                modifier = Modifier.height(36.dp).width(150.53.dp)
            )

            Image(
                modifier = Modifier.size(80.dp),
                painter = rememberLottiePainter(
                    composition = animationComposition.value,
                    iterations = Compottie.IterateForever,
                ),
                contentScale = ContentScale.FillBounds,
                contentDescription = "Lottie animation"
            )
        }
    }
}
