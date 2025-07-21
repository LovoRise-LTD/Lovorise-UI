package com.lovorise.app.swipe.domain

import com.lovorise.app.accounts.domain.model.UserResponse

data class SwipeProfileUser(
    val user:UserResponse,
    val offsetY: Float = 0f,
    val offsetX:Float? = null
)
