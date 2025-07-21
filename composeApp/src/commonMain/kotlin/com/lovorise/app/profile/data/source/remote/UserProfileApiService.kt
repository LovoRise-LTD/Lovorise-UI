package com.lovorise.app.profile.data.source.remote

import com.lovorise.app.profile.data.source.remote.dto.ProfileUpdateRequest
import com.lovorise.app.profile.data.source.remote.dto.UpdatePreferencesRequest

interface UserProfileApiService {
    suspend fun modifyProfile(data: ProfileUpdateRequest, token:String): Boolean
    suspend fun modifyPreference(data: UpdatePreferencesRequest, token:String): Boolean
    suspend fun modifyInterests(data: Map<String, List<String>>, token:String): Boolean
}