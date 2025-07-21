package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangeEmailRequestDto(
    val page:Int,
    val changeToken:String?,
    val verifier:String?,
    val email:String?,
    val code:String?
)
