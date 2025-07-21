package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddVerifyRecoveryEmailResponseDto(
    val message:String? = null,
    val error:Boolean? = null
)
