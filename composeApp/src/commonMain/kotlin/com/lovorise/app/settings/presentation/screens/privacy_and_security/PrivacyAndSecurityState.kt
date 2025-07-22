package com.lovorise.app.settings.presentation.screens.privacy_and_security

data class PrivacyAndSecurityState(
    val photosAndVideos: PrivacyData = PrivacyData(),
    val story: PrivacyData = PrivacyData(),
    val shareProfile: PrivacyData = PrivacyData(),
    val lastSeenAndOnline: PrivacyData = PrivacyData(),
    val readReceipt: PrivacyData = PrivacyData(),
    val location: PrivacyData = PrivacyData()
){
    data class PrivacyData(
        val privacyOption: PrivacyOptions = PrivacyOptions.EVERYBODY,
        val neverShareWith: List<String> = emptyList()
    )
}

enum class PrivacyOptions{
    EVERYBODY,LIKES_AND_MATCHES,MATCHES_ONLY,NOBODY
}