package com.lovorise.app.libs.iap

import kotlinx.serialization.Serializable

@Serializable
data class AppleReceiptData(
    val productId:String,
    val transactionId:String,
    val purchaseToken: String,
    val quantity:Int = 1
)
