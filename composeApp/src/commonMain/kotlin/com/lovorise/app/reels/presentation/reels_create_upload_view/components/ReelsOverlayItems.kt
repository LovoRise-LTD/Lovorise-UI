package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_a_caption
import coinui.composeapp.generated.resources.done
import coinui.composeapp.generated.resources.ic_brush
import coinui.composeapp.generated.resources.ic_crop
import coinui.composeapp.generated.resources.ic_cross_white
import coinui.composeapp.generated.resources.ic_download
import coinui.composeapp.generated.resources.ic_emoji_icon
import coinui.composeapp.generated.resources.ic_microphone
import coinui.composeapp.generated.resources.ic_muted
import coinui.composeapp.generated.resources.ic_reels_settings
import coinui.composeapp.generated.resources.ic_unmuted
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ShimmerAnimation
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ReelsOverlayItems(

    caption:String,
    onCaptionChange:(String)->Unit,
    isImeVisible:Boolean,
    onDoneClick:()->Unit,
    onCancel:()->Unit,
    onSettingsIconClick:()->Unit,
    onDownloadClicked:()->Unit,
    videoFrames:List<Any>,
    progress:Float,
    isPlaying:Boolean,
    onVideoClipRange: (ClosedFloatingPointRange<Float>) ->Unit,
    isVideo:Boolean,
    isVideoMuted:Boolean,
    onToggleMutedState:()->Unit,
    hideMutedState:Boolean = false
) {


    val animatedCornerRadius by animateDpAsState(targetValue = if (isImeVisible) 0.dp else 8.dp)
    val animatedHorizontalPadding by animateDpAsState(targetValue = if (isImeVisible) 0.dp else 16.dp)
    val animatedColor by animateColorAsState(targetValue = if (isImeVisible) Color(0xffF2F4F7) else Color(0xffF2F4F7).copy(alpha = 0.6f))

    Column(
        Modifier.fillMaxSize().padding(top = 29.dp)
    ) {
        Row(Modifier.height(56.dp).fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(24.dp).noRippleClickable(onCancel), contentAlignment = Alignment.Center){
                Image(
                    imageVector = vectorResource(Res.drawable.ic_cross_white),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            Box(Modifier.size(24.dp).noRippleClickable(onDownloadClicked), contentAlignment = Alignment.Center){
                Image(
                    imageVector = vectorResource(Res.drawable.ic_download),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Box(Modifier.size(24.dp).noRippleClickable(onSettingsIconClick), contentAlignment = Alignment.Center){
                Image(
                    imageVector = vectorResource(Res.drawable.ic_reels_settings),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

        }
        Spacer(Modifier.weight(1f))


        if (isVideo && !hideMutedState) {
            Image(
                imageVector = vectorResource(if(isVideoMuted) Res.drawable.ic_muted else Res.drawable.ic_unmuted) ,
                contentDescription = null,
                modifier = Modifier.padding(end = 23.dp).size(24.dp).noRippleClickable(onToggleMutedState).align(Alignment.End)
            )
            Spacer(Modifier.height(12.dp))
        }


        TextFieldWithIcon(
            label = stringResource(Res.string.add_a_caption),
            value = caption,
            onTextFieldValueChange = onCaptionChange,
            horizontalPadding = animatedHorizontalPadding,
            cornerRadius = animatedCornerRadius,
            backgroundColor = animatedColor,
            icon = Res.drawable.ic_emoji_icon
        )
        Spacer(Modifier.height(10.dp))
        if (isVideo) {
            Box(Modifier.padding(horizontal = 16.dp)) {
                if (videoFrames.isNotEmpty()){
                    VideoTrimmingSlider(frameList = videoFrames, progress = progress, onVideoClipRange = onVideoClipRange)
                }else{
                    ShimmerAnimation(Modifier.height(36.dp).fillMaxWidth(), shape = RoundedCornerShape(2.dp))
                }
                //   PlayerProgressIndicator(progress = progress)
            }
        }

        if (!isImeVisible) {
            Box(
                Modifier.height(68.dp).fillMaxWidth().padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isVideo) {
                        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                            Image(
                                imageVector = vectorResource(Res.drawable.ic_crop),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(Modifier.width(24.dp))

                        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                            Image(
                                imageVector = vectorResource(Res.drawable.ic_brush),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(Modifier.width(24.dp))

                        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                            Image(
                                imageVector = vectorResource(Res.drawable.ic_microphone),
                                contentDescription = null,
                                modifier = Modifier.height(19.83.dp).width(15.47.dp)
                            )
                        }
                    }

                    Spacer(Modifier.weight(1f))


                    Box(
                        Modifier.wrapContentSize()
                            .background(Color(0xffF33358), RoundedCornerShape(8.dp))
                            .noRippleClickable(onDoneClick), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                            text = stringResource(Res.string.done),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.2.sp,
                            textAlign = TextAlign.Center
                        )
                    }


                }
            }
        }

    }


}