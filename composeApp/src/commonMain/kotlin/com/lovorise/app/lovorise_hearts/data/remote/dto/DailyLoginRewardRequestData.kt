package com.lovorise.app.lovorise_hearts.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DailyLoginRewardRequestData(
    val challenge:Challenge
){
    @Serializable
    data class Challenge(
        val type:String,
    )
}
