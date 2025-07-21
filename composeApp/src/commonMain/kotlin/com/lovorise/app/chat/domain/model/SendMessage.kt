package com.lovorise.app.chat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SendMessage(
    val conversationID:String,
    val recipientID:String,
    val message:String,
)
