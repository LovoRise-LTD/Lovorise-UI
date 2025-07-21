package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ControlMyViewDto(
    val hideMyAge:Boolean?,
    val onlineStatus:Boolean?,
    val hideLocation:Boolean?
)
