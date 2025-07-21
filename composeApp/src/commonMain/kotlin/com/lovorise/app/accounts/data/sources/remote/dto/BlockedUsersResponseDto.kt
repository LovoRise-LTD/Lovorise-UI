package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BlockedUsersResponseDto(
    val message:String? = null,
    val blockedUsers:List<BlockedUserDto?>? = null
){
    @Serializable
    data class BlockedUserDto(
        @SerialName("created_at")
        val createdAt:String? = null,
        val name:String? = null,
        val blockedAuthID:String? = null,
        val imageUrl:String? = null
    )
}
