package com.lovorise.app.libs.permissions.ios

import com.lovorise.app.libs.permissions.LocationManagerDelegate
import com.lovorise.app.libs.permissions.Permission

import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.AVMediaTypeVideo
import platform.Contacts.CNContactStore
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import com.lovorise.app.libs.permissions.PermissionState

class PermissionsController : PermissionsControllerProtocol {
    private val locationManagerDelegate = LocationManagerDelegate()
    private val contactStore = CNContactStore()

    override suspend fun providePermission(permission: Permission) {
        return getDelegate(permission).providePermission()
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean {
        return getDelegate(permission).getPermissionState() == PermissionState.Granted
    }

    override suspend fun getPermissionState(permission: Permission): PermissionState {
        return getDelegate(permission).getPermissionState()
    }

    override fun openAppSettings() {
//        val settingsUrl: NSURL = NSURL.URLWithString(UIApplicationOpenSettingsURLString)!!
//        UIApplication.sharedApplication.openURL(settingsUrl)
        val settingsUrl: NSURL? = NSURL.URLWithString(UIApplicationOpenSettingsURLString)

        if (settingsUrl != null) {
            UIApplication.sharedApplication.openURL(settingsUrl, options = emptyMap<Any?, Any>()) { success ->
                if (success) {
                    println("Opened Settings successfully")
                } else {
                    println("Failed to open Settings")
                }
            }
        }
    }

    private fun getDelegate(permission: Permission): PermissionDelegate {
        return when (permission) {
            Permission.REMOTE_NOTIFICATION -> RemoteNotificationPermissionDelegate()
            Permission.CAMERA -> AVCapturePermissionDelegate(AVMediaTypeVideo, permission)
            Permission.GALLERY -> GalleryPermissionDelegate()
            Permission.STORAGE, Permission.WRITE_STORAGE -> AlwaysGrantedPermissionDelegate()
            Permission.LOCATION, Permission.COARSE_LOCATION, Permission.BACKGROUND_LOCATION ->
                LocationPermissionDelegate(locationManagerDelegate, permission)

            Permission.RECORD_AUDIO -> AVCapturePermissionDelegate(AVMediaTypeAudio, permission)


           // Permission.CONTACTS->ContactsPermissionDelegate(permission,contactStore)

          //  Permission.MOTION -> MotionPermissionDelegate()
        }
    }
}
