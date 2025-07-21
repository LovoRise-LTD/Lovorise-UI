package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token:String?=null,
    val error:Boolean?=null,
    val message:String?=null,
    val accessToken:String?=null,
    val refreshToken:String?=null,
    val registrationCompleted:Boolean?=null,
    val nextStatus:String?=null
)