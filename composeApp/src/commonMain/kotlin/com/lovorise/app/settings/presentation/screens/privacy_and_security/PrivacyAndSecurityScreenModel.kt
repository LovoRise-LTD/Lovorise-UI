package com.lovorise.app.settings.presentation.screens.privacy_and_security

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PrivacyAndSecurityScreenModel : ScreenModel {

    private val _state = MutableStateFlow(PrivacyAndSecurityState())
    val state = _state.asStateFlow()

    fun updatePhotoPrivacy(data: PrivacyAndSecurityState.PrivacyData){
        _state.update {
            it.copy(
                photosAndVideos = data
            )
        }
    }

    fun updateStoryPrivacy(data: PrivacyAndSecurityState.PrivacyData){
        _state.update {
            it.copy(
                story = data
            )
        }
    }

    fun updateShareProfilePrivacy(data: PrivacyAndSecurityState.PrivacyData){
        _state.update {
            it.copy(
                shareProfile = data
            )
        }
    }

    fun updateLastSeenPrivacy(data: PrivacyAndSecurityState.PrivacyData){
        _state.update {
            it.copy(
                lastSeenAndOnline = data
            )
        }
    }

    fun updateReadReceiptPrivacy(data: PrivacyAndSecurityState.PrivacyData){
        _state.update {
            it.copy(
                readReceipt = data
            )
        }
    }

    fun updateLocation(data: PrivacyAndSecurityState.PrivacyData){
        _state.update {
            it.copy(
                location = data
            )
        }
    }



}