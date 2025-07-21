package com.lovorise.app.swipe.data.source.remote

import com.lovorise.app.accounts.data.sources.remote.dto.UserResponseDto
import com.lovorise.app.swipe.data.source.remote.dto.SwipeUserDetailsDto
import com.lovorise.app.swipe.data.source.remote.dto.SwipeUsersResponseDto
import com.lovorise.app.swipe.presentation.components.SwipeType

interface SwipeApiService {
    suspend fun getUsers(token:String) : SwipeUsersResponseDto?
    suspend fun getUserDetails(token:String,userId:String) : UserResponseDto?
    suspend fun updateSwipeInterest(type: SwipeType, token:String, userId:String) : Boolean
    suspend fun reportUser(token: String,userId:String,reasons:List<String>) : Boolean
    suspend fun blockUser(token: String,userId:String) : Boolean
}