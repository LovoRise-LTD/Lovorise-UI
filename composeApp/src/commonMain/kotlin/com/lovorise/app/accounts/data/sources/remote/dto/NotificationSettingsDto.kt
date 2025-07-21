package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingsDto(
    val messages: Boolean?,
    val likes: Boolean?,
    val matches: Boolean?,
    val visitors: Boolean?,
    val secretCrush: Boolean?,
    val reels: Boolean?,
    val offerAndPromotion:Boolean?
)
