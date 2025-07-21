package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class ReelsResponseDto(
    val error:Boolean? = null,
    val message:String? = null,
    val data:ReelsDataDto? = null
){
    @Serializable
    data class ReelsDataDto(
        val reels: List<ReelDataItemDto?>? = null
    ){
        @Serializable
        data class ReelDataItemDto(
            val reelId:Int? = null,
            val statusId:Int? = null,
            val caption:String? = null,
            val mediaType:String? = null,
            val originalAudio:Boolean? = null,
            val isFavorite:Boolean? = null,
            val content:ReelContentDto? = null,
            val createdAt:String? = null,
            val userData:UserDataDto? = null
        ){
            @Serializable
            data class ReelContentDto(
                val quality:String? = null,
                val mediaUrl:String? = null,
                val thumbnailUrl:String? = null
            )

            @Serializable
            data class UserDataDto(
                val name:String? = null,
                val age:Int? = null,
                val country:String? = null,
                val city:String? = null,
                val distance:Int? = null,
                val verified:Boolean? = null,
                val imageUrl:String? = null
            )
        }
    }


}
