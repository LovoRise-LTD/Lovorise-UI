package com.lovorise.app.swipe.data.repo

import com.lovorise.app.accounts.data.mapper.toUserResponse
import com.lovorise.app.libs.cache.MediaCacheManager
import com.lovorise.app.swipe.data.source.remote.SwipeApiService
import com.lovorise.app.swipe.domain.SwipeProfileUser
import com.lovorise.app.swipe.domain.repo.SwipeRepo
import com.lovorise.app.swipe.presentation.components.SwipeType
import com.lovorise.app.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SwipeRepoImpl(
    private val swipeApiService: SwipeApiService
) : SwipeRepo {

    override suspend fun getUsers(token: String,mediaCacheManager: MediaCacheManager): Flow<Resource<List<SwipeProfileUser>>> {
        return flow {
            emit(Resource.Loading(true))
            val response = swipeApiService.getUsers(token)?.users?.filter { !it.mediaData.isNullOrEmpty() } ?: emptyList()

            val mediaUrls = response.mapNotNull { it.mediaData }.flatten().mapNotNull { it.mediaUrl }

            mediaUrls.map {
                CoroutineScope(Dispatchers.IO).launch {
                      mediaCacheManager.downloadAndCacheIfRequired(it)
                }
            }

            val users = response.mapNotNull { res->
                if (!res.id.isNullOrBlank()) {
                    CoroutineScope(Dispatchers.IO).async {
                        swipeApiService.getUserDetails(token,res.id)?.toUserResponse()
                    }
                }else{
                    null
                }
            }.awaitAll()

            emit(Resource.Success(users.mapNotNull {
                it?.let { user -> SwipeProfileUser(user = user) }
            }))

            emit(Resource.Loading(false))

        }
    }

    override suspend fun updateSwipeInterest(type: SwipeType, token: String, userId: String): Boolean {

        val response = swipeApiService.updateSwipeInterest(type, token, userId)

        return response
    }

    override suspend fun reportUser(token: String, userId: String, reasons:List<String>): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = swipeApiService.reportUser(token, userId, reasons)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Failed to report user"))
            }
        }
    }

    override suspend fun blockUser(token: String, userId: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = swipeApiService.blockUser(token, userId)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Failed to block user"))
            }
        }
    }
}