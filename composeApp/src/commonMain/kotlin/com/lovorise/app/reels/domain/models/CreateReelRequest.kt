package com.lovorise.app.reels.domain.models

data class CreateReelRequest(
    val privacySetting:String,
    val caption:String,
    val contentId:Int
)
