package com.lovorise.app.reels.domain.models

data class MarkReelRequest(
    val reelId:Int,
    val markType:ReelStatus,
    val comment:String?
)
