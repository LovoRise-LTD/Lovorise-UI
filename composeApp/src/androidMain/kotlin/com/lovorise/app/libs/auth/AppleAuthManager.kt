package com.lovorise.app.libs.auth

actual class AppleAuthManager : Oauth {

    override suspend fun login(): OAuthLoginData? {
        return null
    }

    override suspend fun logout() {
        return
    }

    override suspend fun isLastLoggedIn(): Boolean {
        return false
    }
}