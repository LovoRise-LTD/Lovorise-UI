package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val error:Boolean? = null,
    val message:String? = null,
    @SerialName("User")
    val user: UserRes? = null,
    @SerialName("Progress")
    val progress:Int? = null,
    @SerialName("Medias")
    val medias:List<MediaListItem?>? = null
){

    @Serializable
    data class MediaListItem(
        @SerialName("MediaID")
        val mediaId:String? = null,
        @SerialName("MediaUrl")
        val mediaUrl:String? = null,
        @SerialName("MediaName")
        val mediaName:String? = null,
        @SerialName("OrderNum")
        val orderNum:Int? = null,
        @SerialName("MediaType")
        val mediaType:String? = null,
        @SerialName("ThumbnailUrl")
        val thumbnailUrl:String? = null
    )

    @Serializable
    data class UserRes(
        @SerialName("Id")
        val id: String,
        @SerialName("Name")
        val name: String? = null,
        @SerialName("Gender")
        val gender: String? = null,
        @SerialName("AnotherGender")
        val anotherGender:String? = null,
        @SerialName("Email")
        val email:String? = null,
        @SerialName("Verified")
        val isVerified:Boolean? = null,
        @SerialName("EmailVerified")
        val isEmailVerified:Boolean? = null,
        @SerialName("Source")
        val source:String? = null,
        @SerialName("GenderVisible")
        val genderVisible:Boolean? = null,
        @SerialName("NameUpdatedAt")
        val nameUpdatedAt:String? = null,
        @SerialName("Age")
        val age: Int? = null,
        @SerialName("AgeUpdatedAt")
        val ageUpdatedAt:String? = null,
        @SerialName("Height")
        val height: Int? = null,
        @SerialName("HeightVisible")
        val heightVisible:Boolean? = null,
        @SerialName("Education")
        val education: String? = null,
        @SerialName("EducationVisible")
        val educationVisible:Boolean? = null,
        @SerialName("Religion")
        val religion: String? = null,
        @SerialName("ReligionVisible")
        val religionVisible:Boolean? = null,
        @SerialName("Language")
        val language: List<String?>? = null,
        @SerialName("LanguageVisible")
        val languageVisible:Boolean? = null,
        @SerialName("Pets")
        val pets: List<String>? = null,
        @SerialName("PetsVisible")
        val petsVisible:Boolean? = null,
        @SerialName("Bio")
        val bio: String? = null,
        @SerialName("BioVisible")
        val bioVisible:Boolean? = null,
        @SerialName("Ethnicity")
        val ethnicity: String? = null,
        @SerialName("EthnicityVisible")
        val ethnicityVisible:Boolean? = null,
        @SerialName("Preferences")
        val preferences: Preferences? = null,
        @SerialName("Interests")
        val interests: Map<String, List<String?>?>? = null,
        @SerialName("Profession")
        val profession:ProfessionDto? = null,
        @SerialName("Location")
        val location:LocationDataDto? = null,
        @SerialName("BirthDate")
        val birthDate:String? = null,
        @SerialName("Status")
        val status:String? = null,
        @SerialName("NameUpdatable")
        val nameUpdatable:Boolean? = null,
        @SerialName("AgeUpdatable")
        val ageUpdatable:Boolean? = null
    ){

        @Serializable
        data class LocationDataDto(
            @SerialName("Country")
            val country:String? = null,
            @SerialName("City")
            val city:String? = null,
            @SerialName("Latitude")
            val latitude:Double? = null,
            @SerialName("Longitude")
            val longitude:Double? = null
        )

        @Serializable
        data class ProfessionDto(
            @SerialName("JobTitle")
            val jobTitle:String? = null,
            @SerialName("Company")
            val company:String? = null,
            @SerialName("ProfessionVisible")
            val professionVisible:Boolean? = null
        )


        @Serializable
        data class Preferences(
            @SerialName("TypeOfRelation")
            val typeOfRelation: List<String>? = null,
            @SerialName("Family")
            val family: String? = null,
            @SerialName("FamilyVisible")
            val familyVisible: Boolean? = null,
            @SerialName("Drinking")
            val drinking: String? = null,
            @SerialName("DrinkingVisible")
            val drinkingVisible: Boolean? = null,
            @SerialName("Smoking")
            val smoking: String? = null,
            @SerialName("SmokingVisible")
            val smokingVisible: Boolean? = null,
            @SerialName("Distance")
            val distance: Double? = null,
            @SerialName("DistanceMeasurement")
            val distanceMeasurement:String? = null,
            @SerialName("AgeStart")
            val ageStart:Int? = null,
            @SerialName("AgeEnd")
            val ageEnd:Int? = null,
            @SerialName("LikeToMeetVisible")
            val likeToMeetVisible: Boolean? = null,
            @SerialName("LikeToMeet")
            val likeToMeet:List<String>? = null
        )
    }
}
