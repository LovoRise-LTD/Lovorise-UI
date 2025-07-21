package com.lovorise.app.reels.domain.models


data class CreateUpdateReelResponse(
    val id: Int?,
    val authId: String?,
    val contentId: Int?,
    val caption: String?,
    val privacySetting: String?,
    val statusId: Int?,
    val createdAt: String?,
    val updatedAt: String?
)