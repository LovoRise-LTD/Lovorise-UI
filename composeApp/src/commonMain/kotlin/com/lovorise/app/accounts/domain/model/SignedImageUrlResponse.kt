package com.lovorise.app.accounts.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class SignedImageUrlResponse(
    val hash:String,
    val presignUrl:String
)
