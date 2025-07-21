package com.lovorise.app.accounts.domain.model

data class SignupWithEmailResponse(
    val token:String?,
    val nextScreen:String?,
    val refreshToken:String?
)
