package com.lovorise.app.accounts.presentation.signup.gender

data class GenderScreenState(
    val selectedGenderIndex:Int = -1,
    val selectedMoreOptionIndex:Int = -1,
    val description:String = "",
    val showBottomSheet:Boolean = false,
    val genders:List<String> = emptyList(),
    val descriptionCharCount:Int = 0,
    val moreOptions:List<String> = emptyList(),
    val moreAboutGender:String = ""
)
