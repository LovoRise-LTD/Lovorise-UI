package com.lovorise.app.filters

import com.lovorise.app.libs.location.LocationData
import com.lovorise.app.profile.presentation.edit_profile.ProfileBottomSheetType

data class FiltersScreenState(
    val locationData: LocationData? = null,
    val distancePrefs:Double = 80.0,
    val ageRange:ClosedFloatingPointRange<Float> = 18f..37f,
    val heightRange:ClosedFloatingPointRange<Float> = 109f..213f,
    val datingPrefs:List<String> = emptyList(),
    val showMe:ShowMe = ShowMe(),
    val profileStatus:ProfileStatus = ProfileStatus(),
    val sheetType: ProfileBottomSheetType? = null,
    val showBottomSheet:Boolean = false,
    val showVerifiedOnly:Boolean = false,
    val interests:List<String> = emptyList(),
    val languages:List<String> = emptyList(),
    val religion:String = "",
    val familyPlanning:String = "",
    val education:String = "",
    val sortBy:SortBy = SortBy()
){

    data class ShowMe(
        val isExpanded:Boolean = true,
        val data:List<String> = listOf("All")
    )

    data class SortBy(
        val isExpanded:Boolean = true,
        val data:String = ""
    )


    data class ProfileStatus(
        val isExpanded:Boolean = true,
        val data:String = ""
    )
}
