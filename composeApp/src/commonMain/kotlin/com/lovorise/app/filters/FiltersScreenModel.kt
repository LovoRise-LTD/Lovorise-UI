package com.lovorise.app.filters

import cafe.adriel.voyager.core.model.ScreenModel
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.profile.presentation.edit_profile.ProfileBottomSheetType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FiltersScreenModel : ScreenModel {

    private val _state = MutableStateFlow(FiltersScreenState())
    val state = _state.asStateFlow()


    fun loadDataFromAccountsState(state:AccountsApiCallState){
        val user = state.user

        if (user != null){
            _state.update{
                it.copy(
                    showVerifiedOnly = false,
                    interests = user.interests?.values?.filterNotNull()?.flatten()?.filterNotNull() ?: emptyList(),
                    religion = user.religion ?: "",
                    languages = user.language.filterNotNull(),
                    sortBy = FiltersScreenState.SortBy(data = "All"),
                    distancePrefs = user.distance ?: 70.0,
                    education = user.education ?: "",
                    profileStatus = FiltersScreenState.ProfileStatus(data = "All people"),
                    datingPrefs = user.typeOfRelation ?: emptyList(),
                    heightRange = if (user.height == null) 109f..213f else (user.height - 10).toFloat()..(user.height + 10).toFloat(),
                    familyPlanning = user.family ?: "",
                    ageRange = (user.ageStart?.toFloat() ?: 25f)..(user.ageEnd?.toFloat() ?: 35f),
                    showMe = FiltersScreenState.ShowMe(data = user.likeToMeet ?: listOf("All")),
                    locationData = state.currentLocation
                )
            }
        }
    }

    fun updateDistancePrefs(value:Float){
        _state.update {
            it.copy(
                distancePrefs = value.toDouble()
            )
        }
    }

    fun toggleShowVerifiedOnly(){
        _state.update{
            it.copy(
                showVerifiedOnly = !state.value.showVerifiedOnly
            )
        }
    }


    fun updateAgeRange(age:ClosedFloatingPointRange<Float>){
        _state.update {
            it.copy(
                ageRange = age
            )
        }
    }

    fun updateHeightRange(range:ClosedFloatingPointRange<Float>){
        _state.update {
            it.copy(
                heightRange = range
            )
        }
    }

    fun openSheet(sheetType: ProfileBottomSheetType){
        _state.update {
            it.copy(
                sheetType = sheetType,
                showBottomSheet = true
            )
        }
    }

    fun hideSheet(){
        _state.update {
            it.copy(
                showBottomSheet = false
            )
        }
    }


    fun updateDatingPrefs(value:List<String>){
        _state.update {
            it.copy(
                datingPrefs = value
            )
        }
    }

    fun updateReligion(value:String){
        _state.update {
            it.copy(
                religion = value
            )
        }
    }

    fun updateEducation(value:String){
        _state.update {
            it.copy(
                education = value
            )
        }
    }

    fun updateSortBy(value: FiltersScreenState.SortBy){
        _state.update {
            it.copy(
                sortBy = value
            )
        }
    }

    fun updateFamilyPlanning(value:String){
        _state.update {
            it.copy(
                familyPlanning = value
            )
        }
    }

    fun updateLanguages(value:List<String>){
        _state.update {
            it.copy(
                languages = value
            )
        }
    }

    fun updateInterests(value:List<String>){
        _state.update {
            it.copy(
                interests = value
            )
        }
    }

    fun updateShowMe(value:FiltersScreenState.ShowMe){
        _state.update {
            it.copy(
                showMe = value
            )
        }
    }

    fun updateProfileStatus(value:FiltersScreenState.ProfileStatus){
        _state.update {
            it.copy(
                profileStatus = value
            )
        }
    }
}