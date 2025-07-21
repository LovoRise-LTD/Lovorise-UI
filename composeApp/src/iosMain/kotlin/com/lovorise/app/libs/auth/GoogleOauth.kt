package com.lovorise.app.libs.auth

import coil3.PlatformContext

actual class GoogleOauth actual constructor(context: PlatformContext) : Oauth{

    override suspend fun login():OAuthLoginData? = null

    override suspend fun logout() {}

    override suspend fun isLastLoggedIn(): Boolean {
        return false
    }
}
