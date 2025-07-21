package com.lovorise.app.lovorise_hearts.domain.model

import com.lovorise.app.profile.presentation.components.SpotlightType

data class SpotlightData(
    val type:SpotlightType,
    val remainingMinutes:Long,
    val totalMinutes:Long,
    val expiresAt:String
)
