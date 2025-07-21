package com.lovorise.app.libs.iap

import kotlinx.serialization.Serializable

@Serializable
data class GoogleReceiptData(
    val orderId:String,
    val productId:String,
    val purchaseTime:Long,
    val purchaseState:Int,
    val purchaseToken: String,
    val acknowledged:Boolean,
    val quantity:Int = 1,
    val packageName:String = "com.lovoriseapp",
)
