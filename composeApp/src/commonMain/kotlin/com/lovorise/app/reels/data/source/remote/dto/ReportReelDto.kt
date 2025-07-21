package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportReelDto(
    val reelId:Int,
    val reasons:List<String>
)
