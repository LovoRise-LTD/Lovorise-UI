package com.lovorise.app.chat.domain.model

import com.lovorise.app.accounts.domain.model.UserResponse

data class Conversation(
    val id:String,
    val user: UserResponse,
    val unreadCount:Int,
    val unreadUserId:String?,
    val message: Message?,
    val isOnline:Boolean,
    val isBlocked:Boolean = false
)
