package com.lovorise.app.profile.domain.repo

import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepo {
    suspend fun updateProfile(item: ProfileUpdateItem,data: UpdateProfileData<Any>,token:String):Flow<Resource<Boolean>>
    suspend fun updateUserPrefs(item: ProfilePrefsUpdateItem,data: UpdateProfileData<Any>,token: String):Flow<Resource<Boolean>>
    suspend fun updateUserInterests(data:Map<String,List<String>>,token: String):Flow<Resource<Boolean>>
    suspend fun updateAgeRangeAndDistance(token: String,ageRange:Pair<Int,Int>,distance:Double,distanceMeasurement:String):Flow<Resource<Boolean>>
}