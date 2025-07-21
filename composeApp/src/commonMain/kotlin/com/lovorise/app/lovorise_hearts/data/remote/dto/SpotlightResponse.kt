package com.lovorise.app.lovorise_hearts.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class SpotlightResponse(
    val type:String,
    val startedAt:String,
    val expiredAt:String,
)
