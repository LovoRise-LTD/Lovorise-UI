package com.lovorise.app.lovorise_hearts.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionsResponse(
    val transactions:List<TransactionDto?>?
){
    @Serializable
    data class TransactionDto(
        val id:String?,
        val change:Int?,
        val type:String?,
        val name:String?,
        @SerialName("created_at")
        val timestamp:String?
    )
}
