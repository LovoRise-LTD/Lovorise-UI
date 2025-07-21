package com.lovorise.app.accounts.domain.model

data class AddVerifyRecoveryEmailRequest(
    val page:Int,
    val email:String?,
    val code:String?
)
