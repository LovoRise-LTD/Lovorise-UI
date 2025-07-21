package com.lovorise.app.libs.permissions.ios

import com.lovorise.app.libs.permissions.PermissionState


internal class AlwaysGrantedPermissionDelegate : PermissionDelegate {
    override suspend fun providePermission() = Unit

    override suspend fun getPermissionState(): PermissionState = PermissionState.Granted
}
