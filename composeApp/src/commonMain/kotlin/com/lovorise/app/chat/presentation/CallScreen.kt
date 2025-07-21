package com.lovorise.app.chat.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_end_call
import coinui.composeapp.generated.resources.ic_mic_disabled
import coinui.composeapp.generated.resources.ic_mic_enabled
import coinui.composeapp.generated.resources.ic_speaker_disabled
import coinui.composeapp.generated.resources.ic_speaker_enabled
import coinui.composeapp.generated.resources.ringing
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.chat.domain.model.Conversation
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class CallScreen (private val chatScreenModel: ChatScreenModel, private val conversation: Conversation): Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        CallScreenContent(
            isDarkMode = isDarkMode,
            chatScreenModel = chatScreenModel,
            conversation = conversation,
            onBack = {
                navigator.pop()
            }
        )
    }
}

@Composable
fun CallScreenContent(isDarkMode:Boolean, chatScreenModel: ChatScreenModel, conversation: Conversation, onBack:()->Unit) {

    val state by chatScreenModel.state.collectAsState()

//    var animation by remember { mutableStateOf("") }

//
//    LaunchedEffect(Unit) {
//        animation = Res.readBytes("files/ringing_dots_animation.json").decodeToString()
//    }
//
//    val animationComposition =  rememberLottieComposition {
//        LottieCompositionSpec.JsonString(animation)
//    }

    LaunchedEffect(true){
        delay(4000L)
        chatScreenModel.onCalledPicked()
    }

    val timestamp by state.ongoingCallTime.collectAsState("")


    Column {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .background(brush = Brush.linearGradient(
                    listOf(
                        Color(0xffF33358).copy(alpha = 1f),
                        Color(0xffF33365).copy(alpha = 1f),
                        Color(0xffF33372).copy(alpha = 0.95f),
                        Color(0xffF33382).copy(alpha = 0.94f),
                        Color(0xffF3338F).copy(alpha = 0.9f),
                        Color(0xffF3339B).copy(alpha = 0.82f),
                    )
                ))
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            Spacer(Modifier.height(78.dp))

            if (state.isRinging){
                RingingAnimationImage(imageUrl = conversation.user.medias?.firstOrNull()?.url ?: "")
            } else {
                Spacer(Modifier.height(48.dp))
                AsyncImage(
                    model = conversation.user.medias?.firstOrNull()?.url ?: "",
                    contentDescription = null,
                    modifier = Modifier
                        .size(95.87.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(14.dp))


            Text(
                text = conversation.user.name ?: "",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                fontSize = 16.sp,
                letterSpacing = (-0.3).sp
            )

            Spacer(Modifier.height(8.dp))

            Row(Modifier.height(21.dp)) {
                Text(
                    text = if (state.isRinging) "${stringResource(Res.string.ringing)}  " else timestamp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    lineHeight = 21.sp,
                    fontSize = 14.sp,
                    letterSpacing = (-0.3).sp
                )

                if (state.isRinging) {
                    Column(Modifier.fillMaxHeight(), horizontalAlignment = Alignment.Start) {
                        Spacer(Modifier.weight(1f))
                        ThreeDotsLoader()
//                        Image(
//                            modifier = Modifier.width(24.dp).height(15.dp),
//                            painter = rememberLottiePainter(
//                                composition = animationComposition.value,
//                                iterations = Compottie.IterateForever,
//                                clipToCompositionBounds = true
//                            ),
//                            contentScale = ContentScale.Inside,
//                            contentDescription = "Lottie animation"
//                        )
                    }
                }
            }



            Spacer(Modifier.weight(1f))

            Row(
                Modifier.height(45.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))
                Image(
                    imageVector = vectorResource(if (state.isSpeakerDisabled) Res.drawable.ic_speaker_disabled else Res.drawable.ic_speaker_enabled),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp).noRippleClickable {
                        chatScreenModel.toggleSpeakerState()
                    }
                )

                Image(
                    imageVector = vectorResource(if (state.isMicDisabled) Res.drawable.ic_mic_disabled else Res.drawable.ic_mic_enabled),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp).noRippleClickable {
                        chatScreenModel.toggleMicState()
                    }
                )

                Image(
                    imageVector = vectorResource(Res.drawable.ic_end_call),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp).noRippleClickable {
                        onBack()
                        chatScreenModel.onCallEnded()
                    }
                )
                Spacer(Modifier.weight(1f))

            }


            Spacer(Modifier.height(32.dp))



        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )
    }


}


@Composable
fun RingingAnimationImage(
    imageUrl:String,
    modifier: Modifier = Modifier,
    imageSize: Dp = 95.87.dp,
//    rippleColor: Color = Color(0xffF33358).copy(alpha = 0.5f),
//    rippleCount: Int = 3,
    rippleMaxRadius: Dp = 192.dp,
  //  rippleDurationMillis: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()

//    val ripples = List(rippleCount) { index ->
//        infiniteTransition.animateFloat(
//            initialValue = 0f,
//            targetValue = 1f,
//            animationSpec = infiniteRepeatable(
//                animation = tween(
//                    durationMillis = rippleDurationMillis,
//                    delayMillis = index * rippleDurationMillis / rippleCount,
//                    easing = LinearOutSlowInEasing
//                ),
//                repeatMode = RepeatMode.Restart
//            )
//        )
//    }

    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val rippleSize by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = modifier.size(rippleMaxRadius).clip(CircleShape), contentAlignment = Alignment.Center) {
//        for (i in ripples.indices) {
//            val scale = ripples[i].value
//            Canvas(modifier = Modifier.size(rippleMaxRadius)) {
//                scale(scale) {
//                    drawIntoCanvas {
//                        drawCircle(
//                            color = rippleColor.copy(alpha = 1f - scale),
//                            radius = size.minDimension / 2
//                        )
//                    }
//                }
//            }
//        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = rippleAlpha),
                radius = rippleSize
            )
            drawCircle(
                color = Color.White.copy(alpha = rippleAlpha * 0.7f),
                radius = rippleSize * 0.7f
            )
            drawCircle(
                color = Color.White.copy(alpha = rippleAlpha * 0.4f),
                radius = rippleSize * 0.4f
            )
        }

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .alpha(1f),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ThreeDotsLoader() {
    val dotScale = remember { List(3) { Animatable(0f) } }

    dotScale.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            delay(index * 150L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0f at 0 using LinearEasing
                        1f at 300 using  LinearEasing
                        0f at 600 using  LinearEasing
                        0f at 1200
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.wrapContentWidth().height(10.dp)

    ) {
        dotScale.forEach { scale ->
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .scale(scale.value)
                    .background(Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(3.dp))
        }
    }
}