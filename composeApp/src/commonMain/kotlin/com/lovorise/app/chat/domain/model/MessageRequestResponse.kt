package com.lovorise.app.chat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequestResponse(
    val conversationID:String,
    val recipientID:String,
    val message:String,
    val timestamp:String? = null,
)
