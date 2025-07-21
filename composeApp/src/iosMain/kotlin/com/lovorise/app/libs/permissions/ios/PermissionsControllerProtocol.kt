package com.lovorise.app.libs.permissions.ios

import com.lovorise.app.libs.permissions.Permission
import com.lovorise.app.libs.permissions.PermissionState

interface PermissionsControllerProtocol {
    suspend fun providePermission(permission: Permission)
    suspend fun isPermissionGranted(permission: Permission): Boolean
    suspend fun getPermissionState(permission: Permission): PermissionState
    fun openAppSettings()
}
