package com.lovorise.app.swipe.domain.repo

import com.lovorise.app.libs.cache.MediaCacheManager
import com.lovorise.app.swipe.domain.SwipeProfileUser
import com.lovorise.app.swipe.presentation.components.SwipeType
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface SwipeRepo {
    suspend fun getUsers(token:String,mediaCacheManager: MediaCacheManager) : Flow<Resource<List<SwipeProfileUser>>>
    suspend fun updateSwipeInterest(type:SwipeType,token:String,userId:String) : Boolean
    suspend fun reportUser(token: String,userId:String,reasons:List<String>) : Flow<Resource<Boolean>>
    suspend fun blockUser(token: String,userId:String) : Flow<Resource<Boolean>>
}