package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AnonymousStatusResponseDto(
    val id:String? = null,
    @SerialName("created_at")
    val createdAt:String? = null,
    @SerialName("expires_at")
    val expiresAt:String? = null,
    val message:String? = null
)
