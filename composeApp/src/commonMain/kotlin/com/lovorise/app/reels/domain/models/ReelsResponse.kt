package com.lovorise.app.reels.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class ReelsResponse(
    val reels: List<ReelDataItem?>?
){
    @Serializable
    data class ReelDataItem(
        val reelId:Int?,
        val statusId:Int?,
        val caption:String?,
        val mediaType:ReelContentType?,
        val content:ReelContent?,
        val createdAt:String?,
        val userData:UserData?,
        val originalAudio:Boolean?,
        val isFavorite:Boolean,
    ){

        @Serializable
        data class ReelContent(
            val quality:String?,
            val mediaUrl:String?,
            val thumbnailUrl:String?
        )

        @Serializable
        data class UserData(
            val name:String?,
            val age:Int?,
            val country:String?,
            val city:String?,
            val distance:Int?,
            val verified:Boolean?,
            val imageUrl:String?
        )

    }
}
