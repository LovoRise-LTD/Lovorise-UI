package com.lovorise.app.libs.reels

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import coil3.PlatformContext


expect class DefaultReelsPager(context: PlatformContext) : ReelsPager


interface ReelsPager {
    fun setVideoUrls(videoUrls: List<String>)

    @Composable fun ReelsVerticalPager(pagerState: PagerState, currentReelUrl:String, overlayContent : @Composable ()->Unit)
}