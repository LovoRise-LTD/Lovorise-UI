package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReelRequestDto(
    val reelId:Int,
    val caption:String,
    val privacySetting:String
)
