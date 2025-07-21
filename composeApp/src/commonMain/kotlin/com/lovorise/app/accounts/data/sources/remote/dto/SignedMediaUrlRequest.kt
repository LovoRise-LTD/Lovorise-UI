package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignedMediaUrlRequest(
    val files:List<FileData>
){
    @Serializable
    data class FileData(
        val name:String,
        val type:String
    )
}
