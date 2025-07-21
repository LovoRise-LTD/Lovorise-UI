package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HelpSupportRequestDto(
    val type:List<String>,
    val description:String,
    val uploads:List<String>
)
