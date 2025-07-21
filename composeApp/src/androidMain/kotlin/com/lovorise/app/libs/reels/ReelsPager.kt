package com.lovorise.app.libs.reels

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil3.PlatformContext


actual class DefaultReelsPager actual constructor(private val context: PlatformContext) : ReelsPager{

    private var videoUrlList = listOf<String>()
    private val players = mutableStateListOf<ExoPlayer?>()

    override fun setVideoUrls(videoUrls: List<String>) {
        videoUrlList = videoUrls
        // Prepare ExoPlayer instances for each video URL
        players.clear()
        videoUrlList.forEach { url ->
            val player = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(url)
                setMediaItem(mediaItem)
                prepare()
            }
            players.add(player)
        }
    }

    @Composable
    override fun ReelsVerticalPager(
        pagerState: PagerState,
        currentReelUrl:String,
        overlayContent : @Composable ()->Unit,

    ) {

        VerticalPager(
            state = pagerState,
            pageSpacing = 0.dp,
        ) { _ ->

           // val reels = if(page == 0) reelsForYou else reelsFromMatches

            Box {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    //VideoPlayerView(modifier = Modifier.fillMaxSize(), url = currentReelUrl)
                }
                overlayContent()
            }


        }


    }
}

