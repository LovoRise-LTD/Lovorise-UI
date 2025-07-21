package com.lovorise.app.components

import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_heart_small
import com.lovorise.app.PoppinsFontFamily
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.ceil

@Composable
fun RewardsOverlay(
    message:String,
    hearts:Int,
    onClick:()->Unit,
    totalDuration:Long = 3000L
) {

    // State for animating hearts
    var currentHearts by remember { mutableStateOf(0) }

    val totalDuration1 = hearts.toFloat() * 40f
    val interval = ceil(totalDuration1 / hearts).toLong()

    // Gradually increase the hearts count using LaunchedEffect
    LaunchedEffect(hearts) {
        for (i in 0..hearts step 2) {
         //   println("the value for i is $i $interval")
            currentHearts = i
            delay(interval)
        }
    }

    // Animate heart scale (enlarge and contract effect)
    val heartScale by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.3f, // Enlarge size
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = EaseInOutQuad), // Smooth easing
            repeatMode = RepeatMode.Reverse // Contract back to initial
        )
    )

    val animatedHearts by animateIntAsState(currentHearts)


    LaunchedEffect(animatedHearts){
        if (animatedHearts == hearts){
            onClick()
        }
    }

    Dialog(onDismissRequest = {}, properties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        usePlatformDefaultWidth = false
    )){
        Box(
            modifier = Modifier
                .fillMaxSize()
               // .noRippleClickable(onClick)
                //.background(Color(0xFF000000).copy(alpha = 0.65f))
            , // Dark purple background
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.height(115.dp).width(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Image(
                        imageVector = vectorResource(Res.drawable.ic_heart_small),
                        modifier = Modifier
                            .size(46.dp * heartScale),
                        contentScale = ContentScale.Fit,
                        contentDescription = null
                    )


                    Spacer(modifier = Modifier.width(12.7.dp))

                    // +100 text with white border
                    Box(
                        modifier = Modifier.height(81.72.dp).width(140.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        // Fill text
                        Text(
                            text = "+$animatedHearts",
                            fontSize = 54.48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEAAA08), // Fill color
                            fontFamily = PoppinsFontFamily(),
                        )

                        // Stroke text
                        Text(
                            text = "+$animatedHearts",
                            fontSize = 54.48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White, // Make the text itself transparent
                            style = TextStyle.Default.copy(
                                fontSize = 64.sp,
                                drawStyle = Stroke(
                                    miter = 10f,
                                    width = 3f,
                                    join = StrokeJoin.Round
                                ),
                            ),
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Daily login rewards text with white border
                Box(
                    contentAlignment = Alignment.Center
                ) {

                    // Fill text
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFFEAAA08), // Fill color
                        fontFamily = PoppinsFontFamily()
                    )

                    // Stroke text
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White, // Make the text itself transparent
                        style = TextStyle.Default.copy(
                            fontSize = 17.sp,
                            drawStyle = Stroke(
                                miter = 2f,
                                width = 0.5f,
                                join = StrokeJoin.Round
                            ),
                        ),
                        fontFamily = PoppinsFontFamily()
                    )

                }
            }
        }
    }
}