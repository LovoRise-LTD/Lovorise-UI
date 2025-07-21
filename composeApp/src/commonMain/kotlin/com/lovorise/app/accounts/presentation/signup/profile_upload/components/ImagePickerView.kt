package com.lovorise.app.accounts.presentation.signup.profile_upload.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_edit_image
import coinui.composeapp.generated.resources.ic_plus
import coinui.composeapp.generated.resources.ic_remove_image
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.libs.camera.ImageAspect
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileVideoPlayer
import com.lovorise.app.reels.presentation.reels_create_upload_view.screens.Loader
import org.jetbrains.compose.resources.painterResource

@Composable
fun ImagePickerView(
    modifier: Modifier = Modifier,
    photo: Photo,
    isBeingDragged: Boolean,
    onClickAdd: () -> Unit,
    onEdit: () -> Unit,
    onRemove: () -> Unit,
    canRemove:Boolean,
   // cropPreviewSize:Size
) {

    LaunchedEffect(true){
        println("ImagePickerView photo: $photo")
    }
    var isLoading by remember { mutableStateOf(false) }
    if (photo.image != null) {
        Box(
            modifier = modifier.height(130.dp)
        ) {
            if (photo.isCaptured || photo.image is ImageBitmap) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(percent = 16))
                        .border(0.5.dp,Color.LightGray.copy(alpha = 0.3f),RoundedCornerShape(percent = 16))
                        .noRippleClickable(if (canRemove) onRemove else onEdit),
                    painter = BitmapPainter(image = photo.image as ImageBitmap),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
            }
            else{
                if (photo.type == SignedUrlMediaItem.Type.IMAGE) {
                    val imageModel = when {
                        photo.placeHolder.isNotBlank() && photo.placeHolder != "null" -> photo.placeHolder // Load from local path if available
                        else -> photo.image
                    }
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xffEAECF0), RoundedCornerShape(percent = 16))
                            .clip(RoundedCornerShape(percent = 16))
                            .noRippleClickable(if (canRemove) onRemove else onEdit),
                        model = imageModel,
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
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
                }
                else{
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xffEAECF0), RoundedCornerShape(percent = 16))
                        .clip(RoundedCornerShape(percent = 16))
                        .clipToBounds()
                        .noRippleClickable(if (canRemove) onRemove else onEdit)){
                        VideoPlayerComposable(
                            modifier = Modifier.fillMaxSize(),
                            playerHost = MediaPlayerHost(
                                mediaUrl = photo.placeHolder.ifBlank {  photo.image.toString() },
                                initialVideoFitMode = ScreenResize.FILL,
                            ),
                            playerConfig = VideoPlayerConfig(
                                showControls = false,
                                showVideoQualityOptions = false,
                                isDurationVisible = false,
                                isZoomEnabled = false,
                                loadingIndicatorColor = Color.Transparent,
                                isGestureVolumeControlEnabled = false,
//            loaderView = {
//                Box(
//                    modifier = Modifier.height(130.dp).fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ){
//                    Loader()
//                }
//            }
                            )
                        )
                    }
                }
            }

            Box(modifier = Modifier
              //  .offset(x = 10.dp, y = (-10).dp)
                .size(24.dp)
                .offset(x = 5.dp, y = (-5).dp)
                .align(Alignment.TopEnd)
                .background(color = Color.White, shape = CircleShape)
                .border(width = 1.dp, color = Color(0xffF33358), shape = CircleShape)
                .noRippleClickable {
                    if (photo.canEdit) onEdit() else onRemove()
                },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(if (photo.canEdit) 13.2.dp else 11.2.dp),
                    painter = painterResource(
                        if (photo.canEdit) Res.drawable.ic_edit_image
                        else Res.drawable.ic_remove_image
                    ),
                    contentDescription = ""
                )
            }
        }
    } else if (!isBeingDragged) {
        Box(
            modifier = modifier
                .height(130.dp)
                .dashedBorder(width = 1.dp, radius = 16.dp, color = Color(0xffA6ABB4)),
        ) {
            Box(modifier = Modifier
                .size(24.dp)
                .background(color = Color(0xffF33358), shape = CircleShape)
                .align(Alignment.Center)
                .noRippleClickable {
                    onClickAdd()
                },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(14.4.dp),
                    painter = painterResource(Res.drawable.ic_plus),
                    contentDescription = ""
                )
            }
        }
    }
    if (isLoading){
        Box(
            modifier = modifier.height(130.dp).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Loader()
        }
    }
}


fun Modifier.dashedBorder(width: Dp, radius: Dp, color: Color) =
    drawBehind {
        drawIntoCanvas {
            val paint = Paint()
                .apply {
                    strokeWidth = width.toPx()
                    this.color = color
                    style = PaintingStyle.Stroke
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                }
            it.drawRoundRect(
                width.toPx(),
                width.toPx(),
                size.width - width.toPx(),
                size.height - width.toPx(),
                radius.toPx(),
                radius.toPx(),
                paint
            )
        }
    }


@Composable
fun VideoPreviewComposable(
    url: String,
    thumbnail:String?,
   // loadingIndicatorColor: Color = Color.White,
   // frameCount: Int = 1,
    onLoading:()->Unit,
    onSuccess:()->Unit
) {

    VideoPlayerComposable(
        modifier = Modifier.fillMaxSize(),
        playerHost = MediaPlayerHost(
            mediaUrl = url,
            initialVideoFitMode = ScreenResize.FILL,
        ),
        playerConfig = VideoPlayerConfig(
            showControls = false,
            showVideoQualityOptions = false,
            isDurationVisible = false,
            isZoomEnabled = false,
            loadingIndicatorColor = Color.Transparent,
            isGestureVolumeControlEnabled = false,
//            loaderView = {
//                Box(
//                    modifier = Modifier.height(130.dp).fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ){
//                    Loader()
//                }
//            }
        )
    )
    ProfileVideoPlayer(
        path = url,
        enableProgress = false,
        thumbnail = null,
        onProgress = {},
        onLoading = { loading->
            if (loading){
                onLoading()
            }
            else{
                onSuccess()
            }
        },
        aspect = ImageAspect.ASPECT_FILL
    )
}

@Composable
private fun VideoPreview1(frames: List<ImageBitmap>) {
//    val frameIndex = remember { Animatable(0f) }
//    LaunchedEffect(Unit) {
//        while (true) {
//            frameIndex.animateTo(
//                targetValue = frames.size.toFloat(),
//                animationSpec = infiniteRepeatable(
//                    animation = tween(durationMillis = 3000, easing = LinearEasing),
//                    repeatMode = RepeatMode.Restart
//                )
//            )
//        }
//    }

    Image(
        bitmap = frames.first(),
        contentDescription = "Preview Frame",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}
