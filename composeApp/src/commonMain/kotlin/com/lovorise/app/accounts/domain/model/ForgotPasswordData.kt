package com.lovorise.app.accounts.domain.model

data class ForgotPasswordData(
    val page:Int,
    val email:String?,
    val resetToken:String?,
    val code:String?,
    val password:String?
)
