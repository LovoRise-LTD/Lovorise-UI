package com.lovorise.app.libs.reels

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import coil3.PlatformContext


actual class DefaultReelsPager actual constructor(private val context: PlatformContext): ReelsPager{

    override fun setVideoUrls(videoUrls: List<String>) {

    }

    @Composable
    override fun ReelsVerticalPager(pagerState: PagerState, currentReelUrl:String, overlayContent : @Composable ()->Unit) {

    }
}

