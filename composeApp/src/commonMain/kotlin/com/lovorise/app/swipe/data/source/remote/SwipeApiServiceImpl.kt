package com.lovorise.app.swipe.data.source.remote

import com.lovorise.app.accounts.data.sources.remote.dto.UserResponseDto
import com.lovorise.app.swipe.data.source.remote.dto.MatchResponseDto
import com.lovorise.app.swipe.data.source.remote.dto.ReportUserRequest
import com.lovorise.app.swipe.data.source.remote.dto.SwipeActionRequest
import com.lovorise.app.swipe.data.source.remote.dto.SwipeUsersResponseDto
import com.lovorise.app.swipe.presentation.components.SwipeType
import com.lovorise.app.util.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SwipeApiServiceImpl(
     private val httpClient: HttpClient
) : SwipeApiService {

    override suspend fun getUsers(token: String): SwipeUsersResponseDto? {
        return try {
            val response: SwipeUsersResponseDto? = httpClient.get("$GET_USERS?limit=20&page=1"){
                header("Content-Type","application/json")
                header("Authorization","Bearer $token")
            }.body()
            response
        }catch (e:Exception){
            null
        }
    }

    override suspend fun getUserDetails(token: String, userId: String): UserResponseDto? {
        return try {
            val response: UserResponseDto? = httpClient.get("$GET_USER_DETAIL/${userId}"){
                header("Content-Type","application/json")
                header("Authorization","Bearer $token")
            }.body()
            response
        }catch (e:Exception){
            null
        }
    }

    override suspend fun updateSwipeInterest(type: SwipeType, token: String, userId: String): Boolean {
        return try {
            val response:MatchResponseDto? = httpClient.post("$SWIPE_BASE_URL/user/${userId}/action"){
                header("Content-Type","application/json")
                header("Authorization","Bearer $token")
                setBody(SwipeActionRequest(type = if (type == SwipeType.INTERESTED) 1 else 2))
            }.body()
            println("the match response is $response")
            response?.match == "true"
        }catch (e:Exception){
            false
        }
    }

    override suspend fun reportUser(token: String, userId: String,reasons:List<String>): Boolean {
        return try {
            val response = httpClient.post("$SWIPE_BASE_URL/user/${userId}/report"){
                header("Content-Type","application/json")
                header("Authorization","Bearer $token")
                setBody(ReportUserRequest(reasons))
            }
            response.status.value in 200..300
        }catch (e:Exception){
            false
        }
    }

    override suspend fun blockUser(token: String, userId: String): Boolean {
        return try {
            val response = httpClient.post("$SWIPE_BASE_URL/user/${userId}/block"){
                header("Content-Type","application/json")
                header("Authorization","Bearer $token")
            }
            response.status.value in 200..300
        }catch (e:Exception){
            false
        }
    }

    companion object{
        private const val SWIPE_BASE_URL = "${AppConstants.BASE_AUTH_URL}/swipe"
        private const val GET_USERS = "${SWIPE_BASE_URL}/get-users"
        private const val GET_USER_DETAIL = "${SWIPE_BASE_URL}/user"
    }
}