package com.lovorise.app.accounts.domain.model

data class ChangeEmailRequest(
    val page:Int,
    val changeToken:String?,
    val verifier:String?,
    val email:String?,
    val code:String?
)
