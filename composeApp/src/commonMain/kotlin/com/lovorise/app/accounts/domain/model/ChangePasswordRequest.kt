package com.lovorise.app.accounts.domain.model


data class ChangePasswordRequest(
    val password:String,
    val newPassword:String
)