package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BlockActionRequestDto(
    val blockedAuthID:String
)
