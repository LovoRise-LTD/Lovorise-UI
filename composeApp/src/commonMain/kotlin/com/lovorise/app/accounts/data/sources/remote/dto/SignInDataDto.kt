package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInDataDto(
    val action: String = "signin",
    val signin: SignIn
){
    @Serializable
    data class SignIn(
        val email:String,
        val password:String
    )
}
