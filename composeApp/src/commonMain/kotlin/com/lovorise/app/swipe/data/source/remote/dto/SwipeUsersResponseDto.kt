package com.lovorise.app.swipe.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SwipeUsersResponseDto(
    val message:String? = null,
    val users:List<User>? = null
){
    @Serializable
    data class User(
        @SerialName("authID")
        val id:String? = null,
        val mediaData:List<MediaData>? = null
    ){
        @Serializable
        data class MediaData(
            val mediaUrl:String? = null
        )
    }
}
