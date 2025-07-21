package com.lovorise.app.reels.domain.models

data class MyReelsResponse(
    val reels:List<MyReelItem?>?
){
    data class MyReelItem(
        val id:Int?,
        val statusId:Int?,
        val caption:String?,
        val privacySetting:String?,
        val watchCounter: Int?,
        val reelsContent:ReelsContent?,
        val reelsContentProcessed:List<ReelsContentProcessedItem?>?,
        val createdAt:String?,
        val updatedAt:String?,
        val favoriteCounter:Int?,
        val shareCounter:Int?,
//        val originalAudio:Boolean?,
//        val isFavorite:Boolean?
    ){

        data class ReelsContent(
            val mediaType:ReelContentType?,
            val mediaUrl:String?
        )

        data class ReelsContentProcessedItem(
            val quality:String?,
            val mediaUrl: String?,
            val thumbnailUrl:String?
        )
    }
}
