package com.lovorise.app.libs.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import coil3.PlatformContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import android.location.LocationManager as AndroidLocationManager

actual class LocationManager actual constructor(context: PlatformContext) {
    private val appContext: Context = context // Store the context explicitly


    @SuppressLint("MissingPermission") // Make sure permissions are handled before calling this
    actual fun getCurrentLocation(): Flow<LocationData?> = callbackFlow {
//        val isPermissionGranted = ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        if (!isPermissionGranted) return@suspendCancellableCoroutine continuation.resume(null)

        val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as AndroidLocationManager
        val locationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: Location) {
                try {
                    val geocoder = Geocoder(appContext, Locale.getDefault())
                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()

                    val locationData = LocationData(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        city = address?.locality,
                        country = address?.countryName
                    )

                    trySend(locationData).isSuccess
                } catch (e: Exception) {
                    e.printStackTrace()
                    trySend(null).isSuccess
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {
                println("the provider is $provider")
            }
            override fun onProviderEnabled(provider: String) {
                println("the gps is enabled!!")
            }
            override fun onProviderDisabled(provider: String) {
                println("the gps is disabled!!")
            }
        }

        try {
            // Request location updates to get the current location
            locationManager.requestLocationUpdates(
                AndroidLocationManager.GPS_PROVIDER,
                1000L, // Minimum time interval between updates
                10f, // Minimum distance interval between updates
                locationListener,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(null).isSuccess
           // close(e)
            // Resume coroutine with an exception if something goes wrong
           // continuation.resumeWithException(e)
        }

        // Clean up the listener if the coroutine is cancelled
        awaitClose {
            locationManager.removeUpdates(locationListener)
        }
    }
}