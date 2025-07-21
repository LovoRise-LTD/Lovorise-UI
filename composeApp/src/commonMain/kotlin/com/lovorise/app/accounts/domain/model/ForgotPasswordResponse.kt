package com.lovorise.app.accounts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponse(
    val resetToken:String?,
    val token:String?,
    val redirectToRegistration:Boolean?,
    val nextStatus:String?
)
