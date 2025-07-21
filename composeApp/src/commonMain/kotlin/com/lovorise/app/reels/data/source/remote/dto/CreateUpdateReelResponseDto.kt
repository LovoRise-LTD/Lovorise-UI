package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUpdateReelResponseDto(
    val error: Boolean?,
    val message: String?,
    val data: CreatedUpdateReelData?
){
    @Serializable
    data class CreatedUpdateReelData(
        @SerialName("ID")
        val id: Int?,
        @SerialName("AuthID")
        val authId: String?,
        @SerialName("ContentID")
        val contentId: Int?,
        @SerialName("Caption")
        val caption: String?,
        @SerialName("PrivacySetting")
        val privacySetting: String?,
        @SerialName("StatusID")
        val statusId: Int?,
        @SerialName("CreatedAt")
        val createdAt: String?,
        @SerialName("UpdatedAt")
        val updatedAt: String?
    )
}