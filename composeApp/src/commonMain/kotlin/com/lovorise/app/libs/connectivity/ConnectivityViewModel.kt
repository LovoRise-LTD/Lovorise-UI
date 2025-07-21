package com.lovorise.app.libs.connectivity

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.plusmobileapps.konnectivity.Konnectivity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ConnectivityViewModel : ScreenModel {

    val isConnected = Konnectivity()
        .isConnectedState
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(5000L),
            true
        )
}