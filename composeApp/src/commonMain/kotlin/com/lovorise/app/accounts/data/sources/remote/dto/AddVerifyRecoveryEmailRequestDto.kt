package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddVerifyRecoveryEmailRequestDto(
    val page:Int,
    val email:String?,
    val code:String?
)
