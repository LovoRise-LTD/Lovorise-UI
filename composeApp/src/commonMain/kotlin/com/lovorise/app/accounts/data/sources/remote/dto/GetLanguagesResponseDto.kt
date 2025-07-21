package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetLanguagesResponseDto(
    val message:String?,
    val languages:List<String?>?
)
