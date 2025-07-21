package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReelSignedUrlResponseDto(
    val error: Boolean?,
    val message: String?,
    val data:ReelSignedUrlResponseData?
) {
    @Serializable
    data class ReelSignedUrlResponseData(
        val contentId:Int?,
        val presignUrl:String?
    )
}
