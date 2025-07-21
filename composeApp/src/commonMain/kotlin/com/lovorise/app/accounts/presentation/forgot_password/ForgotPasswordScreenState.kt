package com.lovorise.app.accounts.presentation.forgot_password

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ForgotPasswordScreenState(
    val timerCountDown: Flow<Int> = emptyFlow(),
    val showTimer:Boolean = false
)
