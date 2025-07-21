package com.lovorise.app.libs.permissions_compose

import androidx.compose.runtime.Composable
import com.lovorise.app.libs.permissions.PermissionsController

fun interface PermissionsControllerFactory {
    fun createPermissionsController(): PermissionsController
}

@Composable
expect fun rememberPermissionsControllerFactory(): PermissionsControllerFactory
