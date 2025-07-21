package com.lovorise.app.libs.media_player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.PlatformContext
import com.lovorise.app.libs.camera.ImageAspect


@Composable
expect fun VideoPlayerView(modifier: Modifier = Modifier,url:String,thumbnailUrl:String,isVideoPausedInitially:Boolean = false,isPlaying:Boolean = true,onContentDuration:(Long)->Unit = {},onCurrentPosition:(Long)->Unit = {},clipRange: ClosedFloatingPointRange<Float> = 0f..100f,enableAdvancedSettings:Boolean = false,isMuted:Boolean=false,onLoadingState:(Boolean)->Unit = {},aspect: ImageAspect=ImageAspect.ASPECT_FIT,enableLoader:Boolean=true,slowVideo:Boolean= false)

expect class VideoPlayer(context:PlatformContext)

expect fun preloadVideoListInBackground(urls:List<String>,context: PlatformContext)

expect fun exportVideoSegment(context: PlatformContext,url: String, clipRange: ClosedFloatingPointRange<Float>, onExportComplete: (Boolean, String?) -> Unit)
