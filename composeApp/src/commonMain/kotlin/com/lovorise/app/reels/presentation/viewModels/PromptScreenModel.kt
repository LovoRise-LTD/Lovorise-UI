package com.lovorise.app.reels.presentation.viewModels

import cafe.adriel.voyager.core.model.ScreenModel
import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelPrivacySetting
import com.lovorise.app.reels.presentation.states.PromptScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PromptScreenModel : ScreenModel {

    private val _state = MutableStateFlow(PromptScreenState())
    val state = _state.asStateFlow()

    fun updateSelectedTabIndex(index:Int){
        _state.update {
            it.copy(
                selectedTabIndex = index
            )
        }
    }

    fun setPrivacyBottomSheetState(value: Boolean){
        _state.update {
            it.copy(
                showPrivacyBottomSheet = value
            )
        }
    }

    fun setReelsPrivacy(setting: ReelPrivacySetting){
        _state.update {
            it.copy(
                reelsPrivacySettings = setting
            )
        }
    }

    fun setPromptType(promptType: PromptScreenState.PromptType?){
        _state.update {
            it.copy(
                promptType = promptType
            )
        }
    }

    fun updateEditTitleState(value: Boolean){
        _state.update {
            it.copy(
                editTitleEnabled = value
            )
        }
    }

    fun updateTextPrompt(value:String){
        _state.update {
            it.copy(
                textPrompt = value
            )
        }
    }

    fun updatePromptTitle(title:String){
        _state.update {
            it.copy(
                promptTitle = title
            )
        }
    }


}