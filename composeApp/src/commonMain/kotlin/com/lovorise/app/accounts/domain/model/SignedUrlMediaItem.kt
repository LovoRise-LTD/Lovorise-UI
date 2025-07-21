package com.lovorise.app.accounts.domain.model

data class SignedUrlMediaItem(
    val type:Type,
    val fileName:String
){
    enum class Type{
        IMAGE,VIDEO
    }
}
