package com.lovorise.app.lovorise_hearts.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OnboardingInfoRewardRequestData(
    val challenge:Challenge
){
    @Serializable
    data class Challenge(
        val type:String,
        val details:Details
    ){
        @Serializable
        data class Details(
            val source:String
        )
    }
}
