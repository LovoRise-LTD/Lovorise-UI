package com.lovorise.app.profile.data.source.remote

import com.lovorise.app.profile.data.source.remote.dto.ProfileUpdateRequest
import com.lovorise.app.profile.data.source.remote.dto.UpdatePreferencesRequest
import com.lovorise.app.util.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class UserProfileApiServiceImplementation(
    private val httpClient: HttpClient
) : UserProfileApiService {

    override suspend fun modifyProfile(data: ProfileUpdateRequest, token: String): Boolean {
        val response = try{httpClient.put(PROFILE_API_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }}catch (e:Exception){
            null
        }

        return response?.status?.value in 200..300
    }

    override suspend fun modifyPreference(data: UpdatePreferencesRequest, token: String): Boolean {
        val response = try{httpClient.put(PREFERENCES_API_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }}catch (e:Exception){
            null
        }

        return response?.status?.value in 200..300
    }

    override suspend fun modifyInterests(data: Map<String, List<String>>, token: String): Boolean {
        val response = try{ httpClient.put(INTERESTS_API_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }}catch (e:Exception){
            null
        }

        return response?.status?.value in 200..300
    }

    companion object{
        const val PROFILE_API_URL = "${AppConstants.BASE_AUTH_URL}/user/profile"
        const val PREFERENCES_API_URL = "${AppConstants.BASE_AUTH_URL}/user/preference"
        const val INTERESTS_API_URL = "${AppConstants.BASE_AUTH_URL}/user/interests"
    }
}