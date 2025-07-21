package com.lovorise.app.accounts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginResponse(
    val token:String,
    val isRegistrationCompleted:Boolean,
    val nextScreen:String?,
    val refreshToken:String
)
