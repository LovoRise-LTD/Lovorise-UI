package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponseDto(
    val resetToken:String?=null,
    val token:String?=null,
    val redirectToRegistration:Boolean?=null,
    val message:String? = null,
    val error:Boolean? = null,
    val nextStatus:String? = null
)
