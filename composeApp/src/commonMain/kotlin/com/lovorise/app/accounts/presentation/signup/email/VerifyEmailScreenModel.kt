package com.lovorise.app.accounts.presentation.signup.email

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class VerifyEmailScreenModel : ScreenModel {

    private var _state = MutableStateFlow(VerifyEmailScreenState())

    val state = _state.asStateFlow()

    fun startCountDown(){
        if (state.value.showTimer) return
        _state.update {
            it.copy(
                timerCountDown = countdownFlow(59),
                showTimer = true
            )
        }
    }

    private fun countdownFlow(start: Int): Flow<Int> = flow {
        var current = start
        while (current >= 0) {

            emit(current)

            delay(1000L)

            if (current == 0){
                _state.update {
                    it.copy(
                        showTimer = false
                    )
                }
            }

            current--


        }
    }

}