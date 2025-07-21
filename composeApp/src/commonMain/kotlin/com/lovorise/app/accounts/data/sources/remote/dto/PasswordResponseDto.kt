package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResponseDto(
    val error:Boolean? = null,
    val message:String? = null,
    val isPasswordSet:Boolean? = null
)
