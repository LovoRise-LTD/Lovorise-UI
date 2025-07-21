package com.lovorise.app.profile.domain.repo

data class UpdateProfileData<T>(
    val isVisible:Boolean = false,
    val value:T
)
