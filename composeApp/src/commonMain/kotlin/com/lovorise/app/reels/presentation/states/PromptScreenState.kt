package com.lovorise.app.reels.presentation.states

import com.lovorise.app.reels.presentation.reels_create_upload_view.components.ReelPrivacySetting

data class PromptScreenState(
    val selectedTabIndex:Int = 0,
    val textPrompt:String = "",
    val promptTitle:String = "",
    val promptType:PromptType? = null,
    val editTitleEnabled:Boolean = false,
    val showPrivacyBottomSheet:Boolean = false,
    val reelsPrivacySettings: ReelPrivacySetting = ReelPrivacySetting.Everyone
){
    enum class PromptType{
        TEXT,VOICE,VIDEO
    }
}
