package com.lovorise.app.swipe.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SwipeUserDetailsDto(
    val name:String? = null,
    val age: Int? = null,
    val gender:String? = null,
    val language:List<String>? = null,
    val status:String? = null,
    val mediaData:List<MediaItem?>? = null,
    val preference:Preferences? = null,
    val interests: Map<String, List<String?>?>? = null,
){
    @Serializable
    data class MediaItem(
        val mediaID:String,
        val mediaUrl:String,
        val mediaType:String
    )

    @Serializable
    data class Preferences(
        val typeOfRelation: String?,
        val likeToMeet: String?
    )
}
