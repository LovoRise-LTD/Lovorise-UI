package com.lovorise.app.accounts.presentation.signup.gender

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GenderScreenModel : ScreenModel {

    private val _state = MutableStateFlow(GenderScreenState())
    val state = _state.asStateFlow()


    fun onDescriptionChange(description:String){
        if (description.length <= 20) {
            _state.update {
                it.copy(
                    description = description
                )
            }
        }

    }

    fun resetState(){
        _state.update {
            GenderScreenState()
        }
    }

    fun loadData(genders:List<String>,moreGenderOptions:List<String>){
        _state.update {
            it.copy(
                genders = genders,
                moreOptions = moreGenderOptions
            )
        }
    }


    fun saveGenderInfo(){
        _state.update {
            it.copy(
                moreAboutGender = if (state.value.selectedMoreOptionIndex != -1) state.value.moreOptions.getOrNull(state.value.selectedMoreOptionIndex) ?: "" else state.value.description
            )
        }
    }

    fun selectedGenderIndexChange(index:Int){
        _state.update {
            it.copy(
                selectedGenderIndex = index
            )
        }
    }

    fun selectedMoreOptionIndexChange(index:Int){
        _state.update {
            it.copy(
                selectedMoreOptionIndex = index
            )
        }
    }

    fun updateBottomSheetState(flag:Boolean){
        _state.update {
            it.copy(
                showBottomSheet = flag
            )
        }
    }


}