package com.lovorise.app.swipe.data.source.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class ReportUserRequest(
    val reasons:List<String>
)
