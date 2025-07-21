package com.lovorise.app.accounts.presentation.signup.email

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class VerifyEmailScreenState(
    val timerCountDown: Flow<Int> = emptyFlow(),
    val showTimer:Boolean = false
)
