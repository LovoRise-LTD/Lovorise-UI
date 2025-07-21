package com.lovorise.app.profile.data.source.remote.dto

import com.lovorise.app.accounts.data.sources.remote.dto.UserResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateRequest(
    val name:String? = null,
    val age:Int? = null,
    val ageVisible:Boolean? = null,
    val gender:String? = null,
    val anotherGender:String? = null,
    val genderVisible:Boolean? = null,
    val height:Int? = null,
    val heightVisible:Boolean? = null,
    val education:String? = null,
    val educationVisible:Boolean? = null,
    val religion:String? = null,
    val religionVisible:Boolean? = null,
    val language:List<String>? = null,
    val languageVisible:Boolean? = null,
    val birthDate:String? = null,
    val birthDateVisible:Boolean? = null,
    val pets:List<String>? = null,
    val petsVisible:Boolean? = null,
    val bio:String? = null,
    val bioVisible:Boolean? = null,
    val ethnicity:String? = null,
    val ethnicityVisible:Boolean? = null,
    val profession:UserResponseDto.UserRes.ProfessionDto? = null,
    val location:LocationDataDto? = null
){
    @Serializable
    data class LocationDataDto(
        val country:String?,
        val city:String?,
        val latitude:Double,
        val longitude:Double
    )
}
