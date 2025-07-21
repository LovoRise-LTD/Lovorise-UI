package com.lovorise.app.chat.domain

import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.chat.domain.model.Conversation
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.chat.domain.model.SendMessage
import com.lovorise.app.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface ChatRepo {
    suspend fun createOrGetConversationId(token: String, receiverUserId:String) : Flow<Resource<String>>
    suspend fun getConversations(token: String,userId:String) : Flow<Resource<List<Conversation>>>
    suspend fun getMessagesForConversation(token: String,conversationId:String) : Flow<Resource<List<Message>>>
    suspend fun sendMessage(data: SendMessage)
    fun connect(token: String,onConnected:()->Unit, onDisconnected:()->Unit, onMessage: (String) -> Unit, onError: (Throwable) -> Unit): Job
    suspend fun getAllProfiles(token: String) : Flow<Resource<List<UserResponse>>>
}