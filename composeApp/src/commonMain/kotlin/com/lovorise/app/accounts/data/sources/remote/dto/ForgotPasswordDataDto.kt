package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordDataDto(
    val page:Int,
    val email:String?,
    val resetToken:String?,
    val code:String?,
    val password:String?,
)
