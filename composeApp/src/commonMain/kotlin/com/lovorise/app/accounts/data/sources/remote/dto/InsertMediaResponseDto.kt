package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InsertMediaResponseDto(
    val error:Boolean? = null,
    val message:String? = null,
    val mediaUrl:String? = null
)
