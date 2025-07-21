package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class FavouritesReelsResponseDto(
    val error:Boolean?,
    val message:String?,
    val data:ReelsDataDto?
){
    @Serializable
    data class ReelsDataDto(
        val reels: List<ReelDataItemDto?>?
    ){
        @Serializable
        data class ReelDataItemDto(
            val reelId:Int?,
            val statusId:Int?,
            val caption:String?,
            val mediaType:String?,
            val originalAudio:Boolean? = null,
            val isFavorite:Boolean? = null,
            val content:List<ReelsResponseDto.ReelsDataDto.ReelDataItemDto.ReelContentDto?>,
            val createdAt:String?,
            val userData: ReelsResponseDto.ReelsDataDto.ReelDataItemDto.UserDataDto?
        )
    }


}
