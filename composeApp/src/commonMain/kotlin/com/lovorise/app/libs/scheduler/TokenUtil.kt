package com.lovorise.app.libs.scheduler

import coil3.PlatformContext
import com.lovorise.app.di.DebugKtorLogger
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.util.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable


@Serializable
data class TokenDataResponse(
    val accessToken:String? = null,
    val refreshToken:String? = null,
    val error:Boolean? = null,
    val message:String? = null
)

@Serializable
data class RefreshTokenData(
    val refreshToken: String
)

suspend fun refreshToken(): Boolean {
    val prefs = SharedPrefsImpl(null)
    val refreshToken = prefs.getString(PreferencesKeys.REFRESH_TOKEN) ?: return false

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            logger = DebugKtorLogger()
            level = LogLevel.ALL
        }
    }

    return try {
        val response: TokenDataResponse = httpClient.post("${AppConstants.BASE_AUTH_URL}/refresh-token") {
            header("Content-Type", "application/json")
            setBody(RefreshTokenData(refreshToken))
        }.body()

        response.accessToken?.let {
            prefs.setString(PreferencesKeys.AUTH_TOKEN, it)
            prefs.setString(PreferencesKeys.REFRESH_TOKEN, response.refreshToken)
            true
        } ?: false
    } catch (e: Exception) {
        false
    } finally {
        httpClient.close()
    }
}

suspend fun refreshToken(context:PlatformContext?):Boolean{

    val prefs = SharedPrefsImpl(context)
    val refreshToken = prefs.getString(PreferencesKeys.REFRESH_TOKEN) ?: return false

    return withContext(Dispatchers.IO){

        val httpClient = HttpClient {
            install(ContentNegotiation){
                json()
            }
            install(Logging){
                logger = DebugKtorLogger()
                level = LogLevel.ALL
            }
        }

        val response: TokenDataResponse? = try {
            httpClient.post("${AppConstants.BASE_AUTH_URL}/refresh-token") {
                header("Content-Type", "application/json")
                setBody(RefreshTokenData(refreshToken))
            }.body()
        } catch (e: Exception) {
            null
        } finally {
            httpClient.close()
        }

        if (!response?.accessToken.isNullOrBlank() && !response?.refreshToken.isNullOrBlank()){
            prefs.setString(PreferencesKeys.AUTH_TOKEN,response?.accessToken)
            prefs.setString(PreferencesKeys.REFRESH_TOKEN,response?.refreshToken)
            true
        }else{
            false
        }
    }
}