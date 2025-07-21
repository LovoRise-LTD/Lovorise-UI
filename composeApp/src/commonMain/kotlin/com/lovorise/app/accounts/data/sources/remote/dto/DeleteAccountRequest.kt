package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequest(
    val page:Int,
    val reasons:List<String>
)
