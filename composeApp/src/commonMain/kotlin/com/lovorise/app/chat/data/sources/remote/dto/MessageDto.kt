package com.lovorise.app.chat.data.sources.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("conversation_id")
    val conversationId: String?,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("gift")
    val gift: String?,
    @SerialName("media")
    val media: String?,
    @SerialName("message_id")
    val messageId: String?,
    @SerialName("sender_id")
    val senderId: String?,
    @SerialName("text")
    val text: String?,
    @SerialName("type")
    val type: String?
)