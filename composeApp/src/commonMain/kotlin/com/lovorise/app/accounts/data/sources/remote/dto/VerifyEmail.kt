package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class VerifyEmail(
    val email:String,
    val code:String
)
