package com.lovorise.app.chat.domain.model

data class User(
    val id:Int,
    val imageUrl:Any,
    val isVerified:Boolean,
    val name:String,
    val age:Int = 0,
    val isActive:Boolean = true,
    val isBlocked:Boolean = false
)
