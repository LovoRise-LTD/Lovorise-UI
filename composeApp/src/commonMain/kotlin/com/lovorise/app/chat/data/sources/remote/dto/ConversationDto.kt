package com.lovorise.app.chat.data.sources.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ConversationDto(
    @SerialName("conversation_id")
    val conversationId: String?,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("last_message")
    val lastMessage: String?,
    @SerialName("last_message_time")
    val lastMessageTime: String?,
    @SerialName("participants")
    val participants: List<String>?,
    @SerialName("unread_count")
    val unreadCount: Int?,
    @SerialName("unread_user")
    val unreadUser: String?
)