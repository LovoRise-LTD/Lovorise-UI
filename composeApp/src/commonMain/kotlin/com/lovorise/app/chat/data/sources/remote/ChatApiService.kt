package com.lovorise.app.chat.data.sources.remote

import com.lovorise.app.chat.data.sources.remote.dto.ConversationDto
import com.lovorise.app.chat.data.sources.remote.dto.MessageDto
import com.lovorise.app.chat.domain.model.SendMessage
import kotlinx.coroutines.Job

interface ChatApiService{
    suspend fun createOrGetConversation(receiverUserId:String,token:String) : String?
    suspend fun getMessages(token: String,conversationId:String) : List<MessageDto>?
    suspend fun getConversations(token: String) : List<ConversationDto>?
    suspend fun sendMessage(data: SendMessage)
    fun connect(token: String,onConnected:()->Unit,onDisconnected:()->Unit,onMessage: (String) -> Unit, onError: (Throwable) -> Unit): Job
}
