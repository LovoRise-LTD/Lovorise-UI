package com.lovorise.app.profile.data.source.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class UpdatePreferencesRequest(
    val typeOfRelation:List<String>? = null,
    val family:String? = null,
    val familyVisible:Boolean? = null,
    val drinking:String? = null,
    val drinkingVisible:Boolean? = null,
    val smoking:String? = null,
    val smokingVisible:Boolean? = null,
    val whoWouldYouLikeToMeet:List<String>? = null,
    val whoWouldYouLikeToMeetVisible:Boolean? = null,
    val ageStart:Int? = null,
    val ageEnd:Int? = null,
    val distance:Double? = null,
    val distanceMeasurement:String? = null
)
