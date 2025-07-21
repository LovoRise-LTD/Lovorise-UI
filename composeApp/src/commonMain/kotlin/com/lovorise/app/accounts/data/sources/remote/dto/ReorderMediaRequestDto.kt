package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReorderMediaRequestDto(
    val medias:List<MediaItem>
){

    @Serializable
    data class MediaItem(
        @SerialName("mediaID")
        val mediaId:String,
        @SerialName("orderNum")
        val orderNum:Int
    )
}
