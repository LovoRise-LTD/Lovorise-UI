package com.lovorise.app.accounts.data.sources.remote.dto

import com.lovorise.app.libs.iap.AppleReceiptData
import com.lovorise.app.libs.iap.GoogleReceiptData
import kotlinx.serialization.Serializable


@Serializable
data class AppleReceiptDataDto(
    val receiptData:AppleReceiptData
)

@Serializable
data class GoogleReceiptDataDto(
    val receiptData:GoogleReceiptData
)



