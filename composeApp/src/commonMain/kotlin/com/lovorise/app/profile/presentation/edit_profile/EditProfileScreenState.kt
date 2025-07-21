package com.lovorise.app.profile.presentation.edit_profile

import com.lovorise.app.accounts.domain.model.UserResponse

data class EditProfileScreenState(
    val bio:Bio = Bio(),
    val editProfileBottomSheetType: ProfileBottomSheetType? = null,
    val showBottomSheet:Boolean = false,
    val isFloatingButtonVisible:Boolean = false,
    val datingPrefs:DatingPrefs = DatingPrefs(),
    val name:Name = Name(),
    val age:Age = Age(),
    val gender:Gender = Gender(),
    val showMe:ShowMe = ShowMe(),
    val interests:List<String> = listOf("Art", "Music"),
    val height:Height = Height(),
    val distancePref:Float = 40f,
    val distanceMeasurement:DistanceMeasurement = DistanceMeasurement.KM,
    val ageRange:AgeRange = AgeRange(),
    val educationLevel:EducationLevel = EducationLevel(),
    val profession:Profession = Profession(),
    val pet:Pet = Pet(),
    val religion:Religion = Religion(),
    val familyPlanning: FamilyPlanning = FamilyPlanning(),
    val drinking: Drinking = Drinking(),
    val smoking: Smoking = Smoking(),
    val languages:Languages = Languages(),
    val location:UserResponse.LocationData? = null,
    val shouldNavigateBack:Boolean = false,
    val isLoading:Boolean = false,
    val isProfileUpdated:Boolean = false,
    val isAgeRangeUpdate:Boolean = false,
    val isDistancePreferenceUpdate:Boolean = false,
    val isLocationUpdated:Boolean = false
){

    data class Languages(
        val languages: List<String> = listOf("Hindi","English"),
        val isVisible:Boolean = true,
        val isLocked:Boolean = true
    )

    data class Smoking(
        val items:List<String> = listOf("Non-smoker","Smoker","Social smoker","New smoker"),
        val selectedIndex:Int = -1,
        val isVisible:Boolean = true
    )

    data class Drinking(
        val items:List<String> = listOf("Not for me","Sober curious","Socially","On special occasion","Newly teetotal"),
        val selectedIndex:Int = -1,
        val isVisible:Boolean = true
    )

    data class FamilyPlanning(
        val items:List<String> = listOf("Want children","Don’t want children","Open to children","Have children","Not sure yet","Prefer not to say"),
        val selectedIndex:Int = -1,
        val isVisible:Boolean = true,
        val isLocked:Boolean = true
    )

    data class Religion(
        val items:List<String> = listOf("Muslim","Hindu","Christian","Sikh","Buddhist","Spiritual","Atheist","Other"),
        val selectedIndex:Int = -1,
        val isVisible:Boolean = true,
        val isLocked:Boolean = true
    )

    data class Pet(
        val items:List<String> = listOf("Dog","Bird","Fish","Cat","Turtle","Rabbit","Reptile","Don't have but love","All the pets","Allergic to pets","Other"),
        val selectedIndex:Int = -1,
        val isVisible:Boolean = true
    )

    data class Profession(
        val jobTitle:String = "",
        val orgName:String = "",
        val isVisible:Boolean = false
    )

    data class EducationLevel(
        val items:List<String> = listOf("Bachelor degree","At Uni","College","PhD","On a graduate program","Master degree","Other"),
        val selectedIndex:Int = -1,
        val isVisible:Boolean = true,
        val isLocked:Boolean = true
    )

    data class SortBy(
        val items:List<String> = listOf("All","New"),
        val selectedIndex: Int = 1
    )

    data class AgeRange(
        val start:Int = 20,
        val end:Int = 55
    )

    data class Height(
        val isVisible:Boolean = true,
        val height:Int = -1,
        val index:Int = -1,
        val isLocked:Boolean = true,
        val range:Pair<Float,Float> = Pair(100f,180f)
    )

    data class ShowMe(
        val categories: List<String> = listOf("All","Men","Women","Non-binary"),
        val selectedItems : List<String> = listOf("All"),
        val isVisible: Boolean = true
    )

    data class Gender(
        val categories: List<String> = listOf("Man","Woman","Non-binary"),
        val selectedIndex: Int = -1,
        val isVisible: Boolean = true,
        val moreAboutGender:String = ""
    )

    data class Age(
        val age:Int = 22,
        val formattedAge:String = "",
        val isAgreementChecked:Boolean = true,
        val isVisible:Boolean = true,
        val dobText:String="",
        val lastUpdated:String? = null
    )

    data class Name(
        val name:String = "",
        val isChecked:Boolean = false,
        val lastUpdated:String? = null
    )

    data class DatingPrefs(
        val categories: List<String> = listOf("Life partner","Long-term relationship","Casual meets","New friends","Still figuring it out","Marriage"),
        val selectedItems: List<String> = emptyList(),
        val isLocked:Boolean = true
    )

    data class ShowVerifiedProfile(
        val isLocked:Boolean = true,
        val isChecked:Boolean = true
    )

    data class EditProfileItem(
        val description:String,
        val isVisible:Boolean
    )

    data class Bio(
        val description:String = "",
        val charCount:Int = 0,
        val isVisibleOnProfile:Boolean = true
    )
}

enum class DistanceMeasurement(val stringValue:String){
    KM("km"),MILES("mi")
}

data class GenderData(
    val gender:String,
    val moreAboutGender: String
)

sealed class ProfileBottomSheetType{
    data object UpdateBio : ProfileBottomSheetType()
    data object UpdateDatingPrefs : ProfileBottomSheetType()
    data object UpdateName: ProfileBottomSheetType()
    data object UpdateAge: ProfileBottomSheetType()
    data object UpdateGender: ProfileBottomSheetType()
    data object UpdateShowMe: ProfileBottomSheetType()
    data object UpdateInterests: ProfileBottomSheetType()
    data object UpdateHeight: ProfileBottomSheetType()
    data object UpdateEducationLevel: ProfileBottomSheetType()
    data object UpdateProfession: ProfileBottomSheetType()
    data object UpdatePets: ProfileBottomSheetType()
    data object UpdateReligion: ProfileBottomSheetType()
    data object UpdateFamilyPlans: ProfileBottomSheetType()
    data object UpdateDrinking: ProfileBottomSheetType()
    data object UpdateSmoking: ProfileBottomSheetType()
    data object UpdateLanguages: ProfileBottomSheetType()
    data object UpdateSortBy: ProfileBottomSheetType()
}
