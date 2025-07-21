package com.lovorise.app.libs.auth

import coil3.PlatformContext

expect class GoogleOauth(context: PlatformContext) : Oauth


interface Oauth{
    suspend fun login() : OAuthLoginData?
    suspend fun logout()
    suspend fun isLastLoggedIn():Boolean
}


data class OAuthLoginData(
    val token:String?,
    val email:String?
)