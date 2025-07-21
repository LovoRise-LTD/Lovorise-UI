package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerifyPasswordDelete(
    val page:Int,
    val password:String
)
