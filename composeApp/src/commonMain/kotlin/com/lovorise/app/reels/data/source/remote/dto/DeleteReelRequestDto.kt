package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteReelRequestDto(
    val reelId:Int
)
