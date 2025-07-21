package com.lovorise.app.lovorise_hearts.domain.model

data class TransactionData(
    val id:String,
    val change:Int,
    val type:Type,
    val name:String,
    val timestamp:String,
    val formattedDate:String = ""
){
    enum class Type(val value:String){
        INCOMING("incoming"),OUTGOING("outgoing")
    }
}
