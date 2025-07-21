package com.lovorise.app.chat.data.sources.remote

import com.lovorise.app.chat.data.sources.remote.dto.ConversationDto
import com.lovorise.app.chat.data.sources.remote.dto.MessageDto
import com.lovorise.app.chat.domain.model.SendMessage
import com.lovorise.app.util.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Job


class DefaultChatApiServiceImpl(
    private val httpClient: HttpClient,
    private val webSocketClient: WebSocketClient
) : ChatApiService {

    override suspend fun createOrGetConversation(receiverUserId: String, token: String): String? {
        val response: String? = try{httpClient.get("${CREATE_OR_GET_CONVERSATION}/$receiverUserId") {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }.body()}catch (e:Exception){null}
        return response
    }

    override suspend fun getConversations(token: String): List<ConversationDto>? {
        val response: List<ConversationDto>? = try{httpClient.get(GET_CONVERSATIONS) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }.body()}catch (e:Exception){
            null
        }
        return response
    }

    override suspend fun sendMessage(data: SendMessage) {
        webSocketClient.send(data)
    }

    override suspend fun getMessages(token: String,conversationId:String) : List<MessageDto>? {
        val response: List<MessageDto>? = try{httpClient.get("${GET_CONVERSATIONS}/$conversationId") {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }.body()}catch (e:Exception){null}
        return response
    }

    override fun connect(token: String,onConnected: () -> Unit,onDisconnected:()->Unit, onMessage: (String) -> Unit, onError: (Throwable) -> Unit): Job {
        return webSocketClient.connect(url = "${CHAT_WEB_SOCKET_URL}$token", onMessage = onMessage, onError = onError, onConnected = onConnected, onDisconnected = onDisconnected)
    }

//    override suspend fun getAllProfiles(token: String): List<UserResponseDto>{
//        val response: List<UserResponseDto>? = try{httpClient.get(GET_PROFILES) {
//            header("Content-Type","application/json")
//            header("Authorization","Bearer $token")
//        }.body()}catch (e:Exception){null}
//        return response ?: emptyList()
//    }

    companion object{
        const val GET_PROFILES = "${AppConstants.BASE_AUTH_URL}/user/profiles"
        const val CHAT_WEB_SOCKET_URL = "wss://chat.lovorise.org/ws?token="
        const val CREATE_OR_GET_CONVERSATION = "${AppConstants.BASE_CHAT_URL}/conversation"
        const val GET_CONVERSATIONS = "${AppConstants.BASE_CHAT_URL}/conversations"
    }
}