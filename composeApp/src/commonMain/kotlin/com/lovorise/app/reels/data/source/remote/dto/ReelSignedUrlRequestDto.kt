package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReelSignedUrlRequestDto(
    val mediaType:String,
    val fileName:String,
    val originalAudio:Boolean
)
