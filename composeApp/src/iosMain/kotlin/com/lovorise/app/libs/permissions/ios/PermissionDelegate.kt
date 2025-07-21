package com.lovorise.app.libs.permissions.ios

import com.lovorise.app.libs.permissions.PermissionState

internal interface PermissionDelegate {
    suspend fun providePermission()
    suspend fun getPermissionState(): PermissionState
}
