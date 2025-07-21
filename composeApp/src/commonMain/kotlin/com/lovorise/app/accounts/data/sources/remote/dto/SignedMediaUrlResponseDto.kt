package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignedMediaUrlResponseDto(
    @SerialName("mediaID")
    val mediaId:String?,
    val presignUrl:String?
)
