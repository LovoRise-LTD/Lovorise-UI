package com.lovorise.app.util

import kotlinx.coroutines.flow.StateFlow

interface GpsProvider {
    val currentGpsState:StateFlow<Boolean>
    fun isGpsEnabled():Boolean
    fun promptEnableGPS(onResult: (Boolean) -> Unit)
    fun startGpsListener()
    fun stopGpsListener()
}