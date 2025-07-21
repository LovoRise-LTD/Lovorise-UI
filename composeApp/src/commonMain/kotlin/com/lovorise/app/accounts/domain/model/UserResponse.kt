package com.lovorise.app.accounts.domain.model

import com.lovorise.app.profile.presentation.edit_profile.DistanceMeasurement


data class UserResponse(
    val id:String,
    val name:String?,
    val nameUpdatedAt:String?,
    val gender:String?,
    val genderVisible:Boolean?,
    val education:String?,
    val educationVisible:Boolean?,
    val religion:String?,
    val religionVisible:Boolean?,
    val language:List<String?>,
    val languageVisible:Boolean?,
    val pets:List<String>?,
    val petsVisible:Boolean?,
    val bio:String?,
    val bioVisible:Boolean?,
    val ethnicity:String?,
    val ethnicityVisible:Boolean?,
    val interests:Map<String,List<String?>?>?,
    val typeOfRelation:List<String>?,
    val family:String?,
    val familyVisible:Boolean?,
    val drinking:String?,
    val drinkingVisible:Boolean?,
    val smoking:String?,
    val smokingVisible:Boolean?,
    val age: Int?,
    val ageUpdatedAt: String?,
    val height: Int?,
    val heightVisible: Boolean?,
    val ageStart:Int?,
    val ageEnd:Int?,
    val likeToMeet:List<String>?,
    val likeToMeetVisible: Boolean?,
    val distance:Double?,
    val distanceMeasurement: DistanceMeasurement,
    val extraGenderText:String?,
    val progress:Int?,
    val medias:List<GetMediaResponse.MediaData>?,
    val locationData:LocationData?,
    val profession: ProfessionData?,
    val birthDate:String?,
    val status:String?,
    val isVerified:Boolean?,
    val isEmailVerified:Boolean,
    val email:String?,
    val source:AccountSource,
    val isNameUpdatable:Boolean,
    val isAgeUpdatable:Boolean,
    val isAgeVisible:Boolean = true,
    val isGenderUpdatable:Boolean,
    val isActive:Boolean = true
){
    data class ProfessionData(
        val jobTitle:String?,
        val company:String?,
        val professionVisible:Boolean?
    )

    data class LocationData(
        val country:String?,
        val city:String?,
        val longitude:Double?,
        val latitude:Double?
    )

}

enum class AccountSource{
    GOOGLE,APPLE,EMAIL
}
