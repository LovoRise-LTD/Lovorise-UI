package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsertMediaRequestDto(
    @SerialName("mediaID")
    val mediaID:String?,
    @SerialName("orderNum")
    val orderNum:Int?
)
