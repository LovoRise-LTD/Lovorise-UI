package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.libs.camera.ImageAspect
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileVideoPlayer
import com.lovorise.app.reels.presentation.reels_create_upload_view.screens.Loader

@Composable
fun CircularProfileImage(
    url:String,
    completionPercent:Float,
    onClick:()->Unit,
    cachedPath:String?,
    contentType: SignedUrlMediaItem.Type,
    isDarkMode:Boolean
) {

    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.size(106.dp).noRippleClickable{onClick();println("the on click is getting triggered!!") },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(89.dp)) {
            val width = size.width
            val height = size.height
            val arcWidth = 3.5.dp.toPx()
            drawCircle(
                color = if (isDarkMode) Color(0xff24282B) else Color(0XFFEAECF0),
                center = Offset(width/2,height/2),
                style = Stroke(width = arcWidth)
            )

            drawArc(
                color = Color(0XFFF33358),
               // topLeft = Offset(width / 2.5f, height / 2),
                startAngle = 90f, // 0 represents 3'0 clock
                sweepAngle = (completionPercent / 100) * 360f, // size of the arc
                useCenter = false,
                style = Stroke(width = arcWidth, cap = StrokeCap.Round),
                size = Size(width, height),
            )
        }



        if (contentType == SignedUrlMediaItem.Type.IMAGE) {
            val path = if (!cachedPath.isNullOrBlank()) cachedPath else url
            AsyncImage(
                model = path,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(Color(0xffEAECF0)),
                onLoading = {
                    isLoading = true
                },
                onSuccess = {
                    isLoading = false
                },
                onError = {
                    isLoading = false
                }
            )
            if (isLoading && cachedPath != null) {
                AsyncImage(
                    model = cachedPath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(Color(0xffEAECF0))
                )
            }
        }
        else{
            val path = if (!cachedPath.isNullOrBlank()) cachedPath else url
            ProfileVideoPlayer(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .noRippleClickable{onClick();println("on clicking on profile") }
                    .background(Color(0xffEAECF0)),
                path = path,
                thumbnail = null,
                onLoading = {
                    println("the isLoading value is $it")
                    isLoading = it
                },
                enableProgress = false,
                onProgress = {},
                aspect = ImageAspect.FILL
            )
            Box(modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                .noRippleClickable(onClick)

            )
        }


        if (isLoading) {
            Box(Modifier.size(86.dp), contentAlignment = Alignment.Center) {
                Loader()
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.White, RoundedCornerShape(50))
                .padding(1.dp)
                .background(Color.Red,  RoundedCornerShape(50))
        ) {
            Text(
                modifier = Modifier.padding(vertical = 0.3.dp, horizontal = 3.dp),
                text = "${(completionPercent).toInt()}%",
                style = TextStyle(color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Normal, fontFamily = PoppinsFontFamily(), lineHeight = 18.sp, letterSpacing = 0.2.sp)
            )
        }
    }

}



@Composable
fun CircularProfileIcon(
    onClick: () -> Unit = {},
    imageUrl:String,
    completionPercent:Float,
    iconSize:Dp,
    borderWidth:Dp,
    spotlightType: SpotlightType
   // borderColor:Color = Color(0xffEAAA08)
) {

    Box(
        modifier = Modifier.size(iconSize).noRippleClickable(onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(iconSize)) {
            val width = size.width
            val height = size.height
            val arcWidth = borderWidth.toPx()
            drawCircle(
                color = Color(0XFFEAECF0),
                center = Offset(width/2,height/2),
                style = Stroke(width = arcWidth)
            )

            drawArc(
                color = if (spotlightType == SpotlightType.SPOTLIGHT) Color(0xffEAAA08) else Color(0xffAE02FF),
                // topLeft = Offset(width / 2.5f, height / 2),
                startAngle = -90f, // 0 represents 3'0 clock
                sweepAngle = - (completionPercent / 100) * 360f, // size of the arc
                useCenter = false,
                style = Stroke(width = arcWidth, cap = StrokeCap.Round),
                size = Size(width, height),
            )
        }

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(iconSize - 1.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
    }

}