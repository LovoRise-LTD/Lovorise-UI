package com.lovorise.app.chat.data

import com.lovorise.app.accounts.data.mapper.toUserResponse
import com.lovorise.app.accounts.data.sources.remote.AccountsApiService
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.chat.data.sources.remote.ChatApiService
import com.lovorise.app.chat.domain.ChatRepo
import com.lovorise.app.chat.domain.model.Conversation
import com.lovorise.app.chat.domain.model.Message
import com.lovorise.app.chat.domain.model.SendMessage
import com.lovorise.app.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant

class ChatRepoImpl(
    private val chatApiService: ChatApiService,
    private val accountsApiService: AccountsApiService
) : ChatRepo {

    override suspend fun createOrGetConversationId(token: String, receiverUserId: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading(true))
            val response = chatApiService.createOrGetConversation(receiverUserId = receiverUserId, token = token)
            emit(Resource.Loading(false))
            if (response.isNullOrBlank()) {
                emit(Resource.Error("Failed to create conversation"))
            }else{
                emit(Resource.Success(response))
            }
        }
    }

    override suspend fun getConversations(token: String,userId:String): Flow<Resource<List<Conversation>>> {
        return flow {
            emit(Resource.Loading(true))
            val response = chatApiService.getConversations(token)?.filter { !it.lastMessage.isNullOrBlank() } ?: emptyList()
            println("the response size is ${response.size}")
            if (response.isNotEmpty()) {
                val conversations = response.map { conversationDto ->
                    CoroutineScope(Dispatchers.IO).async {
                        if (!conversationDto.conversationId.isNullOrBlank() && conversationDto.participants?.size == 2 && conversationDto.participants.contains(userId)) {
                            val otherUser = conversationDto.participants.firstOrNull{ it != userId }
                            val user = if (!otherUser.isNullOrBlank()) accountsApiService.getProfileDetails(token,otherUser) else null
                            if (user != null) Conversation(
                                unreadUserId = conversationDto.unreadUser,
                                message = Message(
                                    id = "",
                                    conversationId = conversationDto.conversationId,
                                    text = conversationDto.lastMessage ?: "",
                                    timestamp = conversationDto.lastMessageTime?.toTimestamp() ?: "",
                                    senderUserId = ""
                                ),
                                id = conversationDto.conversationId,
                                user = user.toUserResponse()!!,
                                isBlocked = false,
                                unreadCount =if (userId == conversationDto.unreadUser) conversationDto.unreadCount ?: 0 else 0,
                                isOnline = true
                            ) else null
                        }else{
                            null
                        }
                    }
                }.awaitAll()
                emit(Resource.Success(conversations.filterNotNull()))
            }
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMessagesForConversation(token: String, conversationId: String): Flow<Resource<List<Message>>> {
        return flow {
            emit(Resource.Loading(true))
            val response = chatApiService.getMessages(token, conversationId) ?: emptyList()
            emit(Resource.Loading(false))
            if (response.isNotEmpty()){
                val messages = response.map {
                    Message(
                        id = it.messageId ?: "",
                        conversationId = it.conversationId ?: "",
                        senderUserId = it.senderId ?: "",
                        text = it.text ?: "",
                        timestamp = it.createdAt?.toTimestamp() ?: ""
                    )
                }

                emit(Resource.Success(messages))
            }
        }
    }

    override suspend fun sendMessage(data: SendMessage) {
        chatApiService.sendMessage(data)
    }

    override fun connect(token: String, onConnected: () -> Unit, onDisconnected: () -> Unit, onMessage: (String) -> Unit, onError: (Throwable) -> Unit): Job {
        return chatApiService.connect(token = token, onConnected = onConnected, onMessage = onMessage, onError = onError, onDisconnected = onDisconnected)
    }


    override suspend fun getAllProfiles(token: String): Flow<Resource<List<UserResponse>>> {
        return flow {
            emit(Resource.Loading(true))
            val response = accountsApiService.getAllProfiles(token).mapNotNull { it.toUserResponse() }
            emit(Resource.Loading(false))
            emit(Resource.Success(response))
        }
    }
}

fun String.toTimestamp() : String?{

    return try{Instant.parse(this).toEpochMilliseconds().toString()}catch (e:Exception){null}
}