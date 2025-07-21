package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequestDto(
    val password:String,
    val newPassword:String,
    val confirmPassword:String
)