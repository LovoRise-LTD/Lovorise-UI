package com.lovorise.app.reels.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FilterReelsRequestDto(
    val distancePreference: Double,
    val ageStart:Int,
    val ageEnd:Int,
    val showMe:String,
    val sortBy:String,
    val premiumFilter: PremiumFilterDto?
){
    @Serializable
    data class PremiumFilterDto(
        val heightStart:Int,
        val heightEnd:Int,
        val datingPreference:String,
        val showVerified:Boolean,
        val religion:String,
        val familyPlans:String,
        val educationLevel:String,
        val language:List<String>
    )
}
