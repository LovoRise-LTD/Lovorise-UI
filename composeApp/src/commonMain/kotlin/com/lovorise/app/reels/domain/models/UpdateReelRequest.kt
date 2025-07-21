package com.lovorise.app.reels.domain.models


data class UpdateReelRequest(
    val reelId:Int,
    val caption:String,
    val privacySetting:String
)
