package com.lovorise.app.libs.location

import coil3.PlatformContext
import kotlinx.coroutines.flow.Flow

expect class LocationManager(context: PlatformContext) {
    fun getCurrentLocation(): Flow<LocationData?>
}