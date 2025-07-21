package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMediasResponseDto(
    val message:String?,
    val mediaData:List<MediaDataDto?>? = null
){
    @Serializable
    data class MediaDataDto(
        @SerialName("mediaID")
        val mediaId:String? = null,
        val mediaUrl:String? = null,
        val mediaName:String? = null,
        val mediaType:String? = null,
        val orderNum:Int? = null
    )
}
