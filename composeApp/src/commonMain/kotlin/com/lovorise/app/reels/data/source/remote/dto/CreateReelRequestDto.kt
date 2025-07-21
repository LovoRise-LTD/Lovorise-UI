package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateReelRequestDto(
    val privacySetting:String,
    val caption:String,
    val contentId:Int
)
