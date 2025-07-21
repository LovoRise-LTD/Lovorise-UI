package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyReelsResponseDto(
//    @SerialName("Reels")
    val reels:List<MyReelItemDto?>?
){
    @Serializable
    data class MyReelItemDto(
        @SerialName("reelId")
        val id:Int?,
//        @SerialName("StatusId")
        val statusId:Int?,
//        @SerialName("Caption")
        val caption:String?,
//        @SerialName("PrivacySetting")
        val privacySetting:String?,
//        @SerialName("WatchCounter")
        val watchCounter: Int?,
        val favoriteCounter:Int?,
        val shareCounter:Int?,
        @SerialName("content")
        val reelsContent:ReelsContentDto?,
        @SerialName("contentProcessed")
        val reelsContentProcessed:List<ReelsContentProcessedItemDto?>?,
//        @SerialName("CreatedAt")
        val createdAt:String?,
//        @SerialName("UpdatedAt")
        val updatedAt:String?
    ){

        @Serializable
        data class ReelsContentDto(
//            @SerialName("MediaType")
            val mediaType:String?,
//            @SerialName("MediaUrl")
            val mediaUrl:String?
        )

        @Serializable
        data class ReelsContentProcessedItemDto(
//            @SerialName("Quality")
            val quality:String?,
//            @SerialName("MediaUrl")
            val mediaUrl: String?,
//            @SerialName("ThumbnailUrl")
            val thumbnailUrl:String?
        )
    }
}
