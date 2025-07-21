package com.lovorise.app.libs.location

import coil3.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyNearestTenMeters
import platform.Foundation.NSError
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
actual class LocationManager actual constructor(context: PlatformContext) : NSObject(), CLLocationManagerDelegateProtocol {
    private val locationManager = CLLocationManager()
    private var locationCallback: ((LocationData) -> Unit)? = null

    init {
        locationManager.delegate = this
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.requestWhenInUseAuthorization()
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {

        when (locationManager.authorizationStatus) {
            kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> {
                println("Authorized to access location")
                // You can start updating location here if needed
                // locationManager.startUpdatingLocation()
            }
            kCLAuthorizationStatusDenied -> {
                println("Location access denied.")
            }
            kCLAuthorizationStatusRestricted -> {
                println("Location access restricted.")
            }
            kCLAuthorizationStatusNotDetermined -> {
                locationManager.requestWhenInUseAuthorization()
            }
            else -> {
                // Handle unknown status if needed
            }
        }

    }


    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        //  super.locationManager(manager, didUpdateLocations)
        val location = didUpdateLocations.lastOrNull() as? CLLocation ?: return
        val geocoder = CLGeocoder()
        geocoder.reverseGeocodeLocation(location) { placeMarks, error ->
            if (error != null) {
                println("error is $error")
              //  currentLocationContinuation?.resumeWithException(Exception(error.localizedDescription))
                return@reverseGeocodeLocation
            }
            val placeMark = placeMarks?.firstOrNull() as? CLPlacemark
            val city = placeMark?.locality
            val country = placeMark?.country

            val locData = LocationData(
                latitude = location.coordinate.useContents { latitude },
                longitude = location.coordinate.useContents { longitude },
                city = city,
                country = country
            )

            locationCallback?.invoke(locData)
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        println("Failed to fetch location: ${didFailWithError.localizedDescription}")
    }

    actual fun getCurrentLocation(): Flow<LocationData?> = callbackFlow {
        locationCallback = { locationData ->
            trySend(locationData).isSuccess
        }

        locationManager.startUpdatingLocation()

        awaitClose {
            locationCallback = null
            locationManager.stopUpdatingLocation()
        }
    }
}
