package com.lovorise.app

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.location.LocationManager
import androidx.activity.ComponentActivity
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.lovorise.app.util.GpsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidGpsProvider(private val activity: ComponentActivity) : GpsProvider {

    private val requestGPSActivation = activity.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onGPSResultListener?.invoke(true) // GPS enabled
        } else {
            onGPSResultListener?.invoke(false) // GPS not enabled
        }
    }

//    private val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    private var onGPSResultListener: ((Boolean) -> Unit)? = null


    private val _gpsStatus = MutableStateFlow(true)


    override val currentGpsState: StateFlow<Boolean> = _gpsStatus.asStateFlow()


    override fun isGpsEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    override fun promptEnableGPS(onResult: (Boolean) -> Unit) {
        onGPSResultListener = onResult

        val settingsClient = LocationServices.getSettingsClient(activity)
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()
            )
            .setAlwaysShow(true)
            .build()

        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                // GPS is already enabled
                onGPSResultListener?.invoke(true)
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        requestGPSActivation.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        onGPSResultListener?.invoke(false)
                    }
                } else {
                    onGPSResultListener?.invoke(false)
                }
            }
    }

    private val gpsStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            checkGpsStatus()
        }
    }

    override fun startGpsListener() {
        registerReceiver()
        checkGpsStatus()
    }

    override fun stopGpsListener() {
        unRegisterReceiver()
    }


    private fun checkGpsStatus() {
        val isEnabled = isLocationEnabled()
        _gpsStatus.value = isEnabled // Update the current GPS status in the StateFlow
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun registerReceiver() {
        activity.registerReceiver(
            gpsStatusReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
    }

    private fun unRegisterReceiver() {
        activity.unregisterReceiver(gpsStatusReceiver)
    }



}
