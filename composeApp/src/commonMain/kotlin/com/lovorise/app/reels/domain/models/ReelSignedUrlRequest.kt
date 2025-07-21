package com.lovorise.app.reels.domain.models

data class ReelSignedUrlRequest(
    val mediaType:ReelContentType,
    val fileName:String,
    val originalAudio:Boolean = true
)
