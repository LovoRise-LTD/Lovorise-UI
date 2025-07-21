package com.lovorise.app.reels.domain.models

data class FilterReelsRequest(
    val distancePreference: Double,
    val ageStart:Int,
    val ageEnd:Int,
    val showMe:String,
    val sortBy:String,
    val premiumFilter: PremiumFilter?
){
    data class PremiumFilter(
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
